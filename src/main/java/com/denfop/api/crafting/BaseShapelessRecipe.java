package com.denfop.api.crafting;

import com.denfop.api.Recipes;
import com.denfop.items.ItemToolCrafting;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.IngredientInput;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseShapelessRecipe implements CraftingRecipe {

    final NonNullList<Ingredient> listIngridient;
    private final ItemStack output;
    private final List<IInputItemStack> recipeInputList;
    private final String id;
    private ResourceLocation name;

    public BaseShapelessRecipe(ItemStack output, List<IInputItemStack> recipeInputList) {
        this.output = output;
        this.recipeInputList = recipeInputList;
        listIngridient = NonNullList.create();


        for (IInputItemStack input : this.recipeInputList) {
            listIngridient.add(new IngredientInput(input));

        }

        this.id = Recipes.registerRecipe(this);
    }

    public List<IInputItemStack> getRecipeInputList() {
        return recipeInputList;
    }

    public ItemStack getOutput() {
        return output;
    }


    public ItemStack matches(final CraftingContainer inv) {
        List<IInputItemStack> recipeInputList1 = new ArrayList<>(recipeInputList);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            final Iterator<IInputItemStack> iter = recipeInputList1.iterator();
            while (iter.hasNext()) {
                IInputItemStack recipeInput = iter.next();
                if (recipeInput.matches(stack)) {
                    iter.remove();
                    break;
                }
            }
        }
        if (!recipeInputList1.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            int col = 0;
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack stack = inv.getItem(i);
                if (!stack.isEmpty()) {
                    col++;
                }
            }
            if (col == recipeInputList.size()) {
                return this.output.copy();
            } else {
                return ItemStack.EMPTY;
            }
        }
    }


    @Override
    public boolean matches(CraftingContainer p_44002_, Level p_44003_) {
        return matches(p_44002_) != ItemStack.EMPTY;
    }


    @Override
    public ItemStack assemble(CraftingContainer p_44001_) {
        return this.output.copy();
    }


    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= this.recipeInputList.size();
    }


    @Override
    public ItemStack getResultItem() {
        return this.output.copy();
    }


    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer p_44004_) {
        final NonNullList<ItemStack> list = NonNullList.withSize(p_44004_.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < p_44004_.getContainerSize(); i++) {
            ItemStack stack = p_44004_.getItem(i);
            if (stack.getItem() instanceof ItemToolCrafting) {
                stack = stack.getItem().getCraftingRemainingItem(stack);
                list.set(i, stack);
            }
        }
        return list;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {

        return listIngridient;
    }


    @Override
    public ResourceLocation getId() {
        return new ResourceLocation(id);
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHAPELESS_RECIPE;
    }


}
