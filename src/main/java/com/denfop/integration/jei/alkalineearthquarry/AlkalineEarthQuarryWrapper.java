package com.denfop.integration.jei.alkalineearthquarry;

import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class AlkalineEarthQuarryWrapper implements IRecipeWrapper {

    private final ItemStack input;
    private final double chance;
    private final ItemStack output;
    private final ItemStack mesh;

    public AlkalineEarthQuarryWrapper(AlkalineEarthQuarryHandler container) {

        this.input = container.getInput();
        this.chance = container.getChance();
        this.output = container.getOutput();
        this.mesh = container.getMesh();
    }

    public ItemStack getMesh() {
        return mesh;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(this.input, this.mesh));
        ingredients.setOutput(VanillaTypes.ITEM, this.output);
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int y = 20;
        int x = 25;
        minecraft.fontRenderer.drawSplitString(Localization.translate("earth_quarry.jei1"), 5, 3,
                recipeWidth - 5, 4210752
        );
        minecraft.fontRenderer.drawSplitString(this.chance + "%", 80, 24,
                recipeWidth - 5, 4210752
        );

    }

}
