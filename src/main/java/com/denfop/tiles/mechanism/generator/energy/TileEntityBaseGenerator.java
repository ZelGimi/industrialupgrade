package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class TileEntityBaseGenerator extends TileEntityInventory {

    public final InvSlotCharge chargeSlot;
    public final AdvEnergy energy;

    public int fuel = 0;
    protected double production;
    private int ticksSinceLastActiveUpdate;
    private int activityMeter = 0;

    public TileEntityBaseGenerator(double production, int tier, int maxStorage) {
        this.production = production;
        this.ticksSinceLastActiveUpdate = IUCore.random.nextInt(256);
        this.chargeSlot = new InvSlotCharge(this, 1);
        this.energy =
                this.addComponent(AdvEnergy.asBasicSource(this, maxStorage, tier).addManagedSlot(this.chargeSlot));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {

        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.fuel = nbttagcompound.getInteger("fuel");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("fuel", this.fuel);
        return nbt;
    }


    public double charge(double amount, ItemStack stack, boolean simulate, boolean ignore) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
        if (amount == 0.0) {
            return 0;
        }

        return ElectricItem.manager.charge(stack, amount, 2147483647, ignore, simulate);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needsFuel()) {
            this.gainFuel();
        }
        if (!this.chargeSlot.isEmpty()) {
            if (this.charge(
                    this.energy.getEnergy() > 1D ? this.energy.getEnergy() : 0,
                    chargeSlot.get(0),
                    true, false
            ) != 0) {
                this.energy.useEnergy(this.charge(
                        this.energy.getEnergy() > 1D ? this.energy.getEnergy() : 0,
                        chargeSlot.get(0),
                        false, false
                ));
            }
        }
        boolean newActive = this.gainEnergy();

        if (!this.delayActiveUpdate()) {
            this.setActive(newActive);
        } else {
            if (this.ticksSinceLastActiveUpdate % 256 == 0) {
                this.setActive(this.activityMeter > 0);
                this.activityMeter = 0;
            }

            if (newActive) {
                ++this.activityMeter;
            } else {
                --this.activityMeter;
            }

            ++this.ticksSinceLastActiveUpdate;
        }

    }

    public boolean gainEnergy() {
        if (this.isConverting()) {
            this.energy.addEnergy(this.production);
            --this.fuel;
            return true;
        } else {
            return false;
        }
    }

    public boolean isConverting() {
        return !this.needsFuel() && this.energy.getFreeEnergy() >= this.production;
    }

    public boolean needsFuel() {
        return this.fuel <= 0 && this.energy.getFreeEnergy() >= this.production;
    }

    public abstract boolean gainFuel();

    protected boolean delayActiveUpdate() {
        return false;
    }


    public ContainerBase<? extends TileEntityBaseGenerator> getGuiContainer(EntityPlayer player) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return null;
    }


}
