package com.denfop.integration.jei.sunnarium;

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

public class SunnariumWrapper implements IRecipeWrapper {


    private final ItemStack inputstack;
    private final ItemStack outputstack;
    private final ItemStack inputstack1;
    private final ItemStack inputstack2;
    private final ItemStack inputstack3;

    public SunnariumWrapper(SunnariumHandler container) {


        this.inputstack = container.getInput();
        this.inputstack1 = container.getInput1();
        this.inputstack2 = container.getInput2();
        this.inputstack3 = container.getInput3();
        this.outputstack = container.getOutput();

    }

    public ItemStack getInput() {
        return inputstack;
    }

    public ItemStack getInput1() {
        return inputstack1;
    }

    public ItemStack getInput2() {
        return inputstack2;
    }

    public ItemStack getInput3() {
        return inputstack3;
    }

    public List<List<ItemStack>> getInputs() {
        ItemStack inputs = this.inputstack;
        List<ItemStack> stack = new ArrayList<>();
        if (OreDictionary.getOreIDs(inputs).length > 0) {
            int id = OreDictionary.getOreIDs(inputs)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(inputs);
        }
        if (OreDictionary.getOreIDs(this.inputstack1).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack1)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack1);
        }
        if (OreDictionary.getOreIDs(this.inputstack2).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack2)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack2);
        }
        if (OreDictionary.getOreIDs(this.inputstack3).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack3)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack3);
        }
        return inputs.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack);
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
