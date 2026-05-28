package com.denfop.integration.jei.multiblock;

import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class MultiBlockHandler {

    private static final List<MultiBlockHandler> RECIPES = new ArrayList<>();

    private final String name;
    private final MultiBlockStructure structure;
    private final List<ItemStack> ingredientStacks;
    private final ItemStack catalystStack;
    private final Component displayName;

    public MultiBlockHandler(final String name, final MultiBlockStructure structure) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("MultiBlockHandler name cannot be null or blank");
        }
        if (structure == null) {
            throw new IllegalArgumentException("MultiBlockStructure cannot be null");
        }

        this.name = name;
        this.structure = structure;
        this.ingredientStacks = Collections.unmodifiableList(collectIngredientStacks(structure));
        this.catalystStack = resolveCatalystStack(structure, this.ingredientStacks);
        this.displayName = Component.literal(name);
    }

    public static List<MultiBlockHandler> getRecipes() {
        if (RECIPES.isEmpty()) {
            initRecipes();
        }
        return RECIPES;
    }

    public static void refreshRecipes() {
        RECIPES.clear();
        initRecipes();
    }

    public static MultiBlockHandler addRecipe(final String name, final MultiBlockStructure structure) {
        if (name == null || name.isBlank() || structure == null) {
            return null;
        }

        for (final MultiBlockHandler recipe : RECIPES) {
            if (recipe.getName().equals(name)) {
                return recipe;
            }
        }

        final MultiBlockHandler recipe = new MultiBlockHandler(name, structure);
        RECIPES.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        RECIPES.clear();

        final MultiBlockSystem system = MultiBlockSystem.getInstance();
        if (system == null || system.mapMultiBlocks == null || system.mapMultiBlocks.isEmpty()) {
            return;
        }

        for (final Map.Entry<String, MultiBlockStructure> entry : system.mapMultiBlocks.entrySet()) {
            addRecipe(entry.getKey(), entry.getValue());
        }
    }

    private static List<ItemStack> collectIngredientStacks(final MultiBlockStructure structure) {
        final List<ItemStack> result = new ArrayList<>();

        if (structure.itemStackList != null && !structure.itemStackList.isEmpty()) {
            for (final ItemStack stack : structure.itemStackList) {
                if (stack != null && !stack.isEmpty()) {
                    result.add(stack.copy());
                }
            }
            return result;
        }

        if (structure.ItemStackMap == null || structure.ItemStackMap.isEmpty()) {
            return result;
        }

        for (final ItemStack stack : structure.ItemStackMap.values()) {
            if (stack == null || stack.isEmpty()) {
                continue;
            }

            addOrGrow(result, stack);
        }

        return result;
    }

    private static void addOrGrow(final List<ItemStack> result, final ItemStack stack) {
        for (final ItemStack existing : result) {
            if (ItemStack.isSameItemSameComponents(existing, stack)) {
                existing.grow(stack.getCount());
                return;
            }
        }

        result.add(stack.copy());
    }

    private static ItemStack resolveCatalystStack(
            final MultiBlockStructure structure,
            final List<ItemStack> ingredientStacks
    ) {
        if (structure.activateItem != null && !structure.activateItem.isEmpty()) {
            return structure.activateItem.copy();
        }

        if (structure.itemStackList != null && !structure.itemStackList.isEmpty()) {
            final ItemStack first = structure.itemStackList.get(0);
            if (first != null && !first.isEmpty()) {
                return first.copy();
            }
        }

        if (!ingredientStacks.isEmpty()) {
            return ingredientStacks.get(0).copy();
        }

        return ItemStack.EMPTY;
    }

    public MultiBlockStructure getStructure() {
        return this.structure;
    }

    public String getName() {
        return this.name;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public List<ItemStack> getIngredientStacks() {
        final List<ItemStack> result = new ArrayList<>(this.ingredientStacks.size());
        for (final ItemStack stack : this.ingredientStacks) {
            result.add(stack.copy());
        }
        return result;
    }

    public ItemStack getCatalystStack() {
        return this.catalystStack.copy();
    }

    public int getBlockCount() {
        return this.structure.ItemStackMap == null ? 0 : this.structure.ItemStackMap.size();
    }

    public int getLayerCount() {
        if (this.structure.ItemStackMap == null || this.structure.ItemStackMap.isEmpty()) {
            return 0;
        }
        return Math.max(1, this.structure.maxHeight - this.structure.minHeight + 1);
    }

    public int getTypeCount() {
        return this.ingredientStacks.size();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MultiBlockHandler other)) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}