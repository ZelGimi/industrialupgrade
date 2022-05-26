package com.denfop.integration.jei.microchip;


import com.denfop.api.IMicrochipFarbricatorRecipeManager;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MicrochipHandler {

    private static final List<MicrochipHandler> recipes = new ArrayList<>();
    private final short temperature;
    private final ItemStack input, input1, input2, input3, input4, output;

    public MicrochipHandler(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4,
            ItemStack output, short temperature
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.temperature = temperature;
        this.output = output;
    }

    public static List<MicrochipHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MicrochipHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3,
            ItemStack input4, ItemStack output, short temperature
    ) {
        MicrochipHandler recipe = new MicrochipHandler(input, input1, input2, input3, input4, output, temperature);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static MicrochipHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (MicrochipHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (Map.Entry<IMicrochipFarbricatorRecipeManager.Input, RecipeOutput> container :
                Recipes.GenerationMicrochip.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0), container.getKey().fill.getInputs().get(0),
                    container.getKey().fill1.getInputs().get(0), container.getKey().fill2.getInputs().get(0),
                    container.getKey().container1.getInputs().get(0),
                    container.getValue().items.get(0), container.getValue().metadata.getShort("temperature")
            );

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }

    public short getTemperature() { // Получатель входного предмета рецепта.
        return temperature;
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

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1) || is.isItemEqual(input2) || is.isItemEqual(input3) || is.isItemEqual(
                input4);
    }

}
