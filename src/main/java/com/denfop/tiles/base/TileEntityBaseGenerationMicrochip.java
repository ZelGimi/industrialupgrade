package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.audio.AudioSource;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerBaseGenerationChipMachine;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class TileEntityBaseGenerationMicrochip extends TileEntityElectricMachine
        implements IUpgradableBlock {


    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    public final HeatComponent heat;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public AudioSource audioSource;


    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;
    protected short progress;
    protected double guiProgress;

    public TileEntityBaseGenerationMicrochip(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseGenerationMicrochip(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, outputSlots);
        this.progress = 0;
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.output = null;
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 5000));
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

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
        inputSlotA.load();
        this.getOutput();
        if (this.output == null) {
            (this).heat.need = false;
        }
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.heatmachine.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);

        }
        super.addInformation(stack, tooltip, advanced);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        MachineRecipe output = this.output;
        if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items) && this.energy.canUseEnergy(energyConsume) && output.getRecipe().output.metadata != null) {

            if (output.getRecipe().output.metadata.getShort("temperature") == 0 || output.getRecipe().output.metadata.getInteger(
                    "temperature") > this.heat.getEnergy()) {
                if (!(this).heat.need) {
                    (this).heat.need = true;
                }
                return;

            } else if ((this).heat.need) {
                (this).heat.need = false;
            }
            (this).heat.storage--;

            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                if (this.operationLength > this.defaultOperationLength * 0.1) {
                    IUCore.network.get(true).initiateTileEntityEvent(this, 0, true);
                }
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(energyConsume);
            double k = this.progress;
            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
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
            if (this.heat.getEnergy() > 1) {
                this.heat.useEnergy(1);
            }

        }

        if (output == null) {
            this.heat.useEnergy(1);
        }
        if (this.upgradeSlot.tickNoMark()) {
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
                this.defaultEnergyStorage));
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

    public ContainerBaseGenerationChipMachine getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBaseGenerationChipMachine(
                entityPlayer, this);
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
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
                        IUCore.audioManager.playOnce(this, getInterruptSoundFile());
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
