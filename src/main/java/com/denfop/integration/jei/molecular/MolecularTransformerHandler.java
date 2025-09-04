package com.denfop.integration.jei.molecular;


import com.denfop.Constants;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MolecularTransformerHandler {

    private static final List<MolecularTransformerHandler> recipes = new ArrayList<>();
    public final String inputText;
    public final String outputText;
    public final String totalEU;
    private final double energy;
    private final ItemStack input, output;

    public MolecularTransformerHandler(ItemStack input, ItemStack output, double energy) {
        this.input = input;
        this.output = output;
        this.energy = energy;
        String inputText = null;
        if (!this.input.isEmpty()) {
            inputText = input.getDisplayName().getString();
        }

        this.inputText = Localization.translate("gui.MolecularTransformer.input") + ": " + inputText;

        this.outputText =
                Localization.translate("gui.MolecularTransformer.output") + ": " + output.getDisplayName().getString();
        this.totalEU = String.format("%s %s %s", Localization.translate("gui.MolecularTransformer.energyPerOperation") + ":",
                ModUtils.getString(energy),
                Localization.translate(Constants.ABBREVIATION + ".generic.text.EF")
        );
    }

    public static List<MolecularTransformerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MolecularTransformerHandler addRecipe(ItemStack input, ItemStack output, double energy) {
        MolecularTransformerHandler recipe = new MolecularTransformerHandler(input, output, energy);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static MolecularTransformerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (MolecularTransformerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("molecular")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0), container.getOutput().metadata.getDouble("energy")
            );


        }
    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public double getEnergy() { // Получатель выходного предмета рецепта.
        return energy;
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

}
