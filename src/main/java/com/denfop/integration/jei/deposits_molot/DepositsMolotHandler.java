package com.denfop.integration.jei.deposits_molot;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IBaseRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.VeinType;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepositsMolotHandler {

    private static final List<DepositsMolotHandler> recipes = new ArrayList<>();
    private final VeinType veinType;
    private final MachineRecipe machineRecipe;

    public DepositsMolotHandler(MachineRecipe machineRecipe, VeinType veinType) {
        this.veinType = veinType;
        this.machineRecipe = machineRecipe;
    }

    public static List<DepositsMolotHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static DepositsMolotHandler addRecipe(
            MachineRecipe machineRecipe,
            VeinType veinType
    ) {
        DepositsMolotHandler recipe = new DepositsMolotHandler(machineRecipe, veinType);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        for (VeinType vein : WorldBaseGen.veinTypes) {
            if (vein.getHeavyOre() != null) {
                final IBaseRecipe recipe = Recipes.recipes.getRecipe("handlerho");
                final List<BaseMachineRecipe> recipe_list = Recipes.recipes.getRecipeList("handlerho");
                final MachineRecipe output = Recipes.recipes.getRecipeMachineRecipeOutput(
                        recipe,
                        recipe_list,
                        false,
                        Collections.singletonList(new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta()))
                );
                if (output != null) {
                    addRecipe(output, vein);
                }

            }
        }
    }

    public MachineRecipe getMachineRecipe() {
        return machineRecipe;
    }

    public VeinType getVeinType() {
        return veinType;
    }


}
