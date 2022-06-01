package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerGenStone;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public abstract class TileEntityBaseGenStone extends TileEntityElectricMachine implements IHasGui,
        INetworkTileEntityEventListener, IUpgradableBlock, IUpdateTick {

    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    public int energyConsume;

    public int operationLength;

    public int operationsPerTick;
    public AudioSource audioSource;
    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;
    protected short progress;
    protected double guiProgress;

    public TileEntityBaseGenStone(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseGenStone(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super((double) energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.upgradeSlot.setStackSizeLimit(1);
        this.output = null;
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        return nbttagcompound;
    }

    public double getProgress() {
        return this.guiProgress;
    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
            inputSlotA.load();
            this.getOutput();
        }


    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;


        MachineRecipe output = this.output;
        if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items) && this.energy.getEnergy() >= this.energyConsume) {
            setActive(true);
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(this.energyConsume);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                needsInvUpdate = true;
                this.progress = 0;
            }
        } else {
            if (output == null) {
                this.progress = 0;
            }
            setActive(false);
        }
        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof IUpgradeItem) {
                if (((IUpgradeItem) stack.getItem()).onTick(stack, this)) {
                    needsInvUpdate = true;
                }
            }
        }

        if (needsInvUpdate) {
            super.markDirty();
        }

    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage,
                this.defaultOperationLength,
                this.defaultEnergyConsume
        ));
        dischargeSlot.setTier(tier);
    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(output, processResult);
            if (this.output == null) {
                break;
            }
        }
    }

    public void operateOnce(MachineRecipe output, List<ItemStack> processResult) {
        this.outputSlot.add(processResult);
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public abstract String getInventoryName();

    public ContainerBase<? extends TileEntityBaseGenStone> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGenStone(entityPlayer, this);
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {

        return this.energy.useEnergy(amount);
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

}
