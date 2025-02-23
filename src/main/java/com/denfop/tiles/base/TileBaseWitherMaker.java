package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.audio.EnumSound;
import com.denfop.componets.Action;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.TypeAction;
import com.denfop.componets.TypeLoad;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerBaseWitherMaker;
import com.denfop.invslot.InvSlotUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class TileBaseWitherMaker extends TileElectricMachine
        implements IUpgradableBlock, IUpdateTick {


    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final ComponentUpgrade componentUpgrades;
    public MachineRecipe output;
    public InvSlotRecipes inputSlotA;

    public TileBaseWitherMaker(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileBaseWitherMaker(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, outputSlots);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.output = null;
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setAction(new Action(this, 20, TypeAction.AUDIO, TypeLoad.PROGRESS, 3));
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));

    }


    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
        }
        super.addInformation(stack, tooltip);

    }


    public void onLoaded() {
        super.onLoaded();
        inputSlotA.load();
        if (IUCore.proxy.isSimulating()) {
            this.output = this.getOutput();
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

    @Override
    public SoundEvent getSound() {
        return EnumSound.WitherSpawn1.getSoundEvent();
    }


}
