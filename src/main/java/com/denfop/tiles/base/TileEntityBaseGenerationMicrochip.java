package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.audio.AudioSource;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerBaseGenerationChipMachine;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class TileEntityBaseGenerationMicrochip extends TileEntityElectricMachine
        implements IUpgradableBlock {


    public final InvSlotUpgrade upgradeSlot;
    public final HeatComponent heat;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public AudioSource audioSource;


    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;

    public TileEntityBaseGenerationMicrochip(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseGenerationMicrochip(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, outputSlots);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.output = null;
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 5000));
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    public void onLoaded() {
        super.onLoaded();
        inputSlotA.load();
        this.getOutput();
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
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
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getDefaultEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getDefaultOperationLength());

        }
        super.addInformation(stack, tooltip, advanced);
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
