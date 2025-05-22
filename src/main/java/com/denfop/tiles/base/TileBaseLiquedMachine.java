package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotDrainTank;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotTank;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileBaseLiquedMachine extends TileElectricMachine implements IFluidHandler, IUpgradableBlock {

    public final boolean[] drain;
    public final boolean[] fill;
    public final InvSlotUpgrade upgradeSlot;
    public final Fluids fluids;
    public final InvSlotDrainTank[] containerslot;
    public final InvSlotTank[] fluidSlot;
    public final Fluid[] fluid;
    public final int[] old_amount;
    public FluidTank[] fluidTank;
    public int level;

    public TileBaseLiquedMachine(
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

            this.fluidTank[i] = this.fluids.addTank(
                    "fluidTank" + i,
                    8000,
                    i == 0 ? InvSlot.TypeItemSlot.INPUT : InvSlot.TypeItemSlot.OUTPUT,
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
        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        Fluid[] fluid = getFluids(drain, name1);
        this.containerslot = new InvSlotDrainTank[fluid.length];
        for (int i = 0; i < fluid.length; i++) {
            this.containerslot[i] = new InvSlotDrainTank(this, InvSlot.TypeItemSlot.INPUT, 1,
                    InvSlotFluid.TypeFluidSlot.OUTPUT, fluid[i]
            );
        }
        this.fluidSlot = new InvSlotTank[1];
        for (int i = 0; i < fluidSlot.length; i++) {
            this.fluidSlot[i] = new InvSlotTank(this, InvSlot.TypeItemSlot.INPUT, 1,
                    InvSlotFluid.TypeFluidSlot.INPUT, this.fluidTank[0]
            );


        }

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = ModUtils.nbt(stack);

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
                super.addInformation(stack, tooltip);
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
            super.addInformation(stack, tooltip);
            return;
        }
        super.addInformation(stack, tooltip);

    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = ModUtils.nbt(stack);

            int size = nbt.getInteger("size");
            for (int i = 0; i < size; i++) {
                FluidStack fluidStack =
                        FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid" + i));
                if (fluidStack != null) {
                    this.fluidTank[i].fill(fluidStack, true);
                }
            }
            new PacketUpdateFieldTile(this, "fluidTank", this.fluidTank);
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("fluidTank")) {
            try {
                FluidTank[] fluidTanks = (FluidTank[]) DecoderHandler.decode(is);
                for (int i = 0; i < fluidTanks.length; i++) {
                    fluidTank[i].readFromNBT(fluidTanks[i].writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (drop.isItemEqual(this.getPickBlock(
                null,
                null
        )) && (wrench || this.teBlock.getDefaultDrop() == MultiTileBlock.DefaultDrop.Self)) {
            NBTTagCompound nbt = ModUtils.nbt(drop);
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

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.setUpgradestat();
        }

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate;
        needsInvUpdate = false;
        for (FluidTank tank : fluidTank) {
            if (!tank.equals(fluidTank[0])) {
                for (InvSlotDrainTank slot : this.containerslot) {
                    if (tank.getFluidAmount() >= 1000 && !slot.isEmpty()) {
                        slot.processFromTank(tank, this.outputSlot);
                        needsInvUpdate = true;
                    }
                }
            }
        }

        for (final InvSlotTank itemStacks : fluidSlot) {
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
            new PacketUpdateFieldTile(this, "fluidTank", this.fluidTank);
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
        if (!this.getWorld().isRemote && FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
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
                return true;
            }
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
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

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput,
                UpgradableProperty.FluidInput,
                UpgradableProperty.FluidExtract
        );
    }

}
