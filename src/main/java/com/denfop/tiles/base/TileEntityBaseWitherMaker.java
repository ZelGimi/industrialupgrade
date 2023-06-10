package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.audio.AudioSource;
import com.denfop.componets.Action;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.TypeAction;
import com.denfop.componets.TypeLoad;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerBaseWitherMaker;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Random;

public abstract class TileEntityBaseWitherMaker extends TileEntityElectricMachine
        implements IUpgradableBlock, IUpdateTick {


    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final ComponentUpgrade componentUpgrades;
    public AudioSource audioSource;
    public MachineRecipe output;
    public InvSlotRecipes inputSlotA;

    public TileEntityBaseWitherMaker(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileEntityBaseWitherMaker(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, outputSlots);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.output = null;
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setAction(new Action(this, 20, TypeAction.AUDIO, TypeLoad.PROGRESS, Boolean.FALSE, 3));
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT,TypeUpgrade.STACK));

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getDefaultEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getDefaultOperationLength());
        }
        super.addInformation(stack, tooltip, advanced);

    }


    public void onLoaded() {
        super.onLoaded();
        inputSlotA.load();
        if (IC2.platform.isSimulating()) {
            this.output = this.getOutput();
        }

    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            if (!this.inputSlotA.isEmpty()) {
                for (int i = 0; i < 3; i++) {


                    if (!this.inputSlotA.get(i).isEmpty() && this.inputSlotA.get(i).getCount() > 1 && this.inputSlotA
                            .get(i)
                            .getItem() == Items.SKULL && this.inputSlotA.get(i).getItemDamage() == 1) {
                        for (int j = 0; j < 3; j++) {
                            if (this.inputSlotA.get(j).isEmpty() && !this.inputSlotA.get(i).isEmpty() && this.inputSlotA
                                    .get(i)
                                    .getCount() > 1) {
                                this.inputSlotA.consume(i, 1);
                                ItemStack stack = new ItemStack(Items.SKULL, 1, 1);
                                this.inputSlotA.put(j, stack);
                            }
                        }

                    }
                }
                for (int i = 3; i < 7; i++) {
                    for (int j = 3; j < 7; j++) {
                        if (i != j) {
                            if (!this.inputSlotA.get(i).isEmpty() && this.inputSlotA.get(i).getCount() > 1 && (this.inputSlotA
                                    .get(i)
                                    .getItem() == Item.getItemFromBlock(Blocks.SOUL_SAND))) {
                                if (this.inputSlotA.get(j).isEmpty()) {
                                    this.inputSlotA.consume(i, 1);

                                    ItemStack stack = new ItemStack(Blocks.SOUL_SAND, 1);
                                    this.inputSlotA.put(j, stack);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();


        return this.output;
    }


    public ContainerBaseWitherMaker getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBaseWitherMaker(
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
                    this.audioSource.stop();
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
                    IUCore.audioManager.playOnce(this, getInterruptSoundFile1());

                }
                break;
            case 3:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    final Random rand = this.getWorld().rand;
                    if (this.sound) {
                        IUCore.audioManager.playOnce(this, rand.nextInt(2) == 0 ? getInterruptSoundFile2() :
                                getInterruptSoundFile3());
                    }

                }
                break;
        }
    }

    public String getInterruptSoundFile1() {
        return "Machines/WitherDeath1.ogg";
    }

    public String getInterruptSoundFile2() {
        return "Machines/WitherHurt3.ogg";
    }

    public String getInterruptSoundFile3() {
        return "Machines/WitherIdle1.ogg";
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
