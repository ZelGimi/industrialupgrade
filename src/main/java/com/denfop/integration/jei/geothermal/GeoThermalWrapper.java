package com.denfop.integration.jei.geothermal;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeoThermalWrapper implements IRecipeWrapper {


    private final FluidStack outputstack;
    private final FluidStack inputstack;

    public GeoThermalWrapper(GeoThermalHandler container) {


        this.inputstack = container.getInput();
        this.outputstack = container.getOutput();

    }

    public FluidStack getInputstack() {
        return inputstack;
    }

    public FluidStack getOutputstack() {
        return outputstack;
    }

    public List<List<FluidStack>> getInputs() {
        FluidStack inputs = this.inputstack;
        List<FluidStack> stack = new ArrayList<>();
        stack.add(inputs);
        return Collections.singletonList(stack);
    }

    public List<FluidStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.FLUID, this.getInputs());
        ingredients.setOutputs(VanillaTypes.FLUID, this.getOutputs());
        ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(
                new ItemStack(IUItem.crafting_elements, 1, 457),
                new ItemStack(IUItem.crafting_elements, 1, 461),
                new ItemStack(IUItem.crafting_elements, 1, 462),
                new ItemStack(IUItem.crafting_elements, 1, 463)
        ));
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                Localization.translate("cost.name") + " " + ModUtils.getString((double) 10) + " QE",
                10,
                65,
                recipeWidth - 10,
                4210752
        );
        int y = 80;
        int x = 35;
        for (int i = 0; i < 4; i++) {
            double percent = 2;
            if (i > 0) {
                percent = 0.5;
            }
            minecraft.fontRenderer.drawSplitString("-> " + percent + "%", x, y, recipeWidth - x, 4210752);
            y += 16;
        }
    }

}
