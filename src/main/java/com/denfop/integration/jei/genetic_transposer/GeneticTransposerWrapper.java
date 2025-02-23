package com.denfop.integration.jei.genetic_transposer;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneticTransposerWrapper implements IRecipeWrapper {


    private final ItemStack inputstack;
    private final ItemStack outputstack;
    private final ItemStack inputstack1;
    private final FluidStack inputstack2;
    private final ItemStack inputstack3;
    private final ItemStack inputstack4;

    public GeneticTransposerWrapper(GeneticTransposerHandler container) {


        this.inputstack = container.getInput();
        this.inputstack1 = container.getInput1();
        this.inputstack2 = container.getInput2();
        this.inputstack3 = container.getInput3();
        this.inputstack4 = container.getInput4();
        this.outputstack = container.getOutput();

    }

    public ItemStack getInput() {
        return inputstack;
    }

    public ItemStack getInput1() {
        return inputstack1;
    }

    public FluidStack getInput2() {
        return inputstack2;
    }

    public ItemStack getInput3() {
        return inputstack3;
    }

    public ItemStack getInput4() {
        return inputstack4;
    }
    public List<List<ItemStack>> getInputs() {
        ItemStack inputs = this.inputstack;
        List<ItemStack> stack = new ArrayList<>();
        stack.add(inputs);
        stack.add(this.inputstack1);
        stack.add(this.inputstack3);
        stack.add(this.inputstack4);
        return inputs.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack);
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.getInputs());
        ingredients.setInput(VanillaTypes.FLUID, this.inputstack2);
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
