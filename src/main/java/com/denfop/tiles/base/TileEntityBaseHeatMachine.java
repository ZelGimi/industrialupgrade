package com.denfop.tiles.base;

import com.denfop.api.ITemperature;
import com.denfop.api.ITemperatureSourse;
import com.denfop.api.Recipes;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityBaseHeatMachine extends TileEntityElectricMachine implements IFluidHandler, ITemperatureSourse {


    public final boolean hasFluid;
    public final FluidTank fluidTank;
    public final String name;
    public final short maxtemperature;
    public short temperature;

    public TileEntityBaseHeatMachine(String name, boolean hasFluid) {
        super("", hasFluid ? 0D : 10000D, 14, 1);
        this.hasFluid = hasFluid;
        this.fluidTank = new FluidTank(12000);
        this.name = name;
        this.maxtemperature = 5000;
        this.temperature = 0;

    }
    @Override
    public boolean reveiver() {
        return false;
    }
    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        return true;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.fluidTank.readFromNBT(nbttagcompound.getCompoundTag("fluidTank"));

        this.temperature = nbttagcompound.getShort("temperature");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagCompound fluidTankTag = new NBTTagCompound();
        this.fluidTank.writeToNBT(fluidTankTag);
        nbttagcompound.setTag("fluidTank", fluidTankTag);
        nbttagcompound.setShort("temperature", this.temperature);
        return nbttagcompound;

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        IC2.network.get(true).updateTileEntityField(this, "temperature");
        IC2.network.get(true).updateTileEntityField(this, "fluidTank");
        setActive(Recipes.mechanism.process(this));
        for (EnumFacing direction : EnumFacing.values()) {
            TileEntity target = world.getTileEntity(new BlockPos(this.pos.getX() + direction.getFrontOffsetX(),
                    this.pos.getY() + direction.getFrontOffsetY(), this.pos.getZ() + direction.getFrontOffsetZ()
            ));
            if (target instanceof ITemperature && !(target instanceof TileEntityBaseHeatMachine)) {
                Recipes.mechanism.transfer((ITemperature) target, this);
            }
        }

    }


    @Override
    public String getInventoryName() {
        return Localization.translate(name);
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }


    public boolean canFill(Fluid fluid) {
        return hasFluid && fluid.equals(FluidRegistry.LAVA);
    }

    public boolean canDrain(Fluid fluid) {
        return hasFluid;
    }


    @Override
    public short getTemperature() {
        return this.temperature;
    }

    @Override
    public void setTemperature(short temperature) {
        this.temperature = temperature;
    }

    @Override
    public short getMaxTemperature() {
        return this.maxtemperature;
    }

    @Override
    public boolean isFluidTemperature() {
        return this.hasFluid;
    }

    @Override
    public FluidStack getFluid() {
        return fluidTank.getFluid();
    }

    @Override
    public TileEntityElectricMachine getTile() {
        return this;
    }


    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return null;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.fluidTank.getTankProperties();
    }

    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
        if (!canFill(resource.getFluid())) {
            return 0;
        }
        return getFluidTank().fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(getFluidTank().getFluid())) {
            return null;
        }
        if (!canDrain(resource.getFluid())) {
            return null;
        }
        return getFluidTank().drain(resource.amount, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        if (!canDrain(null)) {
            return null;
        }
        return getFluidTank().drain(maxDrain, doDrain);
    }

}
