package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.register.RegisterOreDict;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MetalFormerRecipe {

    public static final String[] recipe = {"ingot", "plate", "ingot"};
    public static final String[] recipe1 = {"plate", "casing", "stik"};

    public static void init() {
        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < RegisterOreDict.itemNames().size(); i++) {
                if (j == 0) {
                    addmolot(recipe[j] + RegisterOreDict.itemNames().get(i), new ItemStack(IUItem.plate, 1, i), 1);
                }

                if (j != 2 && j != 0) {
                    addmolot(recipe[j] + RegisterOreDict.itemNames().get(i), recipe1[j] + RegisterOreDict.itemNames().get(i), 1);
                }
                if (j == 2) {
                    addExtruding(
                            recipe[j] + RegisterOreDict.itemNames().get(i),
                            recipe1[j] + RegisterOreDict.itemNames().get(i),
                            1
                    );
                }

            }
        }
        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < RegisterOreDict.itemNames1().size(); i++) {
                if (j != 2) {
                    addmolot(
                            recipe[j] + RegisterOreDict.itemNames1().get(i),
                            recipe1[j] + RegisterOreDict.itemNames1().get(i),
                            1
                    );
                }
            }
        }
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        Recipes.metalformerExtruding.addRecipe(input1.forStack(Ic2Items.platetin), null, false,
                new ItemStack(Ic2Items.cell.getItem(),3)
        );
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
