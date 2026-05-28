package com.denfop.api.pollution.radiation;

import com.denfop.datacomponent.DataComponentsInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;

import java.util.Locale;
import java.util.Set;

public final class RadiationFoodHelper {

    private static final double MEAT_THRESHOLD = 6.0D;
    private static final double VEGETABLE_THRESHOLD = 10.0D;
    private static final double WATER_THRESHOLD = 4.0D;
    private static final double OTHER_THRESHOLD = 8.0D;

    private static final Set<Item> VEGETABLE_ITEMS = Set.of(
            Items.CARROT,
            Items.GOLDEN_CARROT,
            Items.POTATO,
            Items.BAKED_POTATO,
            Items.POISONOUS_POTATO,
            Items.BEETROOT,
            Items.DRIED_KELP,
            Items.APPLE,
            Items.GOLDEN_APPLE,
            Items.ENCHANTED_GOLDEN_APPLE,
            Items.MELON_SLICE,
            Items.SWEET_BERRIES,
            Items.GLOW_BERRIES,
            Items.CHORUS_FRUIT,
            Items.PUMPKIN_PIE,
            Items.COOKIE
    );

    private RadiationFoodHelper() {
    }

    public static boolean isConsumableCandidate(ItemStack stack) {
        return stack != null
                && !stack.isEmpty()
                && (stack.has(DataComponents.FOOD) || stack.getUseAnimation() == UseAnim.DRINK);
    }

    public static boolean isContaminatableFood(ItemStack stack) {
        return isConsumableCandidate(stack);
    }

    public static FoodCategory getCategory(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return FoodCategory.OTHER;
        }

        if (isDrinkLike(stack)) {
            return FoodCategory.WATER;
        }

        if (stack.has(DataComponents.FOOD)) {
            if (stack.is(ItemTags.MEAT)) {
                return FoodCategory.MEAT;
            }

            if (VEGETABLE_ITEMS.contains(stack.getItem())) {
                return FoodCategory.VEGETABLE;
            }
        }

        return FoodCategory.OTHER;
    }

    public static boolean isDrinkLike(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        if (stack.getUseAnimation() == UseAnim.DRINK) {
            return true;
        }

        return stack.is(Items.POTION)
                || stack.is(Items.HONEY_BOTTLE)
                || stack.is(Items.MILK_BUCKET);
    }

    public static boolean isWaterLikeCategory(ItemStack stack) {
        return getCategory(stack) == FoodCategory.WATER;
    }

    public static double getSpoilThreshold(ItemStack stack) {
        return switch (getCategory(stack)) {
            case MEAT -> MEAT_THRESHOLD;
            case VEGETABLE -> VEGETABLE_THRESHOLD;
            case WATER -> WATER_THRESHOLD;
            case OTHER -> OTHER_THRESHOLD;
        };
    }

    public static double getFoodRadiationDose(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0.0D;
        }

        Double dose = stack.get(DataComponentsInit.FOOD_RADIATION_DOSE.get());
        return dose == null ? 0.0D : Math.max(0.0D, dose);
    }

    public static boolean hasRadiation(ItemStack stack) {
        return getFoodRadiationDose(stack) > 0.0D;
    }

    public static void setFoodRadiationDose(ItemStack stack, double dose) {
        if (stack == null || stack.isEmpty()) {
            return;
        }

        double safeDose = Math.max(0.0D, dose);

        if (safeDose <= 0.0D) {
            stack.remove(DataComponentsInit.FOOD_RADIATION_DOSE.get());
            return;
        }

        stack.set(DataComponentsInit.FOOD_RADIATION_DOSE.get(), safeDose);
    }

    public static double addFoodRadiationDose(ItemStack stack, double delta) {
        if (stack == null || stack.isEmpty() || delta <= 0.0D) {
            return getFoodRadiationDose(stack);
        }

        double updated = getFoodRadiationDose(stack) + delta;
        setFoodRadiationDose(stack, updated);
        return updated;
    }

    public static double getBaseContaminationIncrement(EnumLevelRadiation level) {
        if (level == null) {
            return 0.0D;
        }

        return switch (level) {
            case LOW -> 0.01D;
            case DEFAULT -> 0.02D;
            case MEDIUM -> 0.08D;
            case HIGH -> 0.25D;
            case VERY_HIGH -> 1.5D;
        };
    }

    public static double getContaminationIncrement(EnumLevelRadiation level, ItemStack stack) {
        double base = getBaseContaminationIncrement(level);

        return switch (getCategory(stack)) {
            case WATER -> base * 1.35D;
            case MEAT -> base * 1.10D;
            case VEGETABLE -> base * 0.80D;
            case OTHER -> base;
        };
    }

    public static boolean shouldSpoilIntoRottenFlesh(ItemStack stack) {
        if (!isConsumableCandidate(stack)) {
            return false;
        }

        if (isWaterLikeCategory(stack)) {
            return false;
        }

        return !stack.is(Items.ROTTEN_FLESH) && getFoodRadiationDose(stack) >= getSpoilThreshold(stack);
    }

    public static boolean isAboveCriticalThreshold(ItemStack stack) {
        return getFoodRadiationDose(stack) >= getSpoilThreshold(stack);
    }

    public static ItemStack createSpoiledStack(ItemStack source) {
        ItemStack spoiled = new ItemStack(Items.ROTTEN_FLESH, source.getCount());
        double dose = getFoodRadiationDose(source);
        setFoodRadiationDose(spoiled, dose);

        if (source.has(DataComponents.CUSTOM_NAME)) {
            spoiled.set(DataComponents.CUSTOM_NAME, source.getHoverName());
        }

        return spoiled;
    }

    public static ChatFormatting getDoseFormatting(double dose, double threshold) {
        if (dose >= threshold) {
            return ChatFormatting.DARK_RED;
        }
        if (dose >= threshold * 0.70D) {
            return ChatFormatting.RED;
        }
        if (dose >= threshold * 0.35D) {
            return ChatFormatting.YELLOW;
        }
        return ChatFormatting.GREEN;
    }

    public static float getDoseRatio(ItemStack stack) {
        double threshold = getSpoilThreshold(stack);
        if (threshold <= 0.0D) {
            return 0.0F;
        }
        return (float) Math.max(0.0D, Math.min(1.0D, getFoodRadiationDose(stack) / threshold));
    }

    public static String formatDose(double dose) {
        return String.format(Locale.ROOT, "%.2f", dose);
    }

    public enum FoodCategory {
        MEAT("iu.tooltip.radiation_food.category.meat"),
        VEGETABLE("iu.tooltip.radiation_food.category.vegetable"),
        WATER("iu.tooltip.radiation_food.category.water"),
        OTHER("iu.tooltip.radiation_food.category.other");

        private final String translationKey;

        FoodCategory(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }
}