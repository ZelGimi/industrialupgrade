package com.denfop.tiles.mechanism.energy;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.EnergyNetLocal;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergyController;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerSubstitute;
import com.denfop.gui.GuiEnergySubstitute;
import com.denfop.invslot.CableItem;
import com.denfop.invslot.InvSlotSubstitute;
import com.denfop.items.block.ItemBlockTileEntity;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private ChunkPos chunkPos;

    public TileEnergySubstitute() {
        slot = new InvSlotSubstitute(this);
        this.addComponent(Energy.asBasicSink(this, 0, 14));
    }

    Map<EnumFacing, IEnergyTile> energyConductorMap = new HashMap<>();

    public void RemoveTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()){
                InfoTile<IEnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();


    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }
    public long getIdNetwork() {
        return this.id;
    }
    int hashCodeSource;
    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    public void setId(final long id) {
        this.id = id;
    }
    private long id;
    public void AddTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
        }
    }
    public Map<EnumFacing, IEnergyTile> getTiles() {
        return energyConductorMap;
    }
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.substitute;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @SideOnly(Side.CLIENT)
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
        if (!this.getWorld().isRemote) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this));
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

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public void onUnloaded() {
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EventUnloadController(this));
        }
        super.onUnloaded();
    }

    @Override
    public ContainerSubstitute getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerSubstitute(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiEnergySubstitute(getGuiContainer(entityPlayer));
    }


    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        nbt.setBoolean("work", work);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.work = nbtTagCompound.getBoolean("work");

    }

    public void discover() {
        conductorList.clear();
        size = 0;
        final List<IEnergyConductor> reachedTileEntities = new ArrayList<>();
        final List<IEnergyTile> tileEntitiesToCheck = new ArrayList<>();
        tileEntitiesToCheck.add(this);
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

                        for (ItemStack stack : this.slot.gets()) {

                            if (stack.isItemEqual(main_cableItem.getStack()) && (ModUtils.nbt(main_cableItem.getStack()).equals(
                                    ModUtils.nbt(stack)))) {
                                if (main_cableItem.getStack().getItem() instanceof com.denfop.items.transport.ItemCable) {
                                    final com.denfop.tiles.transport.tiles.TileEntityCable te =
                                            com.denfop.tiles.transport.tiles.TileEntityCable.delegate(com.denfop.items.transport.ItemCable.getCableType(
                                                    main_cableItem.getStack()
                                            ));
                                    TileEntityBlock tile = (TileEntityBlock) EnergyNetGlobal.instance.getBlockPosFromEnergyTile(
                                            conductor);
                                    EnumFacing facing = tile.getFacing().getOpposite();
                                    final List<ItemStack> drops = tile.getBlockType().getDrops(
                                            world,
                                            tile.getPos(),
                                            tile.getBlockState(),
                                            100
                                    );
                                    if (!drops.isEmpty()) {
                                        itemStackList.add(drops.get(0));
                                    }
                                    tile.onUnloaded();
                                    conductor.removeConductor();
                                    BlockPos pos = tile.getPos();

                                    if (ItemBlockTileEntity.placeTeBlock(main_cableItem.getStack(),
                                            fakePlayer,
                                            this.getWorld(),
                                            pos,
                                            facing
                                            ,
                                            te
                                    )) {
                                        stack.shrink(1);
                                        main_cableItem.shrink(1);
                                        com.denfop.tiles.transport.tiles.TileEntityCable cable =
                                                (com.denfop.tiles.transport.tiles.TileEntityCable) this.getWorld().getTileEntity(
                                                        pos);
                                        break;
                                    }
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
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (this.world.isRemote) {
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
