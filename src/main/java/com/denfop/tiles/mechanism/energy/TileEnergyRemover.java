package com.denfop.tiles.mechanism.energy;

import com.denfop.IUItem;
import com.denfop.api.energy.EnergyNetGlobal;
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
import com.denfop.container.ContainerRemover;
import com.denfop.gui.GuiEnergyRemover;
import com.denfop.invslot.Inventory;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.world.WorldBaseGen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileEnergyRemover extends TileEntityInventory implements
        IUpdatableTileEvent, IEnergyController {

    public final Inventory slot;
    public Set<IEnergyConductor> conductorList = new HashSet<>();
    FakePlayerSpawner fakePlayer;
    Map<EnumFacing, IEnergyTile> energyConductorMap = new HashMap<>();
    int hashCodeSource;
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    private boolean work;
    private long id;

    public TileEnergyRemover() {
        slot = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, 16);
        this.addComponent(Energy.asBasicSink(this, 0, 14));
    }

    public void RemoveTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
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
        return BlockBaseMachine3.energy_remover;
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
        if (!this.getWorld().isRemote) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this));
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
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EventUnloadController(this));
        }
        super.onUnloaded();
    }

    @Override
    public ContainerRemover getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerRemover(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiEnergyRemover(getGuiContainer(entityPlayer));
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
                    tileEntitiesToCheck.push(validReceiver.tileEntity);
                    reachedTileEntities.add((IEnergyConductor) validReceiver.tileEntity);
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
                        conductor);
                tile.onUnloaded();
                final List<ItemStack> drops = tile.getBlockType().getDrops(
                        world,
                        tile.getPos(),
                        tile.getBlockState(),
                        100
                );
                if (this.slot.add(drops.get(0))) {
                    conductor.removeConductor();
                }

            }

            this.work = false;
            discover();
        }
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (this.world.isRemote) {
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
