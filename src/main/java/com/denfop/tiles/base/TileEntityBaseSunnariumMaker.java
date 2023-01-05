package com.denfop.tiles.base;


import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.componets.SEComponent;
import com.denfop.container.ContainerSunnariumMaker;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class TileEntityBaseSunnariumMaker extends TileEntityElectricMachine
        implements IUpgradableBlock {

    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public AudioSource audioSource;
    public SEComponent sunenergy;
    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;
    protected short progress;
    protected double guiProgress;

    public TileEntityBaseSunnariumMaker(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseSunnariumMaker(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.output = null;
        this.sunenergy = this.addComponent(SEComponent
                .asBasicSink(this, 10000, 1));
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    @Override
    public void onNetworkUpdate(String field) {

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

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
            inputSlotA.load();
            this.getOutput();
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.solarium_energy_sink.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate(
                    "iu.machines_work_energy_type_se"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);

        }
        super.addInformation(stack, tooltip, advanced);

    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;


        MachineRecipe output = this.output;

        if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items) && this.outputSlot.canAdd(output.getRecipe().output.items) && this.energy.canUseEnergy(
                energyConsume) && this.sunenergy.canUseEnergy(5)) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                if (this.operationLength > this.defaultOperationLength * 0.1) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
                }
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(energyConsume);
            this.sunenergy.useEnergy(5);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                needsInvUpdate = true;
                this.progress = 0;
                if (this.operationLength > this.defaultOperationLength * 0.1 || (this.getType() != valuesAudio[2 % valuesAudio.length])) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
                }
            }
        } else {
            if (this.progress != 0 && getActive()) {
                if (this.operationLength > this.defaultOperationLength * 0.1 || (this.getType() != valuesAudio[1 % valuesAudio.length])) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 1, true);
                }
            }
            if (output == null) {
                this.progress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
        }
        if ((!this.inputSlotA.isEmpty() || !this.outputSlot.isEmpty()) && this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    public void setOverclockRates() {
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }
    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);

            if (!this.inputSlotA.continue_process(this.output) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                getOutput();
                break;
            }
            if (this.output == null) {
                break;
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlotA.consume();

        this.outputSlot.add(processResult);
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();


        return this.output;
    }

    public abstract String getInventoryName();

    public ContainerBase<? extends TileEntityBaseSunnariumMaker> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSunnariumMaker(entityPlayer, this);
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IC2.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;
        }
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

}
