package com.denfop.tiles.base;

import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerAirCollector;
import com.denfop.gui.GuiAirCollector;
import com.denfop.invslot.InvSlotConsumableLiquidByListRemake;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityAirCollector extends TileEntityElectricMachine implements IUpgradableBlock {

    public final FluidTank[] fluidTank;
    public final Fluids fluids;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotConsumableLiquidByListRemake[] containerslot;

    public TileEntityAirCollector() {
        super(5000, 14, 3);
        this.fluidTank = new FluidTank[3];
        this.fluids = this.addComponent(new Fluids(this));
        Fluid[] name1 = new Fluid[]{FluidName.fluidazot.getInstance(), FluidName.fluidoxy.getInstance(),
                FluidName.fluidco2.getInstance()};
        for (int i = 0; i < fluidTank.length; i++) {

            this.fluidTank[i] = this.fluids.addTank("fluidTank" + i, 8000, InvSlot.Access.O,
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

    }

    public FluidTank getFluidTank(int num) {
        return this.fluidTank[num];
    }

    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerAirCollector(entityPlayer, this);
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
        if (this.energy.getEnergy() > 5) {
            if (this.world.provider.getWorldTime() % 20 == 0) {
                if (fluidTank[0].getFluidAmount() + 1 <= fluidTank[0].getCapacity()) {
                    fluidTank[0].fill(new FluidStack(FluidName.fluidazot.getInstance(), 1), true);
                    needsInvUpdate = true;
                }
                if (this.world.provider.getWorldTime() % 60 == 0) {
                    if (fluidTank[1].getFluidAmount() + 1 <= fluidTank[1].getCapacity()) {
                        fluidTank[1].fill(new FluidStack(FluidName.fluidoxy.getInstance(), 1), true);
                        needsInvUpdate = true;
                    }
                }
                if (this.world.provider.getWorldTime() % 120 == 0) {
                    if (fluidTank[2].getFluidAmount() + 1 <= fluidTank[2].getCapacity()) {
                        fluidTank[2].fill(new FluidStack(FluidName.fluidco2.getInstance(), 1), true);
                        needsInvUpdate = true;
                    }
                }
                this.energy.useEnergy(5);
            }
        }
        if (needsInvUpdate) {
            this.markDirty();
        }
    }

    public void setUpgradestat() {
        this.upgradeSlot.onChanged();
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

}
