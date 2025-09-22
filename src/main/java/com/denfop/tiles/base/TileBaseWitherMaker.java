package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
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
import com.denfop.invslot.InventoryUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class TileBaseWitherMaker extends TileElectricMachine
        implements IUpgradableBlock, IUpdateTick {


    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final ComponentUpgrade componentUpgrades;
    public MachineRecipe output;
    public InventoryRecipes inputSlotA;
    private ItemStack WITHER_SKELETON_SKULL;
    private ItemStack  SOUL_SAND;
    public TileBaseWitherMaker(int energyPerTick, int length, int outputSlots) {
        this(energyPerTick, length, outputSlots, 1);
    }

    public TileBaseWitherMaker(int energyPerTick, int length, int outputSlots, int aDefaultTier) {
        super(energyPerTick * length, 1, outputSlots);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
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
            WITHER_SKELETON_SKULL = new ItemStack(Items.SKULL,1,1);
            SOUL_SAND = new ItemStack(Blocks.SOUL_SAND);
        }

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            if (!this.inputSlotA.isEmpty()) {
                balanceSlots(this.inputSlotA.subList(0, 3), WITHER_SKELETON_SKULL);
                balanceSlots(this.inputSlotA.subList(3, 7), SOUL_SAND);
            }
        }
    }
    public void balanceSlots(List<ItemStack> slots, ItemStack targetItem) {
        int total = 0;
        for (ItemStack stack : slots) {
            if (!stack.isEmpty() && stack.isItemEqual(targetItem)) {
                total += stack.getCount();
            }
        }

        if (total == 0) return;

        int perSlot = total / slots.size();
        int remainder = total % slots.size();


        for (int i = 0; i < slots.size(); i++) {
            if (perSlot > 0 || (i < remainder)) {
                int count = perSlot + (i < remainder ? 1 : 0);
                slots.set(i, new ItemStack(targetItem.getItem(), count,targetItem.getItemDamage()));
            } else {
                slots.set(i, ItemStack.EMPTY);
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
