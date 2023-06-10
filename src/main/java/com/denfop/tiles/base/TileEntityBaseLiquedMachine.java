package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.invslot.*;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.ref.TeBlock;
import ic2.core.util.LiquidUtil;
import ic2.core.util.StackUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public final int[] old_amount;
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
        this.old_amount = new int[count_tank];
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
                    InvSlot.InvSide.ANY,
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
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);

            int size = nbt.getInteger("size");
            List<FluidStack> fluidStackList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                FluidStack fluidStack =
                        FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid" + i));
                if (fluidStack != null) {
                    fluidStackList.add(fluidStack);
                }
            }
            if (fluidStackList.isEmpty()) {
                super.addInformation(stack, tooltip, advanced);
                return;
            }
            if (fluidStackList.size() == 1) {
                tooltip.add(Localization.translate("iu.fluid.info") + fluidStackList.get(0).getLocalizedName());
                tooltip.add(Localization.translate("iu.fluid.info1") + fluidStackList.get(0).amount / 1000 + " B");
            } else {
                tooltip.add(Localization.translate("iu.fluid.info2"));
                for (FluidStack fluidStack : fluidStackList) {
                    tooltip.add(fluidStack.getLocalizedName() + " " + fluidStack.amount / 1000 + " B");
                }

            }
            super.addInformation(stack, tooltip, advanced);
            return;
        }
        super.addInformation(stack, tooltip, advanced);

    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);

            int size = nbt.getInteger("size");
            for (int i = 0; i < size; i++) {
                FluidStack fluidStack =
                        FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid" + i));
                if (fluidStack != null) {
                    this.fluidTank[i].fill(fluidStack, true);
                }
            }
            IUCore.network.get(true).updateTileEntityField(this, "fluidTank");
        }
    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
            nbt.setInteger("size", this.fluidTank.length);
            for (int i = 0; i < this.fluidTank.length; i++) {
                if (this.fluidTank[i].getFluidAmount() > 0) {
                    nbt.setTag("fluid" + i, this.fluidTank[i].getFluid().writeToNBT(new NBTTagCompound()));
                }
            }
        }
        return drop;
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
        needsInvUpdate = false;
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
                if (tank.equals(fluidTank[0]) && tank.getFluidAmount() + 1000 <= tank.getCapacity() && !itemStacks
                        .get()
                        .isEmpty()) {
                    if (itemStacks.processIntoTank(tank, this.outputSlot)) {
                        needsInvUpdate = true;

                    }
                }
            }
        }
        if (needsInvUpdate) {
            IUCore.network.get(true).updateTileEntityField(this, "fluidTank");
        }

        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }

    }



    public void setUpgradestat() {
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
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && LiquidUtil.isFluidContainer(player.getHeldItem(hand))) {

            return FluidUtil.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        }
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

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
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
        return EnumSet.of(
                UpgradableProperty.RedstoneSensitive,
                UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing,
                UpgradableProperty.FluidProducing,
                UpgradableProperty.FluidConsuming
        );
    }

}
