package com.denfop.tiles.mechanism.energy;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyController;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.Path;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerController;
import com.denfop.gui.GuiEnergyController;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TileEnergyController extends TileEntityInventory implements
        IUpdatableTileEvent, IEnergyController {

    public List<Path> energyPathList = new ArrayList<>();
    public boolean work = false;
    public int size;
    private ChunkPos chunkPos;

    public TileEnergyController() {
        this.addComponent(Energy.asBasicSink(this, 0, 14));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.energy_controller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(Localization.translate("iu.controller.info"));
        tooltip.add(Localization.translate("iu.controller.info1"));
    }

    Map<EnumFacing, IEnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();


    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

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
    public long getIdNetwork() {
        return this.id;
    }


    public void setId(final long id) {
        this.id = id;
    }
    private long id;
    int hashCodeSource;
    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
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
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this));

        }

    }

    @Override
    public void onUnloaded() {
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EventUnloadController(this));
        }
        super.onUnloaded();
    }


    @Override
    public ContainerController getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerController(this, entityPlayer);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiEnergyController(getGuiContainer(entityPlayer));
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

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (this.world.isRemote) {
            return;
        }
        if (i == 0) {
            energyPathList.clear();
            for (EnumFacing facing : EnumFacing.values()) {
                final List<Path> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing)
                );

                for (Path path : energyPathList1) {
                    if (!energyPathList.contains(path)) {
                        energyPathList.add(path);
                    }
                }
            }
            this.size = energyPathList.size();
        } else if (i == 1) {
            this.work = true;
            energyPathList.forEach(energyPath -> energyPath.setHasController(true));
        }
    }

    @Override
    public boolean getWork() {
        return this.work;
    }

    @Override
    public void work() {
        if (this.getWork()) {
            energyPathList.clear();
            for (EnumFacing facing : EnumFacing.values()) {
                final List<Path> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing)
                );

                for (Path path : energyPathList1) {
                    if (!energyPathList.contains(path)) {
                        energyPathList.add(path);
                    }
                }
            }
            this.size = energyPathList.size();
            energyPathList.forEach(energyPath -> energyPath.setHasController(true));
        }
    }

    @Override
    public void unload() {
        energyPathList.forEach(energyPath -> energyPath.setHasController(false));

    }


    @Override
    public TileEntity getTileEntity() {
        return this;
    }

}
