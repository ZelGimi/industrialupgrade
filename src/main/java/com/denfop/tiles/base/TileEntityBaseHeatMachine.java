package com.denfop.tiles.base;

import com.denfop.api.ITemperature;
import com.denfop.api.ITemperatureSourse;
import com.denfop.api.Recipes;
import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatSource;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.container.ContainerHeatMachine;
import com.denfop.gui.GuiHeatMachine;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.block.comp.Energy;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityBaseHeatMachine extends TileEntityElectricMachine implements INetworkClientTileEntityEventListener,
        IFluidHandler, ITemperatureSourse, IHeatSource {


    public final boolean hasFluid;
    public final String name;
    public final short maxtemperature;
    public boolean auto;
    public FluidTank fluidTank;
    public Fluids fluids = null;
    public short temperature;
    public InvSlotConsumableLiquid fluidSlot;

    public TileEntityBaseHeatMachine(String name, boolean hasFluid) {
        super("", hasFluid ? 0D : 10000D, 14, 1);
        this.hasFluid = hasFluid;
        this.fluidTank = new FluidTank(12000);
        if (this.hasFluid) {
            this.energy = this.addComponent(Energy.asBasicSink(this, 0, 14));
        }
        if (this.hasFluid) {
            this.fluids = this.addComponent(new Fluids(this));
            this.fluidTank = this.fluids.addTank("fluidTank", 12000, InvSlot.Access.I, InvSlot.InvSide.ANY, Fluids.fluidPredicate(
                    FluidRegistry.getFluid("cryotheum"), FluidRegistry.getFluid("petrotheum"),
                    FluidRegistry.getFluid("redstone"), FluidRegistry.getFluid("ic2pahoehoe_lava"),
                    FluidRegistry.LAVA, FluidRegistry.getFluid("iufluiddizel"),
                    FluidRegistry.getFluid("iufluidbenz"), FluidRegistry.getFluid("ic2biomass"),
                    FluidRegistry.getFluid("biomass")
            ));
            this.fluidSlot = new InvSlotConsumableLiquidByTank(
                    this,
                    "fluidSlot",
                    InvSlot.Access.I,
                    1,
                    InvSlot.InvSide.ANY,
                    InvSlotConsumableLiquid.OpType.Drain,
                    this.fluidTank
            );
        }
        this.name = name;
        this.maxtemperature = 10000;
        this.temperature = 0;

        this.auto = false;
    }

    @Override
    public boolean reveiver() {
        return false;
    }

    @Override
    public boolean requairedTemperature() {
        return false;
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        this.auto = !this.auto;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this));

    }

    protected void onUnloaded() {
        MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this));
        super.onUnloaded();
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.fluidTank.readFromNBT(nbttagcompound.getCompoundTag("fluidTank"));
        this.temperature = nbttagcompound.getShort("temperature");
        this.auto = nbttagcompound.getBoolean("auto");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagCompound fluidTankTag = new NBTTagCompound();
        this.fluidTank.writeToNBT(fluidTankTag);
        nbttagcompound.setTag("fluidTank", fluidTankTag);
        nbttagcompound.setShort("temperature", this.temperature);
        nbttagcompound.setBoolean("auto", this.auto);
        return nbttagcompound;

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.hasFluid) {
            if (this.fluidSlot.processIntoTank(this.fluidTank, this.outputSlot)) {
                this.markDirty();
            }
        }
        setActive(Recipes.mechanism.process(this));
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
    public ContainerHeatMachine getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerHeatMachine(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiHeatMachine(getGuiContainer(entityPlayer), b);
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

    @Override
    public boolean emitsHeatTo(final IHeatAcceptor var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public double getOfferedHeat() {
        return Math.min(4, this.temperature);
    }

    @Override
    public void drawHeat(final double var1) {
        this.temperature -= var1;

    }


    @Override
    public World getWorldTile() {
        return this.getWorld();
    }

    @Override
    public ITemperature getITemperature() {
        return this;
    }

}
