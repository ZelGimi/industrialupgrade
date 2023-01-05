package com.denfop.tiles.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerAirCollector;
import com.denfop.gui.GuiAirCollector;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByListRemake;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityAirCollector extends TileEntityElectricMachine implements IUpgradableBlock, IManufacturerBlock {

    public final FluidTank[] fluidTank;
    public final Fluids fluids;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotConsumableLiquidByListRemake[] containerslot;
    private int level;

    public TileEntityAirCollector() {
        super(5000, 1, 3);
        this.fluidTank = new FluidTank[3];
        this.fluids = this.addComponent(new Fluids(this));
        Fluid[] name1 = new Fluid[]{FluidName.fluidazot.getInstance(), FluidName.fluidoxy.getInstance(),
                FluidName.fluidco2.getInstance()};
        for (int i = 0; i < fluidTank.length; i++) {

            this.fluidTank[i] = this.fluids.addTank("fluidTank" + i, 10000, InvSlot.Access.O,
                    InvSlot.InvSide.ANY,
                    Fluids.fluidPredicate(name1[i])
            );

        }
        this.containerslot = new InvSlotConsumableLiquidByListRemake[name1.length];
        for (int i = 0; i < name1.length; i++) {
            this.containerslot[i] = new InvSlotConsumableLiquidByListRemake(this, "container" + i, InvSlot.Access.I, 1,
                    InvSlot.InvSide.TOP,
                    InvSlotConsumableLiquid.OpType.Fill, name1[i]
            );
        }
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.level = 0;

    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);
    }

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }

    public FluidTank getFluidTank(int num) {
        return this.fluidTank[num];
    }

    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerAirCollector(entityPlayer, this);
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

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiAirCollector(new ContainerAirCollector(entityPlayer, this));
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank(0).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(0).getFluidAmount() * i / this.getFluidTank(0).getCapacity();
    }

    public double gaugeLiquidScaled(double i) {
        return this.getFluidTank(0).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(0).getFluidAmount() * i / this.getFluidTank(0).getCapacity();
    }

    public String getStartSoundFile() {
        return "Machines/air_collector.ogg";
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.setUpgradestat();
        }

    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setUpgradestat();
        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        for (FluidTank tank : fluidTank) {
            if (!tank.equals(fluidTank[0])) {
                for (InvSlotConsumableLiquidByListRemake slot : this.containerslot) {
                    if (tank.getFluidAmount() >= 1000 && !slot.isEmpty()) {
                        slot.processFromTank(tank, this.outputSlot);
                    }
                }
            }
        }
        boolean work = false;
        if (this.energy.getEnergy() > 5 + 5 * this.level) {
            if (this.world.provider.getWorldTime() % 400 == 0) {
                this.initiate(2);
            }
            work = true;
            if (this.world.provider.getWorldTime() % 20 == 0) {
                if (fluidTank[0].getFluidAmount() + 1 <= fluidTank[0].getCapacity()) {
                    fluidTank[0].fill(
                            new FluidStack(FluidName.fluidazot.getInstance(), Math.min(
                                    1 + this.level,
                                    fluidTank[0].getCapacity() - fluidTank[0].getFluidAmount()
                            )),
                            true
                    );

                }
                if (this.world.provider.getWorldTime() % 60 == 0) {
                    if (fluidTank[1].getFluidAmount() + 1 <= fluidTank[1].getCapacity()) {
                        fluidTank[1].fill(new FluidStack(FluidName.fluidoxy.getInstance(), Math.min(
                                1 + this.level,
                                fluidTank[1].getCapacity() - fluidTank[1].getFluidAmount()
                        )), true);
                        work = true;
                    }
                }
                if (this.world.provider.getWorldTime() % 120 == 0) {
                    if (fluidTank[2].getFluidAmount() + 1 <= fluidTank[2].getCapacity()) {
                        fluidTank[2].fill(new FluidStack(FluidName.fluidco2.getInstance(), Math.min(
                                1 + this.level,
                                fluidTank[2].getCapacity() - fluidTank[2].getFluidAmount()
                        )), true);
                        work = true;
                    }
                }
                this.energy.useEnergy(5 + 5 * this.level);
            }
        }
        if (!work) {
            this.initiate(2);
        } else {
            this.initiate(0);
        }
        if (this.upgradeSlot.tickNoMark()) {
            this.setUpgradestat();
        }
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(this.tier + this.upgradeSlot.extraTier);
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

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

}
