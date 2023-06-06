package com.denfop.tiles.mechanism.energy;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.EnergyNetLocal;
import com.denfop.api.energy.IEnergyController;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.api.inv.IHasGui;
import com.denfop.container.ContainerController;
import com.denfop.gui.GuiEnergyController;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityEnergyController extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener, IEnergyController {

    public List<EnergyNetLocal.EnergyPath> energyPathList = new ArrayList<>();
    public boolean work = false;
    public int size;

    public TileEntityEnergyController() {

    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.controller.info"));
        tooltip.add(Localization.translate("iu.controller.info1"));
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this));
        }

    }

    @Override
    protected void onUnloaded() {
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
    public void onGuiClosed(final EntityPlayer entityPlayer) {

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
    protected void updateEntityServer() {
        super.updateEntityServer();
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        if (this.world.isRemote) {
            return;
        }
        if (i == 0) {
            energyPathList.clear();
            for (EnumFacing facing : EnumFacing.values()) {
                final List<EnergyNetLocal.EnergyPath> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing)
                );

                for (EnergyNetLocal.EnergyPath path : energyPathList1) {
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
                final List<EnergyNetLocal.EnergyPath> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing)
                );

                for (EnergyNetLocal.EnergyPath path : energyPathList1) {
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
