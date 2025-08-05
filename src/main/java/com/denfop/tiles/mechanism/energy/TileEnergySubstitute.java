package com.denfop.tiles.mechanism.energy;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.*;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSubstitute;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiEnergySubstitute;
import com.denfop.invslot.CableItem;
import com.denfop.invslot.InvSlotSubstitute;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;

public class TileEnergySubstitute extends TileEntityInventory implements
        IUpdatableTileEvent, IEnergyController {

    public final InvSlotSubstitute slot;
    public Set<IEnergyConductor> conductorList = new HashSet<>();
    public boolean work = false;
    public int size;
    public int max_value = 0;
    List<CableItem> cableItemList = new ArrayList<>();
    CableItem main_cableItem = null;
    FakePlayerSpawner fakePlayer;
    Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    int hashCodeSource;
    private ChunkPos chunkPos;
    private long id;


    public TileEnergySubstitute(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.substitute,pos,state);
        slot = new InvSlotSubstitute(this);
        this.addComponent(Energy.asBasicSink(this, 0, 14));
    }

    public void RemoveTile(IEnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IEnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public long getIdNetwork() {
        return this.id;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void AddTile(IEnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
        }
    }

    public Map<Direction, IEnergyTile> getTiles() {
        return energyConductorMap;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.substitute;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(Localization.translate("iu.controller_cables.info"));
        tooltip.add(Localization.translate("iu.controller_cables.info1"));
    }

    public List<CableItem> getCableItemList() {
        return cableItemList;
    }

    public void setCableItemList(final List<CableItem> cableItemList) {
        this.cableItemList = cableItemList;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this,level));
            fakePlayer = new FakePlayerSpawner(this.getWorld());
            this.slot.onChanged();

        }

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            size = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, size);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }



    @Override
    public void onUnloaded() {
        if (!this.getWorld().isClientSide) {
            MinecraftForge.EVENT_BUS.post(new EventUnloadController(this,level));
        }
        super.onUnloaded();
    }

    @Override
    public ContainerSubstitute getGuiContainer(final Player entityPlayer) {
        return new ContainerSubstitute(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiEnergySubstitute((ContainerSubstitute) menu);
    }


    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        nbt.putBoolean("work", work);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.work = nbtTagCompound.getBoolean("work");

    }

    public void discover() {
        conductorList.clear();
        size = 0;
        final List<IEnergyConductor> reachedTileEntities = new ArrayList<>();
        final List<IEnergyTile> tileEntitiesToCheck = new ArrayList<>();
        tileEntitiesToCheck.add(this.getComp(Energy.class).delegate);
        EnergyNetLocal energyNetLocal = EnergyNetGlobal.getForWorld(this.getWorld());
        while (!tileEntitiesToCheck.isEmpty()) {
            final IEnergyTile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<InfoTile<IEnergyTile>> validReceivers = energyNetLocal.getValidReceiversSubstitute(currentTileEntity);

            for (final InfoTile<IEnergyTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != this && validReceiver.tileEntity instanceof IEnergyConductor) {
                    if (reachedTileEntities.contains((IEnergyConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.add((IEnergyConductor) validReceiver.tileEntity);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        conductorList.addAll(reachedTileEntities);
        size = conductorList.size();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work) {
            if (main_cableItem != null) {
                List<ItemStack> itemStackList = new ArrayList<>();
                for (IEnergyConductor conductor : this.conductorList) {
                    if (conductor.getConductorBreakdownEnergy() - 1 < this.main_cableItem.getProvider()) {

                        for (ItemStack stack : this.slot) {

                            if (stack.is(main_cableItem.getStack().getItem()) && main_cableItem.getStack().getItem() instanceof ItemBlockTileEntity<?> && (ModUtils.nbt(main_cableItem.getStack()).equals(
                                    ModUtils.nbt(stack)))) {
                                    TileEntityBlock tile = (TileEntityBlock) EnergyNetGlobal.instance.getBlockPosFromEnergyTile(
                                            conductor,level);
                                    final List<ItemStack> drops = tile.getBlock().getDrops(level,
                                            tile.getPos(),
                                            tile.getBlockState(),
                                            null
                                    );
                                    if (!drops.isEmpty()) {
                                        itemStackList.add(drops.get(0));
                                    }
                                    tile.onUnloaded();
                                    conductor.removeConductor();
                                    BlockPos pos = tile.getPos();
                                  ItemBlockTileEntity<?> itemBlockTileEntity = (ItemBlockTileEntity<?>) main_cableItem.getStack().getItem();
                                  if (itemBlockTileEntity.placeTeBlock(main_cableItem.getStack(),
                                            fakePlayer,
                                            this.getWorld(),
                                            pos
                                  )) {
                                        stack.shrink(1);
                                        main_cableItem.shrink(1);
                                        com.denfop.tiles.transport.tiles.TileEntityCable cable =
                                                (com.denfop.tiles.transport.tiles.TileEntityCable) this.getWorld().getBlockEntity(
                                                        pos);
                                        break;
                                    }
                            }
                        }
                        if (main_cableItem.getCount() == 0) {
                            this.slot.onChanged();
                            if (main_cableItem == null) {
                                break;
                            }
                        }
                    }

                }
                for (ItemStack stack : itemStackList) {
                    this.slot.add(stack);
                }
            }
            this.size = 0;
            this.work = false;
            discover();
            this.main_cableItem = null;
            this.slot.onChanged();
        }
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        if (this.level.isClientSide) {
            return;
        }
        if (i == 0) {
            discover();
            this.main_cableItem = null;
            this.slot.onChanged();
        } else if (i == 1) {
            this.work = true;


        }
    }

    @Override
    public boolean getWork() {
        return this.work;
    }

    @Override
    public void work() {
        if (this.getWork()) {
            discover();

        }
    }

    @Override
    public void unload() {
        this.conductorList.clear();
        this.size = 0;
    }


    public void setMaxValue(int max_value, double provider, final CableItem cableItem) {
        if (provider > max_value) {
            this.max_value = (int) provider;
            this.main_cableItem = cableItem;
        }
    }

}
