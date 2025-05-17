package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.simplyquarries.SimplyQuarries;

public class RecipeSimplyQuarries {
    public static void register() {

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(SimplyQuarries.quarry, 1), new Object[]{
                " D ", "ABE", " C ",

                Character.valueOf('A'), ItemStackHelper.fromData( IUItem.crafting_elements, 1, 158),

                Character.valueOf('B'), IUItem.advancedMachine,

                Character.valueOf('C'), IUItem.elemotor,

                Character.valueOf('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 72),

                Character.valueOf('E'),ItemStackHelper.fromData( IUItem.crafting_elements, 1, 44)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(SimplyQuarries.quarry, 1, 1), new Object[]{
                "D D", "ABE", "DCD",

                Character.valueOf('A'), ItemStackHelper.fromData( IUItem.crafting_elements, 1, 256),

                Character.valueOf('B'), ItemStackHelper.fromData(SimplyQuarries.quarry, 1),

                Character.valueOf('C'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 20),

                Character.valueOf('D'),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 138),

                Character.valueOf('E'), ItemStackHelper.fromData( IUItem.crafting_elements, 1, 47)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(SimplyQuarries.quarry, 1, 2), new Object[]{
                "D D", "ABE", "DCD",

                Character.valueOf('A'),ItemStackHelper.fromData( IUItem.crafting_elements, 1, 252),

                Character.valueOf('B'), ItemStackHelper.fromData(SimplyQuarries.quarry, 1, 1),

                Character.valueOf('C'), ItemStackHelper.fromData( IUItem.crafting_elements, 1, 96),

                Character.valueOf('D'),
                ItemStackHelper.fromData( IUItem.crafting_elements, 1, 139),

                Character.valueOf('E'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 49)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(SimplyQuarries.quarry, 1, 3), new Object[]{
                "D D", "ABE", "DCD",

                Character.valueOf('A'), ItemStackHelper.fromData( IUItem.crafting_elements, 1, 254),

                Character.valueOf('B'), ItemStackHelper.fromData(SimplyQuarries.quarry, 1, 2),

                Character.valueOf('C'), ItemStackHelper.fromData( IUItem.crafting_elements, 1, 120),

                Character.valueOf('D'),
                ItemStackHelper.fromData( IUItem.crafting_elements, 1, 140),

                Character.valueOf('E'), ItemStackHelper.fromData( IUItem.crafting_elements, 1, 51)});

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(SimplyQuarries.quarry, 1, 4), new Object[]{
                "D D", "ABE", "DCD",

                Character.valueOf('A'), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 631),

                Character.valueOf('B'),ItemStackHelper.fromData(SimplyQuarries.quarry, 1, 3),

                Character.valueOf('C'),ItemStackHelper.fromData( IUItem.crafting_elements, 1, 622),

                Character.valueOf('D'),
                ItemStackHelper.fromData( IUItem.crafting_elements, 1, 623),

                Character.valueOf('E'), ItemStackHelper.fromData( IUItem.crafting_elements, 1, 52)});

    }
}
