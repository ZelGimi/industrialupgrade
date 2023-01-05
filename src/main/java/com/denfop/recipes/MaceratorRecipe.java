package com.denfop.recipes;

import com.denfop.Ic2Items;
import com.denfop.register.RegisterOreDictionary;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MaceratorRecipe {

    public static final String[] recipe = {"ore", "ingot"};
    public static final String[] recipe1 = {"crushed", "dust"};
    public static final int[] number = {2, 1};

    public static void recipe() {

        Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipe3 =
                ic2.api.recipe.Recipes.macerator.getRecipes();
        Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iter1 = recipe3.iterator();
        while (iter1.hasNext()) {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe2 = iter1.next();
            List<ItemStack> list = (List<ItemStack>) recipe2.getOutput();
            if (list.get(0).isItemEqual(Ic2Items.crushedSilverOre)) {
                iter1.remove();
            }
            if (list.get(0).isItemEqual(Ic2Items.silverDust)) {
                iter1.remove();
            }
            if (list.get(0).isItemEqual(Ic2Items.smallSilverDust)) {
                iter1.remove();
            }
        }
        if (!Loader.isModLoaded("aobd")) {
            for (int j = 0; j < recipe.length; j++) {
                for (int i = 0; i < RegisterOreDictionary.itemNames().size(); i++) {
                    if (i != 4 && i != 5 && i != 13) {
                        addmacerator(
                                recipe[j] + RegisterOreDictionary.itemNames().get(i),
                                recipe1[j] + RegisterOreDictionary.itemNames().get(i),
                                number[j]
                        );
                    }

                }
            }
            for (int j = 1; j < recipe.length; j++) {
                for (int i = 0; i < RegisterOreDictionary.itemNames1().size(); i++) {
                    addmacerator(
                            recipe[j] + RegisterOreDictionary.itemNames1().get(i),
                            recipe1[j] + RegisterOreDictionary.itemNames1().get(i),
                            number[j]
                    );

                }
            }


        }
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        if (!OreDictionary.getOres("oreYellorite").isEmpty()) {
            Recipes.macerator.addRecipe(input1.forOreDict("oreYellorite", 1), null, false,
                    Ic2Items.uraniumOre
            );
        }
    }

    public static void addmacerator(String input, String output, int n) {
        ItemStack stack;


        stack = OreDictionary.getOres(output).get(0);

        stack.setCount(n);

        final IRecipeInputFactory input1 = Recipes.inputFactory;

        Recipes.macerator.addRecipe(input1.forOreDict(input, 1), null, false,
                stack
        );


    }

}
