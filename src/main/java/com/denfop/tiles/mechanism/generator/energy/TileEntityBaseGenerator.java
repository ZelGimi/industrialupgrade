//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.componets.AdvEnergy;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.item.ElectricItem;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.invslot.InvSlotCharge;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class TileEntityBaseGenerator extends TileEntityInventory implements IHasGui {

    public final InvSlotCharge chargeSlot;
    public final AdvEnergy energy;

    public int fuel = 0;
    public AudioSource audioSource;
    protected double production;
    private int ticksSinceLastActiveUpdate;
    private int activityMeter = 0;

    public TileEntityBaseGenerator(double production, int tier, int maxStorage) {
        this.production = production;
        this.ticksSinceLastActiveUpdate = IC2.random.nextInt(256);
        this.chargeSlot = new InvSlotCharge(this, 1);
        this.energy =
                this.addComponent(AdvEnergy.asBasicSource(this, maxStorage, tier).addManagedSlot(this.chargeSlot));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (this.hasComponent(AdvEnergy.class)) {
            AdvEnergy energy = this.getComponent(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
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

    protected void onUnloaded() {
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }

        super.onUnloaded();
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

    protected void updateEntityServer() {
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

    public String getOperationSoundFile() {
        return null;
    }

    protected boolean delayActiveUpdate() {
        return false;
    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public ContainerBase<? extends TileEntityBaseGenerator> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return DynamicGui.create(this, player, GuiParser.parse(this.teBlock));
    }

    public void onNetworkUpdate(String field) {
        if (field.equals("active")) {
            if (this.audioSource == null && this.getOperationSoundFile() != null) {
                this.audioSource = IC2.audioManager.createSource(
                        this,
                        PositionSpec.Center,
                        this.getOperationSoundFile(),
                        true,
                        false,
                        IC2.audioManager.getDefaultVolume()
                );
            }

            if (this.getActive()) {
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
            } else if (this.audioSource != null) {
                this.audioSource.stop();
            }
        }

        super.onNetworkUpdate(field);
    }

}
