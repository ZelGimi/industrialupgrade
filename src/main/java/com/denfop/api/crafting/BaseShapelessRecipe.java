package com.denfop.api.crafting;

import com.denfop.api.Recipes;
import com.denfop.items.ItemToolCrafting;
import com.denfop.recipe.IInputItemStack;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseShapelessRecipe implements IRecipe {

    private final ItemStack output;
    private final List<IInputItemStack> recipeInputList;
    private ResourceLocation name;

    public BaseShapelessRecipe(ItemStack output, List<IInputItemStack> recipeInputList) {
        this.output = output;
        this.recipeInputList = recipeInputList;
        Recipes.registerRecipe(this);
    }

    public List<IInputItemStack> getRecipeInputList() {
        return recipeInputList;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        return getCraftingResult(inv) != ItemStack.EMPTY;
    }

    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        List<IInputItemStack> recipeInputList1 = new ArrayList<>(recipeInputList);
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            final Iterator<IInputItemStack> iter = recipeInputList1.iterator();
            while (iter.hasNext()) {
                IInputItemStack recipeInput = iter.next();
                if (recipeInput.matches(stack)) {
                    iter.remove();
                    break;
                }
            }
        }
        if (recipeInputList1.size() != 0) {
            return ItemStack.EMPTY;
        } else {
            int col = 0;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = inv.getStackInSlot(i);
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
    public boolean canFit(final int x, final int y) {
        return x * y >= this.recipeInputList.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
        final NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemToolCrafting) {
                stack = stack.getItem().getContainerItem(stack);
                list.set(i, stack);
            }
        }
        return list;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        final NonNullList<Ingredient> list = NonNullList.create();


        for (IInputItemStack input : this.recipeInputList) {
            list.add(Ingredient.fromStacks(input.getInputs().toArray(new ItemStack[0])));

        }

        return list;
    }

    @Override
    public IRecipe setRegistryName(final ResourceLocation name) {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }

}
