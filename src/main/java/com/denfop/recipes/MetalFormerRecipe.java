package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetalFormerRecipe {

    public static final String[] recipe = {"forge:ingots/", "forge:plates/", "forge:ingots/", "forge:plates/", "forge:storage_blocks/"};
    public static final String[] recipe1 = {"forge:plates/", "forge:casings/", "forge:rods/", "forge:rods/", "forge:plates/"};

    public static final String[] recipe2 = {"Osmium", "Tantalum", "Cadmium"};
    public static final List<String> list_baseore1 = itemNames7();

    public static List<String> itemNames7() {
        return Arrays.asList(
                "Arsenic",
                "Barium",
                "Bismuth",
                "Gadolinium",
                "Gallium",
                "Hafnium",
                "Yttrium",
                "Molybdenum",
                "Neodymium",
                "Niobium",
                "Palladium",
                "Polonium",
                "Strontium",
                "Thallium",
                "Zirconium"
        );
    }

    public static List<String> itemNames() {
        List<String> list = new ArrayList<>();
        list.add("Mikhail");//0
        list.add("Aluminium");//1
        list.add("Vanady");//2
        list.add("Tungsten");//3
        list.add("Invar");//4
        list.add("Caravky");//5
        list.add("Cobalt");//6
        list.add("Magnesium");//7
        list.add("Nickel");//8
        list.add("Platinum");//9
        list.add("Titanium");//10
        list.add("Chromium");//11
        list.add("Spinel");//12
        list.add("Electrum");//13
        list.add("Silver");//14
        list.add("Zinc");//15
        list.add("Manganese");//16
        list.add("Iridium");//17
        list.add("Germanium");//18
        return list;
    }

    public static List<String> itemNames1() {
        List<String> list = new ArrayList<>();
        list.add("Aluminumbronze");//0
        list.add("Alumel");//1
        list.add("Redbrass");//2
        list.add("Muntsa");//3
        list.add("Nichrome");//4
        list.add("Alcled");//5
        list.add("Vanadoalumite");//6
        list.add("Vitalium");//7
        list.add("Duralumin");//8
        list.add("Ferromanganese");//9
        list.add("AluminiumSilicon");//10
        list.add("BerylliumBronze");//11
        list.add("Zeliber");//12
        list.add("StainlessSteel");//13
        list.add("Inconel");//14
        list.add("Nitenol");//15
        list.add("Stellite");//16
        list.add("HafniumBoride");//17
        list.add("Woods");//18
        list.add("Nimonic");//19
        list.add("TantalumTungstenHafnium");//20
        list.add("Permalloy");//21
        list.add("AluminiumLithium");//22
        list.add("CobaltChrome");//23
        list.add("HafniumCarbide");//24
        list.add("MolybdenumSteel");//25
        list.add("NiobiumTitanium");//26
        list.add("Osmiridium");//27
        list.add("SuperalloyHaynes");//28
        list.add("SuperalloyRene");//29
        list.add("YttriumAluminiumGarnet");//30
        list.add("GalliumArsenic");//31
        return list;
    }

    public static void init() {
        addmolot("forge:ingots/Lithium", "forge:plates/Lithium", 1);
        addmolot("forge:gems/Bor", "forge:plates/Bor", 1);
        addmolot("forge:gems/Beryllium", "forge:plates/Beryllium", 1);
        addmolot(new ItemStack(IUItem.synthetic_rubber.getItem()), 1, new ItemStack(IUItem.synthetic_plate.getItem(), 1));
        addmolot(new ItemStack(IUItem.graphene.getItem()), 1, new ItemStack(IUItem.graphene_plate.getItem(), 1));
        addCutting(new ItemStack(IUItem.graphene_plate.getItem()), new ItemStack(IUItem.graphene_wire.getItem(), 4), 4);

        addmolot(new ItemStack(IUItem.crafting_elements.getStack(504), 1), 1, new ItemStack(IUItem.crafting_elements.getStack(501), 1));
        addCutting(new ItemStack(IUItem.crafting_elements.getStack(495), 2), new ItemStack(IUItem.crafting_elements.getStack(494), 1), 1);
        for (String s : recipe2) {
            addcutting("forge:ingots/" + s, "forge:rods/" + s, 2);
        }
        for (String s : list_baseore1) {
            addcutting("forge:ingots/" + s, "forge:rods/" + s, 2);
        }
        addmolot(new ItemStack(IUItem.wolframite.getItem()), 1, new ItemStack(IUItem.crafting_elements.getStack(655), 1));
        addmolot(new ItemStack(IUItem.crafting_elements.getStack(655), 1), 1, new ItemStack(IUItem.crafting_elements.getStack(505), 2));
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
        for (String s : RegisterOreDictionary.spaceElementList) {
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

                }
                if (j == 3) {

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
        for (String s : list_baseore1) {
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
            for (int i = 0; i < itemNames().size(); i++) {
                if (j == 0) {
                    addmolot(recipe[j] + itemNames().get(i), new ItemStack(IUItem.plate.getStack(i), 1), 1);
                }

                if (j == 1) {
                    addmolot(
                            recipe[j] + itemNames().get(i),
                            recipe1[j] + itemNames().get(i),
                            2
                    );
                }
                if (j == 2) {
                    addExtruding(
                            recipe[j] + itemNames().get(i),
                            recipe1[j] + itemNames().get(i),
                            2
                    );
                }
                if (j == 3) {
                    addExtruding(
                            recipe[j] + itemNames().get(i),
                            recipe1[j] + itemNames().get(i),
                            3
                    );
                }
                if (j == 4) {
                    addmolot(
                            recipe[j] + itemNames().get(i),
                            recipe1[j] + itemNames().get(i),
                            9
                    );
                }
            }
        }
        addmolot(
                "forge:storage_blocks/Iron",
                "forge:plates/Iron",
                9
        );
        addmolot(
                "forge:storage_blocks/Bronze",
                "forge:plates/Bronze",
                9
        );
        addmolot(
                "forge:storage_blocks/Tin",
                "forge:plates/Tin",
                9
        );
        addmolot(
                "forge:storage_blocks/Copper",
                "forge:plates/Copper",
                9
        );
        addmolot(
                "forge:storage_blocks/Gold",
                "forge:plates/Gold",
                9
        );
        addmolot(
                "forge:storage_blocks/Lapis",
                "forge:plates/Lapis",
                9
        );
        addmolot(
                "forge:storage_blocks/Lead",
                "forge:plates/Lead",
                9
        );
        addmolot(
                "forge:storage_blocks/Steel",
                "forge:plates/Steel",
                9
        );
        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < itemNames1().size(); i++) {
                if (j == 0) {
                    addmolot(
                            recipe[j] + itemNames1().get(i),
                            recipe1[j] + itemNames1().get(i),
                            1
                    );
                }
                if (j == 1) {
                    addmolot(
                            recipe[j] + itemNames1().get(i),
                            recipe1[j] + itemNames1().get(i),
                            2
                    );
                }
                if (j == 4) {
                    addmolot(
                            recipe[j] + itemNames1().get(i),
                            recipe1[j] + itemNames1().get(i),
                            9
                    );
                }
            }
        }


        addCutting("forge:plates/Muntsa", 4, new ItemStack(IUItem.expcable.getItem()));

        addmolot("forge:ingots/Steel", IUItem.plateadviron);
        addmolot("forge:ingots/Copper", IUItem.platecopper);
        addmolot("forge:ingots/Lead", IUItem.platelead);
        addmolot("forge:ingots/Gold", IUItem.plategold);
        addmolot("forge:ingots/Tin", IUItem.platetin);
        addmolot("forge:ingots/Iron", IUItem.plateiron);
        addmolot("forge:ingots/Bronze", IUItem.platebronze);
        addmolot("forge:plates/Bronze", IUItem.casingbronze, 2);
        addmolot("forge:plates/Iron", IUItem.casingiron, 2);
        addmolot("forge:plates/Steel", IUItem.casingadviron, 2);
        addmolot("forge:plates/Copper", IUItem.casingcopper, 2);
        addmolot("forge:plates/Lead", IUItem.casinglead, 2);
        addmolot("forge:plates/Gold", IUItem.casinggold, 2);
        addmolot("forge:plates/Tin", IUItem.casingtin, 2);

        addExtruding("forge:ingots/Tin", IUItem.tinCableItem, 3);
        addExtruding("forge:ingots/Iron", IUItem.ironCableItem, 4);
        addExtruding("forge:ingots/Gold", IUItem.goldCableItem, 4);
        addExtruding("forge:ingots/Copper", IUItem.copperCableItem, 3);


        addCutting("forge:plates/Tin", IUItem.tinCableItem, 3);
        addCutting("forge:plates/Iron", IUItem.ironCableItem, 4);
        addCutting("forge:plates/Gold", IUItem.goldCableItem, 4);
        addCutting("forge:plates/Copper", IUItem.copperCableItem, 3);
        for (int i = 0; i < 19; i++) {
            addCutting(
                    new ItemStack(IUItem.stik.getStack(i), 2),
                    "forge:ingots/" + itemNames()
                            .get(i)
            );
        }
        addExtruding(new ItemStack(IUItem.sunnarium.getStack(2), 4), new ItemStack(IUItem.crafting_elements.getStack(422)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(0), 4), new ItemStack(IUItem.crafting_elements.getStack(312)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(1), 4), new ItemStack(IUItem.crafting_elements.getStack(400)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(2), 4), new ItemStack(IUItem.crafting_elements.getStack(347)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(3), 4), new ItemStack(IUItem.crafting_elements.getStack(408)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(4), 4), new ItemStack(IUItem.crafting_elements.getStack(383)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(5), 4), new ItemStack(IUItem.crafting_elements.getStack(390)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(6), 4), new ItemStack(IUItem.crafting_elements.getStack(331)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(7), 4), new ItemStack(IUItem.crafting_elements.getStack(431)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(8), 4), new ItemStack(IUItem.crafting_elements.getStack(360)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(9), 4), new ItemStack(IUItem.crafting_elements.getStack(308)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(10), 4), new ItemStack(IUItem.crafting_elements.getStack(303)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(11), 4), new ItemStack(IUItem.crafting_elements.getStack(317)));
        addExtruding(new ItemStack(IUItem.sunnariumpanel.getStack(12), 4), new ItemStack(IUItem.crafting_elements.getStack(351)));
    }

    public static void addmolot(String input, String output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;

        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
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

    public static void addcutting(String input, String output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
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

    public static void addmolot(String input, String output) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
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
        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
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

    public static void addExtruding(ItemStack input, ItemStack output) {
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

    public static void addCutting(ItemStack output, String input) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "cutting",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

}
