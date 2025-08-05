package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.audio.EnumSound;
import com.denfop.componets.*;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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


    public TileBaseWitherMaker(int energyPerTick, int length, int outputSlots, IMultiTileBlock multiTileBlock, BlockPos pos, BlockState state) {
        super(energyPerTick * length, 1, outputSlots, multiTileBlock, pos, state);
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
        if (!this.getWorld().isClientSide) {
            this.output = this.getOutput();
        }

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 20 == 0) {
            if (!this.inputSlotA.isEmpty()) {
                balanceSlots(this.inputSlotA.subList(0, 3), Items.WITHER_SKELETON_SKULL);
                balanceSlots(this.inputSlotA.subList(3, 7), Blocks.SOUL_SAND.asItem());
            }
        }

    }

    public void balanceSlots(List<ItemStack> slots, Item targetItem) {
        int total = 0;
        for (ItemStack stack : slots) {
            if (!stack.isEmpty() && stack.getItem() == targetItem) {
                total += stack.getCount();
            }
        }

        if (total == 0) return;

        int perSlot = total / slots.size();
        int remainder = total % slots.size();


        for (int i = 0; i < slots.size(); i++) {
            if (perSlot > 0 || (i < remainder)) {
                int count = perSlot + (i < remainder ? 1 : 0);
                slots.set(i, new ItemStack(targetItem, count));
            } else {
                slots.set(i, ItemStack.EMPTY);
            }
        }
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();


        return this.output;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.WitherSpawn1.getSoundEvent();
    }


}
