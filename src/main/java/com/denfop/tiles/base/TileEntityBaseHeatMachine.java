package com.denfop.tiles.base;

import com.denfop.componets.AdvEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerHeatMachine;
import com.denfop.gui.GuiHeatMachine;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import ic2.api.network.INetworkClientTileEntityEventListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

public class TileEntityBaseHeatMachine extends TileEntityElectricMachine implements INetworkClientTileEntityEventListener {


    public final boolean hasFluid;
    public final HeatComponent heat;
    public short maxtemperature;
    public boolean auto;
    public FluidTank fluidTank;
    public Fluids fluids = null;
    public InvSlotConsumableLiquid fluidSlot;
    public int coef = 1;
    public boolean work = true;

    public TileEntityBaseHeatMachine(boolean hasFluid) {
        super(hasFluid ? 0D : 10000D, 14, 1);
        this.hasFluid = hasFluid;
        this.fluidTank = new FluidTank(12000);
        if (this.hasFluid) {
            this.energy = this.addComponent(AdvEnergy.asBasicSink(this, 0, 14));
        }
        if (this.hasFluid) {
            this.fluids = this.addComponent(new Fluids(this));
            this.fluidTank = this.fluids.addTank("fluidTank", 12000, InvSlot.Access.I, InvSlot.InvSide.ANY, Fluids.fluidPredicate(
                    FluidRegistry.getFluid("cryotheum"), FluidRegistry.getFluid("petrotheum"),
                    FluidRegistry.getFluid("redstone"), FluidRegistry.getFluid("ic2pahoehoe_lava"),
                    FluidRegistry.LAVA, FluidRegistry.getFluid("iufluiddizel"),
                    FluidRegistry.getFluid("iufluidbenz"),
                    FluidRegistry.getFluid("biocrude"),
                    FluidRegistry.getFluid("biofuel"),
                    FluidRegistry.getFluid("ic2biogas"),
                    FluidRegistry.getFluid("refined_biofuel"),
                    FluidRegistry.getFluid("ic2biomass"),
                    FluidRegistry.getFluid("biomass")
            ));
            this.fluidSlot = new InvSlotConsumableLiquidByList(
                    this,
                    "fluidSlot",
                    InvSlot.Access.I,
                    1,
                    InvSlot.InvSide.ANY,
                    InvSlotConsumableLiquid.OpType.Drain,
                    FluidRegistry.getFluid("cryotheum"),
                    FluidRegistry.getFluid("petrotheum"),
                    FluidRegistry.getFluid("redstone"),
                    FluidRegistry.getFluid("ic2pahoehoe_lava"),
                    FluidRegistry.LAVA,
                    FluidRegistry.getFluid("iufluiddizel"),
                    FluidRegistry.getFluid("iufluidbenz"),
                    FluidRegistry.getFluid("ic2biomass"),
                    FluidRegistry.getFluid("biocrude"),
                    FluidRegistry.getFluid("biofuel"),
                    FluidRegistry.getFluid("ic2biogas"),
                    FluidRegistry.getFluid("refined_biofuel"),
                    FluidRegistry.getFluid("biomass")
            );
        }

        this.maxtemperature = 1000;
        this.heat = this.addComponent(HeatComponent
                .asBasicSource(this, 1000));
        this.auto = false;

    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.coef = (int) Math.max(Math.ceil(this.heat.storage / 2000), 1);
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        if (i == 0) {
            this.maxtemperature = (short) (this.maxtemperature + 1000);
            if (this.maxtemperature > 10000) {
                this.maxtemperature = 10000;
            }
            this.heat.setCapacity(this.maxtemperature);

        }
        if (i == 1) {
            this.maxtemperature = (short) (this.maxtemperature - 1000);
            if (this.maxtemperature < 1000) {
                this.maxtemperature = 1000;
            }
            this.heat.setCapacity(this.maxtemperature);
        }
        if (i == 2) {
            this.work = !this.work;
        }
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.fluidTank.readFromNBT(nbttagcompound.getCompoundTag("fluidTank"));
        this.maxtemperature = nbttagcompound.getShort("maxtemperature");
        this.auto = nbttagcompound.getBoolean("auto");
        this.work = nbttagcompound.getBoolean("work");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagCompound fluidTankTag = new NBTTagCompound();
        this.fluidTank.writeToNBT(fluidTankTag);
        nbttagcompound.setTag("fluidTank", fluidTankTag);
        nbttagcompound.setShort("maxtemperature", this.maxtemperature);
        nbttagcompound.setBoolean("auto", this.auto);
        nbttagcompound.setBoolean("work", this.work);
        return nbttagcompound;

    }

    public boolean process() {
        if (!this.hasFluid) {
            if ((this.energy != null && this.energy.getEnergy() < 50)) {
                return false;
            }
        } else {
            if (this.fluidTank.getFluid() == null || this.fluidTank.getFluidAmount() == 0) {
                return false;
            }
        }
        short temp = (short) this.heat.getEnergy();
        if (temp >= this.maxtemperature) {
            return false;
        }
        if (this.heat.allow || work) {
            if (this.hasFluid) {
                if (this.getFluidTank().getFluidAmount() >= 1) {
                    this.heat.addEnergy(5);
                    this.getFluidTank().drain(this.coef, true);
                    return true;
                }
            } else {
                if (this.energy.getEnergy() >= 30 * this.coef) {
                    this.heat.addEnergy(5);
                    this.energy.useEnergy(30 * this.coef);
                    return true;
                }
            }
        }
        return false;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.hasFluid) {
            MutableObject<ItemStack> output = new MutableObject<>();
            if (this.fluidSlot.transferToTank(
                    this.fluidTank,
                    output,
                    true
            ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                this.fluidSlot.transferToTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }

        }
        boolean active = process();
        if (active != this.getActive()) {
            setActive(active);
        }
        if (this.world.provider.getWorldTime() % 60 == 0) {
            if (this.heat.getEnergy() > 0) {
                this.heat.useEnergy(1);
            }
        }
    }


    public FluidTank getFluidTank() {
        return this.fluidTank;
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


}
