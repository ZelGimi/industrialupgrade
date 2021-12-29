//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.integration.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IC2RecipeInput implements IRecipeInput {
    private final IIngredient ingredient;

    public IC2RecipeInput(IIngredient ingredient) {
        this.ingredient = ingredient;
    }

    public boolean matches(ItemStack subject) {
        return this.ingredient.matches(CraftTweakerMC.getIItemStack(subject));
    }

    public int getAmount() {
        return this.ingredient.getAmount();
    }

    public List<ItemStack> getInputs() {
        List<ItemStack> items = new ArrayList();
        Iterator var2 = this.ingredient.getItems().iterator();

        while(var2.hasNext()) {
            IItemStack item = (IItemStack)var2.next();
            items.add(CraftTweakerMC.getItemStack(item));
        }

        return items;
    }

    public int hashCode() {
        int hash = 7;
         hash = 89 * hash + (this.ingredient != null ? this.ingredient.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            IC2RecipeInput other = (IC2RecipeInput)obj;
            return this.ingredient == other.ingredient || this.ingredient != null && this.ingredient.equals(other.ingredient);
        }
    }
}
