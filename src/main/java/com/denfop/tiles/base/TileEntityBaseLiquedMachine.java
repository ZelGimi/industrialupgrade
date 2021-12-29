package com.denfop.tiles.base;

import com.denfop.invslot.InvSlotConsumableLiquidByListRemake;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileEntityBaseLiquedMachine extends TileEntityElectricMachine implements IFluidHandler, IUpgradableBlock {

    public final FluidTank[] fluidTank;
    public final boolean[] drain;
    public final boolean[] fill;
    public final InvSlotUpgrade upgradeSlot;
    public final Fluids fluids;
    public final InvSlotConsumableLiquidByListRemake[] containerslot;
    public final InvSlotConsumableLiquidByTank[] fluidSlot;
    public final Fluid[] fluid;

    public TileEntityBaseLiquedMachine(
            final String name, final double MaxEnergy, final int tier, final int count,
            final int count_tank, boolean[] drain, boolean[] fill, Fluid[] name1
    ) {
        super(name, MaxEnergy, tier, count);
        this.fluidTank = new FluidTank[count_tank];
        this.drain = drain;
        this.fill = fill;
        this.fluids = this.addComponent(new Fluids(this));
        this.fluid = name1;
        for (int i = 0; i < fluidTank.length; i++) {
            this.fluidTank[i] = this.fluids.addTank("fluidTank" + i, 8000,
                    Fluids.fluidPredicate(name1[i])
            );
        }
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        Fluid[] fluid = getFluids(drain, name1);
        this.containerslot = new InvSlotConsumableLiquidByListRemake[fluid.length];
        for (int i = 0; i < fluid.length; i++) {
            this.containerslot[i] = new InvSlotConsumableLiquidByListRemake(this, "container" + i, InvSlot.Access.I, 1,
                    InvSlot.InvSide.TOP,
                    InvSlotConsumableLiquid.OpType.Fill, fluid[i]
            );
        }
        fluid = getFluidsFill(fill, name1);
        this.fluidSlot = new InvSlotConsumableLiquidByTank[fluid.length];
        for (int i = 0; i < fluidSlot.length; i++) {
            this.fluidSlot[i] = new InvSlotConsumableLiquidByTank(this, "fluidSlot" + i, InvSlot.Access.I, 1, InvSlot.InvSide.ANY,
                    InvSlotConsumableLiquid.OpType.Drain, getTank(this.fluidTank, fluid[i], name1)
            );


        }

    }

    public FluidTank getTank(FluidTank[] tanks, Fluid fluid, Fluid[] fluidlist) {
        for (int i = 0; i < fluidlist.length; i++) {
            if (fluidlist[i].equals(fluid)) {
                return tanks[i];
            }
        }

        return null;
    }

    public FluidTank getFluidTank(int num) {
        return this.fluidTank[num];
    }

    public int getTankAmount(int num) {
        return this.fluidTank[num].getFluidAmount();
    }

    public FluidStack getFluidStackfromTank1(int num) {
        return this.fluidTank[num].getFluid();
    }

    public double gaugeLiquidScaled(double i, int num) {
        return this.fluidTank[num].getFluidAmount() <= 0 ? 0 :
                this.fluidTank[num].getFluidAmount() * i / this.fluidTank[num].getCapacity();
    }

    public Fluid[] getFluids(boolean[] drain, Fluid[] name) {
        List<Fluid> fluidlist = new ArrayList<>();

        for (final boolean b : drain) {
            if (b) {
                fluidlist.add(name[0]);
            }

        }
        Fluid[] fluid = new Fluid[fluidlist.size()];
        for (int i = 0; i < fluidlist.size(); i++) {
            fluid[i] = fluidlist.get(i);
        }
        return fluid;
    }

    public Fluid[] getFluidsFill(boolean[] fill, Fluid[] name) {
        List<Fluid> fluidlist = new ArrayList<>();

        for (final boolean b : fill) {
            if (!b) {
                fluidlist.add(name[0]);
            }

        }
        Fluid[] fluid = new Fluid[fluidlist.size()];
        for (int i = 0; i < fluidlist.size(); i++) {
            fluid[i] = fluidlist.get(i);
        }
        return fluid;
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.setUpgradestat();
        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate;
        needsInvUpdate = this.upgradeSlot.tickNoMark();
        for (FluidTank tank : fluidTank) {
            if (!tank.equals(fluidTank[0])) {
                for (InvSlotConsumableLiquidByListRemake slot : this.containerslot) {
                    needsInvUpdate |= slot.processFromTank(tank, this.outputSlot);
                }
            }
        }
        if (needsInvUpdate) {
            this.markDirty();
        }
        for (final InvSlotConsumableLiquidByTank itemStacks : fluidSlot) {
            for (final FluidTank tank : fluidTank) {
                if (itemStacks.processIntoTank(tank, this.outputSlot)) {
                    this.markDirty();
                }
            }
        }
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setUpgradestat();
        }

    }

    public void setUpgradestat() {
        this.upgradeSlot.onChanged();
        this.energy.setSinkTier(1 + this.upgradeSlot.extraTier);
    }


    @Override
    public IFluidTankProperties[] getTankProperties() {
        IFluidTankProperties[] tankProperties = new IFluidTankProperties[fluidTank.length];

        for (int i = 0; i < fluidTank.length; i++) {
            tankProperties[i] = fluidTank[i].getTankProperties()[0];
        }

        return tankProperties;
    }

    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
        for (int i = 0; i < fluidTank.length; i++) {
            if (resource != null && resource.getFluid().equals(fluid[i]) && !this.fill[i] && fluidTank[i].getFluidAmount() >= 0) {
                return fluidTank[i].fill(resource, doFill);
            }
        }
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        for (int i = 0; i < fluidTank.length; i++) {
            if (resource != null && resource.isFluidEqual(fluidTank[i].getFluid()) && this.drain[i] && fluidTank[i].getFluidAmount() > 0) {
                return fluidTank[i].drain(resource.amount, doDrain);
            }
        }
        return null;

    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        for (int i = 0; i < fluidTank.length; i++) {
            if (this.drain[i] && fluidTank[i].getFluidAmount() > 0) {
                return fluidTank[i].drain(maxDrain, doDrain);
            }
        }

        return null;
    }

    @Override
    public double getEnergy() {
        return this.energy.getEnergy();
    }

    @Override
    public boolean useEnergy(final double v) {
        return this.energy.useEnergy(v);
    }

    @Override

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing
        );
    }

}
