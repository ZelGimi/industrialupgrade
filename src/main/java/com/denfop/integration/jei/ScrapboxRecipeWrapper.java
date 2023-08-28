package com.denfop.integration.jei;


import com.denfop.IUItem;
import com.denfop.recipes.ScrapboxRecipeManager;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScrapboxRecipeWrapper implements IRecipeWrapper {

    private final Map.Entry<ItemStack, Float> entry;

    public ScrapboxRecipeWrapper(Map.Entry<ItemStack, Float> entry) {
        this.entry = entry;
    }

    public static List<ScrapboxRecipeWrapper> createRecipes() {
        List<ScrapboxRecipeWrapper> recipes = new ArrayList();

        for (final Map.Entry<ItemStack, Float> itemStackFloatEntry : ScrapboxRecipeManager.instance.getDrops().entrySet()) {
            recipes.add(new ScrapboxRecipeWrapper(itemStackFloatEntry));
        }

        return recipes;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        float value = this.entry.getValue();
        String text;
        if ((double) value < 0.001) {
            text = "< 0.01";
        } else {
            text = "  " + String.format("%.2f", value * 100.0F);
        }

        minecraft.fontRenderer.drawString(text + "%", 86, 9, 4210752);
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, IUItem.scrapBox);
        ingredients.setOutput(ItemStack.class, this.entry.getKey());
    }

}
