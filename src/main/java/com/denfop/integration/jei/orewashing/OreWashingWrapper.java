package com.denfop.integration.jei.orewashing;

import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OreWashingWrapper implements IRecipeWrapper {


    private final ItemStack inputstack;
    private final List<ItemStack> outputstack;
    private final short temperature;


    public OreWashingWrapper(OreWashingHandler container) {


        this.inputstack = container.getInput();
        this.outputstack = container.getOutput();
        this.temperature = container.getTemperature();
    }

    public ItemStack getInput() {
        return inputstack;
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
        return inputs.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack);
    }

    public List<ItemStack> getOutputs() {
        return this.outputstack;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public List<ItemStack> getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(FluidRegistry.WATER.getLocalizedName(new FluidStack(
                FluidRegistry.WATER,
                1
        )) + ": " + temperature + " " + Localization.translate(
                "iu" + ".generic.text.mb"), 88, 44, 4210752);
    }

}
