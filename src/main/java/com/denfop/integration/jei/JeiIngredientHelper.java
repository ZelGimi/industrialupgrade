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

    public static <T> T attachInputVariants(T recipe, BaseMachineRecipe container) {
        if (recipe instanceof IJeiVariantRecipe variantRecipe) {
            variantRecipe.setInputVariants(collectInputVariants(container));
        }
        return recipe;
    }

    public static List<List<ItemStack>> collectInputVariants(BaseMachineRecipe container) {
        if (container == null || container.input == null || container.input.getInputs() == null) {
            return Collections.emptyList();
        }

        List<List<ItemStack>> result = new ArrayList<>();

        for (int slot = 0; slot < container.input.getInputs().size(); slot++) {
            try {
                result.add(copyStacks(container.input.getInputs().get(slot).getInputs()));
            } catch (Exception ignored) {
                result.add(Collections.emptyList());
            }
        }

        return result;
    }

    public static List<ItemStack> getInputVariants(List<List<ItemStack>> inputVariants, int slot, ItemStack fallback) {
        if (inputVariants != null && slot >= 0 && slot < inputVariants.size()) {
            List<ItemStack> stacks = copyStacks(inputVariants.get(slot));
            if (!stacks.isEmpty()) {
                return stacks;
            }
        }

        return fallbackList(fallback);
    }

    public static List<ItemStack> getInputVariants(Object recipe, int slot, ItemStack fallback) {
        if (recipe instanceof IJeiVariantRecipe variantRecipe) {
            List<ItemStack> stacks = variantRecipe.getInputVariants(slot, fallback);
            if (stacks != null && !stacks.isEmpty()) {
                return stacks;
            }
        }

        return fallbackList(fallback);
    }

    public static void addInputSlot(
            IRecipeLayoutBuilder builder,
            RecipeIngredientRole role,
            int x,
            int y,
            Object recipe,
            int slot,
            ItemStack fallback
    ) {
        builder.addSlot(role, x, y).addItemStacks(getInputVariants(recipe, slot, fallback));
    }

    public static List<ItemStack> fallbackList(ItemStack fallback) {
        if (fallback == null || fallback.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(fallback.copy());
    }

    private static List<ItemStack> copyStacks(List<ItemStack> stacks) {
        if (stacks == null || stacks.isEmpty()) {
            return Collections.emptyList();
        }

        List<ItemStack> result = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack != null && !stack.isEmpty()) {
                result.add(stack.copy());
            }
        }
        return result;
    }

}
