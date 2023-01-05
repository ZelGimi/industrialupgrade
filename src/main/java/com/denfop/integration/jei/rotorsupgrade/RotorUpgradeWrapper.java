package com.denfop.integration.jei.rotorsupgrade;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RotorUpgradeWrapper implements IRecipeWrapper {


    private final ItemStack[] inputstack;


    public RotorUpgradeWrapper(RotorUpgradeHandler container) {


        this.inputstack = container.getInputs();

    }

    public ItemStack[] getInput() {
        return inputstack;
    }

    public List<List<ItemStack>> getInputs() {
        List<ItemStack> stack = new ArrayList<>();
        for (ItemStack inputs : this.inputstack) {
            if (OreDictionary.getOreIDs(inputs).length > 0) {
                int id = OreDictionary.getOreIDs(inputs)[0];
                stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
            } else {
                stack.add(inputs);
            }
        }
        return Collections.singletonList(stack);
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.getInputs());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

}
