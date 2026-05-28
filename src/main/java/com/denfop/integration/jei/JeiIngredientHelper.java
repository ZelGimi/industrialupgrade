package com.denfop.integration.jei;

import com.denfop.api.recipe.BaseMachineRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class JeiIngredientHelper {

    private JeiIngredientHelper() {
    }

    public static <T> T attachInputVariants(final T recipe, final BaseMachineRecipe container) {
        if (recipe instanceof IJeiVariantRecipe variantRecipe) {
            variantRecipe.setInputVariants(collectInputVariants(container));
        }
        return recipe;
    }

    public static List<List<ItemStack>> collectInputVariants(final BaseMachineRecipe container) {
        if (container == null || container.input == null || container.input.getInputs() == null) {
            return Collections.emptyList();
        }

        final List<List<ItemStack>> result = new ArrayList<>();

        for (int slot = 0; slot < container.input.getInputs().size(); slot++) {
            try {
                result.add(copyStacks(container.input.getInputs().get(slot).getInputs()));
            } catch (Exception ignored) {
                result.add(Collections.emptyList());
            }
        }

        return result;
    }

    public static List<ItemStack> getInputVariants(
            final List<List<ItemStack>> inputVariants,
            final int slot,
            final ItemStack fallback
    ) {
        if (inputVariants != null && slot >= 0 && slot < inputVariants.size()) {
            final List<ItemStack> stacks = copyStacks(inputVariants.get(slot));
            if (!stacks.isEmpty()) {
                return stacks;
            }
        }

        return fallbackList(fallback);
    }

    public static List<ItemStack> getInputVariants(final Object recipe, final int slot, final ItemStack fallback) {
        if (recipe instanceof IJeiVariantRecipe variantRecipe) {
            final List<ItemStack> stacks = variantRecipe.getInputVariants(slot, fallback);
            if (stacks != null && !stacks.isEmpty()) {
                return stacks;
            }
        }

        final BaseMachineRecipe container = getContainerByReflection(recipe);
        if (container != null) {
            final List<ItemStack> stacks = getInputVariants(collectInputVariants(container), slot, fallback);
            if (stacks != null && !stacks.isEmpty()) {
                return stacks;
            }
        }

        return fallbackList(fallback);
    }

    private static BaseMachineRecipe getContainerByReflection(final Object recipe) {
        if (recipe == null) {
            return null;
        }

        try {
            final Object container = recipe.getClass().getMethod("getContainer").invoke(recipe);
            if (container instanceof BaseMachineRecipe baseMachineRecipe) {
                return baseMachineRecipe;
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    public static void addInputSlot(
            final IRecipeLayoutBuilder builder,
            final RecipeIngredientRole role,
            final int x,
            final int y,
            final Object recipe,
            final int slot,
            final ItemStack fallback
    ) {
        builder.addSlot(role, x, y).addItemStacks(getInputVariants(recipe, slot, fallback));
    }

    public static List<ItemStack> fallbackList(final ItemStack fallback) {
        if (fallback == null || fallback.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(fallback.copy());
    }

    private static List<ItemStack> copyStacks(final List<ItemStack> stacks) {
        if (stacks == null || stacks.isEmpty()) {
            return Collections.emptyList();
        }

        final List<ItemStack> result = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack != null && !stack.isEmpty()) {
                result.add(stack.copy());
            }
        }
        return result;
    }

}
