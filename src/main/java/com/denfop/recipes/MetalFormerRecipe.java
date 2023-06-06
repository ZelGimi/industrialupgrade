package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
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
        addmolot(
                "blockIron",
                "plateIron",
                9
        );
        addmolot(
                "blockTin",
                "plateTin",
                9
        );
        addmolot(
                "blockCopper",
                "plateCopper",
                9
        );
        addmolot(
                "blockGold",
                "plateGold",
                9
        );
        addmolot(
                "blockLapis",
                "plateLapis",
                9
        );
        addmolot(
                "blockLead",
                "plateLead",
                9
        );
        addmolot(
                "blockSteel",
                "plateSteel",
                9
        );
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
        addExtruding("plateTin", Ic2Items.cell, 3
        );
        addmolot(Ic2Items.coal_chunk, 9, new ItemStack(IUItem.coal_chunk1)
        );


        addCutting("plateMuntsa", 4, new ItemStack(IUItem.expcable)
        );
        //
        addmolot("ingotSteel", Ic2Items.plateadviron);
        addmolot("ingotCopper", Ic2Items.platecopper);
        addmolot("ingotLead", Ic2Items.platelead);
        addmolot("ingotGold", Ic2Items.plategold);
        addmolot("ingotTin", Ic2Items.platetin);
        addmolot("ingotIron", Ic2Items.plateiron);
        addmolot("ingotBronze", Ic2Items.platebronze);
        addmolot("plateBronze", Ic2Items.casingbronze, 2);
        addmolot("plateIron", Ic2Items.casingiron, 2);
        addmolot("plateSteel", Ic2Items.casingadviron, 2);
        addmolot("plateCopper", Ic2Items.casingcopper, 2);
        addmolot("plateLead", Ic2Items.casinglead, 2);
        addmolot("plateGold", Ic2Items.casinggold, 2);
        addmolot("plateTin", Ic2Items.casingtin, 2);

        addExtruding(Ic2Items.casingtin, Ic2Items.tinCan, 1);
        addExtruding("ingotTin", IUItem.tinCableItem, 3);
        addExtruding("ingotIron", IUItem.ironCableItem, 4);
        addExtruding("ingotGold", IUItem.goldCableItem, 4);
        addExtruding("ingotCopper", IUItem.copperCableItem, 3);
        addExtruding(Ic2Items.casingiron, Ic2Items.ironFence, 1);
        addExtruding("plateIron", Ic2Items.fuelRod, 1);

        addCutting("plateTin", IUItem.tinCableItem, 3);
        addCutting("plateIron", IUItem.ironCableItem, 4);
        addCutting("plateGold", IUItem.goldCableItem, 4);
        addCutting("plateCopper", IUItem.copperCableItem, 3);
        addCutting("casingIron", Ic2Items.coin, 1);
    }

    public static void addmolot(String input, String output, int n) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addmolot(String input, String output) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addCutting(String input, int i, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "cutting",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, i)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addmolot(ItemStack input, int i, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input, i)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addmolot(String input, ItemStack output, int n) {

        final IRecipeInputFactory input1 = Recipes.inputFactory;
        output = output.copy();
        output.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addmolot(String input, ItemStack output) {

        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addExtruding(String input, String output, int n) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "extruding",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addExtruding(String input, ItemStack output, int n) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack stack = output.copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "extruding",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addExtruding(ItemStack input, ItemStack output, int n) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack stack = output.copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "extruding",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addCutting(ItemStack input, ItemStack output, int n) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack stack = output.copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "cutting",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addCutting(String input, ItemStack output, int n) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack stack = output.copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "cutting",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

}
