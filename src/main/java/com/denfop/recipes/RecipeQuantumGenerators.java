package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.quantumgenerators.QGCore;

public class RecipeQuantumGenerators {
    public static void register() {
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(QGCore.qg, 1, 0), new Object[]{
                "CBC", "BAB", "DBD",

                Character.valueOf('A'), ItemStackHelper.fromData(IUItem.blockpanel, 1, 7), Character.valueOf('B'), ItemStackHelper.fromData(
                IUItem.core,
                1,
                8
        ),
                Character.valueOf('C'), ItemStackHelper.fromData(IUItem.advQuantumtool), Character.valueOf('D'),
                TileGenerationMicrochip.getLevelCircuit(IUItem.circuitSpectral,9)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(QGCore.qg, 1, 1), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), ItemStackHelper.fromData(QGCore.qg, 1, 0), Character.valueOf('B'), ItemStackHelper.fromData(IUItem.core, 1, 9)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(QGCore.qg, 1, 2), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), ItemStackHelper.fromData(QGCore.qg, 1, 1), Character.valueOf('B'), ItemStackHelper.fromData(IUItem.core, 1, 10)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(QGCore.qg, 1, 3), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), ItemStackHelper.fromData(QGCore.qg, 1, 2), Character.valueOf('B'), ItemStackHelper.fromData(IUItem.core, 1, 11)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(QGCore.qg, 1, 4), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), ItemStackHelper.fromData(QGCore.qg, 1, 3), Character.valueOf('B'), ItemStackHelper.fromData(IUItem.core, 1, 12)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(QGCore.qg, 1, 5), new Object[]{" B ", "BAB", " B ",

                Character.valueOf('A'), ItemStackHelper.fromData(QGCore.qg, 1, 4), Character.valueOf('B'), ItemStackHelper.fromData(IUItem.core, 1, 13)});
    }
}
