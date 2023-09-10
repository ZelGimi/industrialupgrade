package com.denfop.recipe;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class IngredientInput extends Ingredient {

    private final IInputItemStack input;
    private ItemStack[] items;
    private IntList list;

    public IngredientInput(final IInputItemStack input) {
        this.input = input;
    }

    public ItemStack[] getMatchingStacks() {
        if (this.items == null) {
            this.items = this.input.getInputs().toArray(new ItemStack[0]);
        }
        return this.items;
    }

    public boolean apply(@Nullable final ItemStack item) {
        return this.input.matches(item);
    }

    @SideOnly(Side.CLIENT)
    public IntList getValidItemStacksPacked() {
        if (this.list == null) {
            final ItemStack[] items = this.getMatchingStacks();
            this.list = new IntArrayList(items.length);
            for (final ItemStack itemstack : items) {
                this.list.add(RecipeItemHelper.pack(itemstack));
            }
            this.list.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.list;
    }

}
