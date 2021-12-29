package com.denfop.integration.jei.genstar;


import com.denfop.api.IWitherMaker;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenStarHandler {
    private static List< GenStarHandler> recipes = new ArrayList<>();


    public static List< GenStarHandler> getRecipes() { // Получатель всех рецептов.
        if(recipes.isEmpty())
            initRecipes();
        return recipes;
    }

    private final ItemStack input,input1,input2,input3,input4,input5,input6, output;


    public GenStarHandler(ItemStack input, ItemStack input1,ItemStack input2,ItemStack input3,ItemStack input4,
                            ItemStack input5,ItemStack input6,
                            ItemStack output) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.input5 = input5;
        this.input6 = input6;
        this.output = output;
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
    public ItemStack getInput3() {
        return input3;
    }
    public ItemStack getInput4() { // Получатель входного предмета рецепта.
        return input4;
    }
    public ItemStack getInput5() { // Получатель входного предмета рецепта.
        return input5;
    }
    public ItemStack getInput6() { // Получатель входного предмета рецепта.
        return input6;
    }
    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public static GenStarHandler addRecipe(ItemStack input, ItemStack input1, ItemStack input2,ItemStack input3,
                                             ItemStack input4, ItemStack input5, ItemStack input6,ItemStack output) {
        GenStarHandler recipe = new GenStarHandler(input,input1,input2,input3,input4,input5, input6,output);
        if (recipes.contains(recipe))
            return null;
        recipes.add(recipe);
        return recipe;
    }

    public static GenStarHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty())
            return null;
        for (GenStarHandler recipe : recipes)
            if (recipe.matchesInput(is))
                return recipe;
        return null;
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) ||is.isItemEqual(input1)||is.isItemEqual(input2)||is.isItemEqual(input3)||is.isItemEqual(input4);
    }

    public static void initRecipes() {
        for (Map.Entry<IWitherMaker.Input, RecipeOutput> container :
                Recipes.withermaker.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0),container.getKey().fill.getInputs().get(0),
                    container.getKey().container1.getInputs().get(0),  container.getKey().fill1.getInputs().get(0),
                    container.getKey().fill2.getInputs().get(0),container.getKey().fill3.getInputs().get(0),
                    container.getKey().fill4.getInputs().get(0),
                    container.getValue().items.get(0)  );

        }
    }



    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }
}
