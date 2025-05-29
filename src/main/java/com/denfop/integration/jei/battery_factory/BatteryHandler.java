package com.denfop.integration.jei.battery_factory;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BatteryHandler {

    private static final List<BatteryHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, input2, input3, input4, input5, input6, input7, input8, output;

    public BatteryHandler(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4, ItemStack input5,
            ItemStack input6, ItemStack input7, ItemStack input8,
            ItemStack output
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.input5 = input5;
        this.input6 = input6;
        this.input7 = input7;
        this.input8 = input8;
        this.output = output;
    }

    public static List<BatteryHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static BatteryHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3,
            ItemStack input4, ItemStack input5,
            ItemStack input6, ItemStack input7, ItemStack input8, ItemStack output
    ) {
        BatteryHandler recipe = new BatteryHandler(input, input1, input2, input3, input4, input5, input6, input7, input8, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static BatteryHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (BatteryHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("battery_factory")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.input.getInputs().get(4).getInputs().get(0),
                    container.input.getInputs().get(5).getInputs().get(0),
                    container.input.getInputs().get(6).getInputs().get(0),
                    container.input.getInputs().get(7).getInputs().get(0),
                    container.input.getInputs().get(8).getInputs().get(0),
                    container.getOutput().items.get(0)
            );


        }
    }

    public List<ItemStack> getInputs1() {
        return Arrays.asList(
                getInput(),
                getInput1(),
                getInput2(),
                getInput3(),
                getInput4(),
                getInput5(),
                getInput6(),
                getInput7(),
                getInput8()
        );
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
    }

    public ItemStack getInput2() { // Получатель входного предмета рецепта.
        return input2;
    }

    public ItemStack getInput3() { // Получатель входного предмета рецепта.
        return input3;
    }

    public ItemStack getInput4() { // Получатель входного предмета рецепта.
        return input4;
    }

    public ItemStack getInput5() {
        return input5;
    }

    public ItemStack getInput6() {
        return input6;
    }

    public ItemStack getInput7() {
        return input7;
    }

    public ItemStack getInput8() {
        return input8;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
