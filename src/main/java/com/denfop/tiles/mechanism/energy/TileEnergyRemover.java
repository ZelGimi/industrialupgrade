package com.denfop.tiles.mechanism.energy;

import com.denfop.IUItem;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergyController;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRemover;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiEnergyRemover;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class TileEnergyRemover extends TileEntityInventory implements
        IUpdatableTileEvent, IEnergyController {

    public final InvSlot slot;
    public Set<IEnergyConductor> conductorList = new HashSet<>();
    FakePlayerSpawner fakePlayer;
    Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
    int hashCodeSource;
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    private boolean work;
    private long id;

    public TileEnergyRemover(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.energy_remover, pos,state);
        slot = new InvSlot(this, InvSlot.TypeItemSlot.OUTPUT, 16);
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
        return BlockBaseMachine3.energy_remover;
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

    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this,level));
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
            MinecraftForge.EVENT_BUS.post(new EventUnloadController(this,level));
        }
        super.onUnloaded();
    }

    @Override
    public ContainerRemover getGuiContainer(final Player entityPlayer) {
        return new ContainerRemover(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiEnergyRemover((ContainerRemover) menu);
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
        final LinkedList<IEnergyTile> tileEntitiesToCheck = new LinkedList<>();
        final LinkedList<IEnergyConductor> reachedTileEntities = new LinkedList<>();
        IEnergyTile tile = this.getComp(Energy.class).delegate;
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        while (!tileEntitiesToCheck.isEmpty()) {
            final IEnergyTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<IEnergyTile>> validReceivers = currentTileEntity.getValidReceivers();
            for (final InfoTile<IEnergyTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id && validReceiver.tileEntity instanceof IEnergyConductor) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof IEnergyConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);
                        reachedTileEntities.add((IEnergyConductor) validReceiver.tileEntity);
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
            for (IEnergyConductor conductor : this.conductorList) {
                TileEntityBlock tile = (TileEntityBlock) EnergyNetGlobal.instance.getBlockPosFromEnergyTile(
                        conductor,level);
                final List<ItemStack> drops = tile.getBlock().getDrops(
                        level,
                        tile.getPos(),
                        tile.getBlockState(),null
                );
                if (this.slot.add(drops.get(0))) {
                tile.onUnloaded();


                   this.getWorld().removeBlock( tile.getPos(),false);
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
