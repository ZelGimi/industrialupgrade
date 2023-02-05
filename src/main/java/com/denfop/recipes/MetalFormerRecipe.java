package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.register.RegisterOreDictionary;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MetalFormerRecipe {

    public static final String[] recipe = {"ingot", "plate", "ingot", "plate", "block"};
    public static final String[] recipe1 = {"plate", "casing", "stick", "stick", "plate"};

    public static void init() {
        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < RegisterOreDictionary.itemNames().size(); i++) {
                if (j == 0) {
                    addmolot(recipe[j] + RegisterOreDictionary.itemNames().get(i), new ItemStack(IUItem.plate, 1, i), 1);
                }

                if (j == 1) {
                    addmolot(
                            recipe[j] + RegisterOreDictionary.itemNames().get(i),
                            recipe1[j] + RegisterOreDictionary.itemNames().get(i),
                            2
                    );
                }
                if (j == 2) {
                    addExtruding(
                            recipe[j] + RegisterOreDictionary.itemNames().get(i),
                            recipe1[j] + RegisterOreDictionary.itemNames().get(i),
                            2
                    );
                }
                if (j == 3) {
                    addExtruding(
                            recipe[j] + RegisterOreDictionary.itemNames().get(i),
                            recipe1[j] + RegisterOreDictionary.itemNames().get(i),
                            3
                    );
                }
                if (j == 4) {
                    addmolot(
                            recipe[j] + RegisterOreDictionary.itemNames().get(i),
                            recipe1[j] + RegisterOreDictionary.itemNames().get(i),
                            9
                    );
                }
            }
        }
        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < RegisterOreDictionary.itemNames1().size(); i++) {
                if (j == 0) {
                    addmolot(
                            recipe[j] + RegisterOreDictionary.itemNames1().get(i),
                            recipe1[j] + RegisterOreDictionary.itemNames1().get(i),
                            1
                    );
                }
                if (j == 1) {
                    addmolot(
                            recipe[j] + RegisterOreDictionary.itemNames1().get(i),
                            recipe1[j] + RegisterOreDictionary.itemNames1().get(i),
                            2
                    );
                }
                if (j == 4) {
                    addmolot(
                            recipe[j] + RegisterOreDictionary.itemNames1().get(i),
                            recipe1[j] + RegisterOreDictionary.itemNames1().get(i),
                            9
                    );
                }
            }
        }
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        Recipes.metalformerExtruding.addRecipe(input1.forOreDict("plateTin"), null, false,
                new ItemStack(Ic2Items.cell.getItem(), 3)
        );
        Recipes.metalformerRolling.addRecipe(input1.forStack(Ic2Items.coal_chunk, 9), null, false,
                new ItemStack(IUItem.coal_chunk1)
        );


        Recipes.metalformerCutting.addRecipe(input1.forOreDict("plateMuntsa", 4), null, false,
                new ItemStack(IUItem.expcable)
        );
        //


    }

    public static void addmolot(String input, String output, int n) {
        ItemStack stack = OreDictionary.getOres(output).get(0);
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        stack.setCount(n);
        Recipes.metalformerRolling.addRecipe(input1.forOreDict(input, 1), null, false,
                stack
        );

    }

    public static void addmolot(String input, ItemStack output, int n) {

        final IRecipeInputFactory input1 = Recipes.inputFactory;
        output.setCount(n);
        Recipes.metalformerRolling.addRecipe(input1.forOreDict(input, 1), null, false,
                output
        );

    }

    public static void addExtruding(String input, String output, int n) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack stack = OreDictionary.getOres(output).get(0);
        stack.setCount(n);
        Recipes.metalformerExtruding.addRecipe(input1.forOreDict(input, 1), null, false,
                stack
        );

    }

}
