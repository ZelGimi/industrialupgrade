package com.denfop.integration.jei.sunnariumpanel;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SannariumPanelWrapper implements  IRecipeWrapper  {




    private final ItemStack inputstack;
    private final ItemStack outputstack;
    private final ItemStack inputstack1;


    public SannariumPanelWrapper(SannariumPanelHandler container) {


        this.inputstack = container.getInput();
        this.inputstack1 = container.getInput1();
        this.outputstack = container.getOutput();

    }
    public ItemStack getInput() {
        return inputstack;
    }
    public ItemStack getInput1() {
        return inputstack1;
    }
    public List<List<ItemStack>> getInputs() {
        ItemStack inputs = this.inputstack;
        List<ItemStack> stack = new ArrayList<>();
        if(OreDictionary.getOreIDs(inputs).length >0){
            int id =    OreDictionary.getOreIDs(inputs)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        }else
            stack.add(inputs);
        if(OreDictionary.getOreIDs(this.inputstack1).length >0){
            int id =    OreDictionary.getOreIDs(this.inputstack1)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        }else
            stack.add(this.inputstack1);
        return inputs.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack);
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList(Collections.singleton(this.outputstack));
    }



    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, this.getInputs());
        ingredients.setOutputs(ItemStack.class, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {


    }
}
