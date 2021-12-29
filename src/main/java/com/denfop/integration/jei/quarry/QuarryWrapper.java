package com.denfop.integration.jei.quarry;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class QuarryWrapper implements  IRecipeWrapper  {



    private final ItemStack outputstack;


    public QuarryWrapper(QuarryHandler container) {


        this.outputstack = container.getOutput();

    }


    public List<ItemStack> getOutputs() {
        ItemStack inputs = this.outputstack;
        List<ItemStack> stack = new ArrayList<>();
        if(OreDictionary.getOreIDs(inputs).length >0){
            int id =    OreDictionary.getOreIDs(inputs)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        }else

            stack.add(inputs);
        return stack;
    }



    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(ItemStack.class, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }
}
