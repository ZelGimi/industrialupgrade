package com.denfop.recipes;

import com.denfop.register.RegisterOreDict;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class MaceratorRecipe {

    public static final String[] recipe = {"ore", "ingot"};
    public static final String[] recipe1 = {"crushed", "dust"};
    public static final int[] number = {2, 1};

    public static void recipe() {
        if (!Loader.isModLoaded("aobd")) {
            for (int j = 0; j < recipe.length; j++) {
                for (int i = 0; i < RegisterOreDict.itemNames().size(); i++) {
                    if (i != 4 && i != 5 && i != 13) {
                        addmacerator(
                                recipe[j] + RegisterOreDict.itemNames().get(i),
                                recipe1[j] + RegisterOreDict.itemNames().get(i),
                                number[j]
                        );
                    }

                }
            }
            for (int j = 1; j < recipe.length; j++) {
                for (int i = 0; i < RegisterOreDict.itemNames1().size(); i++) {
                    addmacerator(
                            recipe[j] + RegisterOreDict.itemNames1().get(i),
                            recipe1[j] + RegisterOreDict.itemNames1().get(i),
                            number[j]
                    );

                }
            }


        }


    }

    public static void addmacerator(String input, String output, int n) {
        ItemStack stack;

        if (!output.equals("dustSilver") && !output.equals("crushedSilver")) {
            stack = OreDictionary.getOres(output).get(0);
        } else {
            stack = OreDictionary.getOres(output).get(1);
        }
        stack.setCount(n);

        final IRecipeInputFactory input1 = Recipes.inputFactory;
        if (!output.equals("dustSilver") && !output.equals("crushedSilver")) {
            Recipes.macerator.addRecipe(input1.forOreDict(input, 1), null, false,
                    stack
            );
        }

    }

}
