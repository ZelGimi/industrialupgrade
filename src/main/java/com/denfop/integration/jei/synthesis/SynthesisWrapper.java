package com.denfop.integration.jei.synthesis;

import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SynthesisWrapper implements  IRecipeWrapper  {




    private final ItemStack inputstack;
    private final ItemStack outputstack;
    private final ItemStack inputstack1;
    private final int percent;


    public SynthesisWrapper(SynthesisHandler container) {


        this.inputstack = container.getInput();
        this.inputstack1 = container.getInput1();
        this.percent = container.getPercent();
        this.outputstack = container.getOutput();

    }
    public ItemStack getInput() {
        return inputstack;
    }
    public int getPercent() {
        return percent;
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
        minecraft.fontRenderer.drawSplitString(TextFormatting.GREEN + Localization.translate("chance") + this.getPercent() + "%", 69,
                67, recipeWidth - 69,  ModUtils.convertRGBcolorToInt(217, 217, 217));


    }
}
