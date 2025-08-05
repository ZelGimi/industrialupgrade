package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.water.upgrade.IRotorUpgradeItem;
import com.denfop.api.water.upgrade.RotorUpgradeItemInform;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.water.upgrade.event.EventRotorItemLoad;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class ItemWaterRotor extends ItemDamage implements IWindRotor, IRotorUpgradeItem, IItemTab {
    private final int radius;
    private final float efficiency;
    private final ResourceLocation renderTexture;
    private final int level;
    private final int index;
    private final int tier;
    private final Color color;

    public ItemWaterRotor(
            String name, int durability, float efficiency,
            ResourceLocation RenderTexture, int level, int index, Color color
    ) {
        super(new Properties().stacksTo(1), durability);
        this.radius = 4;
        this.efficiency = efficiency;
        this.renderTexture = RenderTexture;
        this.level = level;
        this.index = index;
        this.color = color;
        double KU1 = 20 * efficiency * 25.0F;
        this.tier = EnergyNetGlobal.instance.getTierFromPower(KU1);
        IUCore.runnableListAfterRegisterItem.add(() -> {
            final IInputHandler input = com.denfop.api.Recipes.inputFactory;
            for (int i = 0; i < EnumInfoRotorUpgradeModules.values().length; i++) {
                Recipes.recipes.addRecipe(
                        "water_rotor_upgrade",
                        new BaseMachineRecipe(
                                new Input(
                                        input.getInput(new ItemStack(this, 1)),
                                        input.getInput(new ItemStack(IUItem.water_rotors_upgrade.getStack(i), 1))
                                ),
                                new RecipeOutput(null, new ItemStack(this, 1))
                        )
                );
            }
        });
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.water_rotor.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable TooltipContext world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        int windStrength = 10;
        int windStrength1 = 20;
        double KU = windStrength * this.getEfficiency(stack) * 25.0F;
        double KU1 = windStrength1 * this.getEfficiency(stack) * 25.0F;

        tooltip.add(Component.literal(Localization.translate("iu.watergenerator") + windStrength + " m/s "
                + Localization.translate("iu.windgenerator1") + ModUtils.getString(KU)));
        tooltip.add(Component.literal(Localization.translate("iu.watergenerator") + windStrength1 + " m/s "
                + Localization.translate("iu.windgenerator1") + ModUtils.getString(KU1)));
        tooltip.add(Component.literal(Localization.translate("gui.iu.tier") + ": " + this.getLevel()));
        tooltip.add(Component.literal(Localization.translate("iu.watergenerator1")));

        double hours = 0;
        double minutes = 0;
        double seconds = 0;
        final List<Double> time = ModUtils.Time(this.getMaxCustomDamage(stack) - this.getCustomDamage(stack));
        if (time.size() > 0) {
            hours = time.get(0);
            minutes = time.get(1);
            seconds = time.get(2);
        }

        String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") : "";
        String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") : "";
        String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") : "";

        tooltip.add(Component.literal(Localization.translate("iu.timetoend") + time1 + time2 + time3));

        if (RotorUpgradeSystem.instance.hasInMap(stack)) {
            final List<RotorUpgradeItemInform> lst = RotorUpgradeSystem.instance.getInformation(stack);
            final int col = RotorUpgradeSystem.instance.getRemaining(stack);
            if (!lst.isEmpty()) {
                for (RotorUpgradeItemInform upgrade : lst) {
                    tooltip.add(Component.literal(upgrade.getName()));
                }
            }
            if (col != 0) {
                tooltip.add(Component.literal(Localization.translate("free_slot") + col + Localization.translate("free_slot1")));
            } else {
                tooltip.add(Component.literal(Localization.translate("not_free_slot")));
            }
        }
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack itemStack, @Nonnull Level world, @Nonnull Entity entity, int slot, boolean isSelected) {

        if (!RotorUpgradeSystem.instance.hasInMap(itemStack)) {
            NeoForge.EVENT_BUS.post(new EventRotorItemLoad(world, this, itemStack));
        }

    }


    public int getDiameter(ItemStack stack) {
        return this.radius;
    }

    public ResourceLocation getRotorRenderTexture(ItemStack stack) {
        return this.renderTexture;
    }

    public float getEfficiency(ItemStack stack) {
        return this.efficiency;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public int getSourceTier() {
        return tier;
    }
}
