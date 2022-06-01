package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import com.denfop.invslot.InvSlotConsumableLiquidByListRemake;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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
    public int level;

    public TileEntityBaseLiquedMachine(
            final double MaxEnergy, final int tier, final int count,
            final int count_tank, boolean[] drain, boolean[] fill, Fluid[] name1
    ) {
        super(MaxEnergy, tier, count);
        this.fluidTank = new FluidTank[count_tank];
        this.drain = drain;
        this.fill = fill;
        this.fluids = this.addComponent(new Fluids(this));
        this.fluid = name1;
        this.tier = tier;
        this.level = 0;
        for (int i = 0; i < fluidTank.length; i++) {

            this.fluidTank[i] = this.fluids.addTank("fluidTank" + i, 8000, i == 0 ? InvSlot.Access.I : InvSlot.Access.O,
                    InvSlot.InvSide.ANY,
                    i == 0 && name1[i].getName().equals(FluidName.fluidneft.getInstance().getName())
                            ? Fluids.fluidPredicate(name1[i],
                            FluidRegistry.getFluid("oil_heavy"), FluidRegistry.getFluid("oil_heavy_heat_1"),
                            FluidRegistry.getFluid("oil_heavy_heat_2"), FluidRegistry.getFluid("oil_heat_2"),
                            FluidRegistry.getFluid("oil_heat_1"), FluidRegistry.getFluid("oil"),
                            FluidRegistry.getFluid("fluid_cride_oil"), FluidRegistry.getFluid("refined_oil"),
                            FluidRegistry.getFluid("crude_oil")
                    )
                            :
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
        this.fluidSlot = new InvSlotConsumableLiquidByTank[1];
        for (int i = 0; i < fluidSlot.length; i++) {
            this.fluidSlot[i] = new InvSlotConsumableLiquidByTank(this, "fluidSlot" + i, InvSlot.Access.I, 1, InvSlot.InvSide.ANY,
                    InvSlotConsumableLiquid.OpType.Drain, this.fluidTank[0]
            );


        }

    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);
        return nbttagcompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.level = nbttagcompound.getInteger("level");
    }

    public FluidTank getFluidTank(int num) {
        return this.fluidTank[num];
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
                    if (tank.getFluidAmount() >= 1000 && !slot.isEmpty()) {
                        slot.processFromTank(tank, this.outputSlot);
                        needsInvUpdate = true;
                    }
                }
            }
        }

        for (final InvSlotConsumableLiquidByTank itemStacks : fluidSlot) {
            for (final FluidTank tank : fluidTank) {
                if (tank.equals(fluidTank[0])) {
                    if (itemStacks.processIntoTank(tank, this.outputSlot)) {
                        needsInvUpdate = true;

                    }
                }
            }
        }
        if (needsInvUpdate) {
            this.markDirty();
            IC2.network.get(true).updateTileEntityField(this, "fluidTank");

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
        this.energy.setSinkTier(this.tier + this.upgradeSlot.extraTier);
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

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return false;
            }
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        for (int i = 1; i < fluidTank.length; i++) {
            if (resource != null && resource.isFluidEqual(fluidTank[i].getFluid()) && this.drain[i] && fluidTank[i].getFluidAmount() > 0) {
                return fluidTank[i].drain(resource.amount, doDrain);
            }
        }
        return null;

    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        for (int i = 1; i < fluidTank.length; i++) {
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
