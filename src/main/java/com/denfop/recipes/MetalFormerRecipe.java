package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MetalFormerRecipe {

    public static final String[] recipe = {"ingot", "plate", "ingot", "plate", "block"};
    public static final String[] recipe1 = {"plate", "casing", "stick", "stick", "plate"};

    public static final String[] recipe2 = {"Osmium", "Tantalum", "Cadmium"};
    public static void init() {
        addmolot("ingotLithium", "plateLithium", 1);
        addmolot("gemBor", "plateBor", 1);
        addmolot("gemBeryllium", "plateBeryllium", 1);
        for (String s : recipe2) {
            for (int j = 0; j < recipe1.length; j++) {
                if (j == 0) {
                    addmolot(recipe[j] + s, recipe1[j] + s, 1);
                }
                if (j == 1) {
                    addmolot(
                            recipe[j] + s,
                            recipe1[j] + s,
                            2
                    );
                }
                if (j == 2) {
                    addExtruding(
                            recipe[j] + s,
                            recipe1[j] + s,
                            2
                    );
                }
                if (j == 3) {
                    addExtruding(
                            recipe[j] + s,
                            recipe1[j] + s,
                            3
                    );
                }
                if (j == 4) {
                    addmolot(
                            recipe[j] + s,
                            recipe1[j] + s,
                            9
                    );
                }
            }
        }
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




        addCutting("plateMuntsa", 4, new ItemStack(IUItem.expcable)
        );

        addmolot("ingotSteel", IUItem.plateadviron);
        addmolot("ingotCopper", IUItem.platecopper);
        addmolot("ingotLead", IUItem.platelead);
        addmolot("ingotGold", IUItem.plategold);
        addmolot("ingotTin", IUItem.platetin);
        addmolot("ingotIron", IUItem.plateiron);
        addmolot("ingotBronze", IUItem.platebronze);
        addmolot("plateBronze", IUItem.casingbronze, 2);
        addmolot("plateIron", IUItem.casingiron, 2);
        addmolot("plateSteel", IUItem.casingadviron, 2);
        addmolot("plateCopper", IUItem.casingcopper, 2);
        addmolot("plateLead", IUItem.casinglead, 2);
        addmolot("plateGold", IUItem.casinggold, 2);
        addmolot("plateTin", IUItem.casingtin, 2);

        addExtruding("ingotTin", IUItem.tinCableItem, 3);
        addExtruding("ingotIron", IUItem.ironCableItem, 4);
        addExtruding("ingotGold", IUItem.goldCableItem, 4);
        addExtruding("ingotCopper", IUItem.copperCableItem, 3);


        addCutting("plateTin", IUItem.tinCableItem, 3);
        addCutting("plateIron", IUItem.ironCableItem, 4);
        addCutting("plateGold", IUItem.goldCableItem, 4);
        addCutting("plateCopper", IUItem.copperCableItem, 3);

        addExtruding(new ItemStack(IUItem.sunnarium,4,2),new ItemStack(IUItem.crafting_elements,1,422));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,0),new ItemStack(IUItem.crafting_elements,1,312));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,1),new ItemStack(IUItem.crafting_elements,1,400));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,2),new ItemStack(IUItem.crafting_elements,1,347));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,3),new ItemStack(IUItem.crafting_elements,1,408));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,4),new ItemStack(IUItem.crafting_elements,1,383));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,5),new ItemStack(IUItem.crafting_elements,1,390));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,6),new ItemStack(IUItem.crafting_elements,1,331));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,7),new ItemStack(IUItem.crafting_elements,1,431));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,8),new ItemStack(IUItem.crafting_elements,1,360));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,9),new ItemStack(IUItem.crafting_elements,1,308));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,10),new ItemStack(IUItem.crafting_elements,1,303));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,11),new ItemStack(IUItem.crafting_elements,1,317));
        addExtruding(new ItemStack(IUItem.sunnariumpanel,4,12),new ItemStack(IUItem.crafting_elements,1,351));
    }

    public static void addmolot(String input, String output, int n) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        final IInputHandler input1 = Recipes.inputFactory;
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addmolot(String input, String output) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addCutting(String input, int i, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "cutting",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, i)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addmolot(ItemStack input, int i, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, i)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addmolot(String input, ItemStack output, int n) {

        final IInputHandler input1 = Recipes.inputFactory;
        output = output.copy();
        output.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addmolot(String input, ItemStack output) {

        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addExtruding(String input, String output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "extruding",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addExtruding(String input, ItemStack output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = output.copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "extruding",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }
    public static void addExtruding(ItemStack input,  ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "extruding",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }
    public static void addExtruding(ItemStack input, ItemStack output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = output.copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "extruding",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addCutting(ItemStack input, ItemStack output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = output.copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "cutting",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addCutting(String input, ItemStack output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = output.copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "cutting",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

}
