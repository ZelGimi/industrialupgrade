package com.denfop.blockentity.mechanism.energy;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.event.load.EventLoadController;
import com.denfop.api.energy.event.unload.EventUnloadController;
import com.denfop.api.energy.interfaces.EnergyConductor;
import com.denfop.api.energy.interfaces.EnergyController;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.base.FakePlayerSpawner;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuRemover;
import com.denfop.inventory.Inventory;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenEnergyRemover;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class BlockEntityEnergyRemover extends BlockEntityInventory implements
        IUpdatableTileEvent, EnergyController {

    public final Inventory slot;
    public Set<EnergyConductor> conductorList = new HashSet<>();
    FakePlayerSpawner fakePlayer;
    Map<Direction, EnergyTile> energyConductorMap = new HashMap<>();
    int hashCodeSource;
    List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
    private boolean work;
    private long id;

    public BlockEntityEnergyRemover(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.energy_remover, pos, state);
        slot = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, 16);
        this.addComponent(Energy.asBasicSink(this, 0, 14));
    }

    public void RemoveTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<EnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<EnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public void AddTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
        }
    }

    public Map<Direction, EnergyTile> getTiles() {
        return energyConductorMap;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.energy_remover;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this, level));
            fakePlayer = new FakePlayerSpawner(this.getWorld());

        }

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
    }


    @Override
    public void onUnloaded() {
        if (!this.getWorld().isClientSide) {
            MinecraftForge.EVENT_BUS.post(new EventUnloadController(this, level));
        }
        super.onUnloaded();
    }

    @Override
    public ContainerMenuRemover getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuRemover(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenEnergyRemover((ContainerMenuRemover) menu);
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
        final LinkedList<EnergyTile> tileEntitiesToCheck = new LinkedList<>();
        final LinkedList<EnergyConductor> reachedTileEntities = new LinkedList<>();
        EnergyTile tile = this.getComp(Energy.class).delegate;
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        while (!tileEntitiesToCheck.isEmpty()) {
            final EnergyTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<EnergyTile>> validReceivers = currentTileEntity.getValidReceivers();
            for (final InfoTile<EnergyTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id && validReceiver.tileEntity instanceof EnergyConductor) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof EnergyConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);
                        reachedTileEntities.add((EnergyConductor) validReceiver.tileEntity);
                    }
                }
            }
        }
        conductorList.addAll(reachedTileEntities);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work) {
            for (EnergyConductor conductor : this.conductorList) {
                BlockEntityBase tile = (BlockEntityBase) EnergyNetGlobal.instance.getBlockPosFromEnergyTile(
                        conductor, level);
                final List<ItemStack> drops = tile.getBlock().getDrops(
                        level,
                        tile.getPos(),
                        tile.getBlockState(), null
                );
                if (this.slot.add(drops.get(0))) {
                    tile.onUnloaded();

                    this.getWorld().removeBlock(tile.getPos(), false);
                }

            }

            this.work = false;
            discover();
        }
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        if (this.level.isClientSide) {
            return;
        }
        if (i == 0) {
            discover();
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
    }


}
