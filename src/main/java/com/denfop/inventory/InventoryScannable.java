package com.denfop.inventory;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IBaseRecipe;
import com.denfop.api.recipe.IRecipeInputStack;
import com.denfop.api.recipe.RecipeInputStack;
import com.denfop.blockentity.base.BlockEntityScanner;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class InventoryScannable extends Inventory {

    private final IBaseRecipe recipe;
    private final List<IRecipeInputStack> accepts;
    private final BlockEntityScanner tile;
    private final List<BaseMachineRecipe> recipes;

    public InventoryScannable(BlockEntityScanner base1, int count) {
        super(base1, TypeItemSlot.INPUT, count);
        this.setStackSizeLimit(1);
        this.tile = base1;
        this.recipe = Recipes.recipes.getRecipe("replicator");
        this.accepts = Recipes.recipes.getMap_recipe_managers_itemStack(this.recipe.getName());
        this.recipes = Recipes.recipes.getRecipeList(recipe.getName());
    }

    public boolean canPlaceItem(int index, ItemStack stack) {
        return accepts.contains(
                new RecipeInputStack(stack)) && !(this.tile.state == BlockEntityScanner.State.COMPLETED);
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (content.isEmpty()) {
            this.tile.recipe = null;
        } else {
            this.tile.recipe = Recipes.recipes.getRecipeOutput(this.recipe, this.recipes, false, content);
            this.tile.pattern = this.tile.recipe.output.items.get(0);
        }
        return content;
    }

}
