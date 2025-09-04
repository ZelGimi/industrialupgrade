package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blocks.BlockRaws;
import com.denfop.recipe.IInputHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaceratorRecipe {

    public static final String[] recipe = {"c:raw_materials/", "c:ingots/"};
    public static final String[] recipe1 = {"c:crushed/", "c:dusts/"};
    public static final int[] number = {2, 1};

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
        list.add("vanadium");//2
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

    public static void recipe() {


        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < itemNames().size(); i++) {
                if (i != 4 && i != 5 && i != 13) {
                    addmacerator(
                            recipe[j] + itemNames().get(i),
                            recipe1[j] + itemNames().get(i),
                            number[j]
                    );
                }

            }
        }
        for (int j = 1; j < recipe.length; j++) {
            for (int i = 0; i < itemNames1().size(); i++) {
                addmacerator(
                        recipe[j] + itemNames1().get(i),
                        recipe1[j] + itemNames1().get(i),
                        number[j]
                );

            }
        }


        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < itemNames7().size(); i++) {
                addmacerator(
                        recipe[j] + itemNames7().get(i),
                        recipe1[j] + itemNames7().get(i),
                        number[j]
                );

            }
        }

        addmacerator(IUItem.plateobsidian, IUItem.obsidianDust);


        addmacerator("wool", "c:strings", 4);
        addmacerator("c:raw_materials/Osmium", "c:crushed/Osmium", 2);
        addmacerator("c:dusts/Osmium", "c:smalldust/Osmium", 9);
        addmacerator("c:dusts/Platinum", "c:smalldust/Platinum", 9);
        addmacerator("c:raw_materials/Tantalum", "c:crushed/Tantalum", 2);
        addmacerator("c:raw_materials/Cadmium", "c:crushed/Cadmium", 2);
        addmacerator("c:raw_materials/Tin", "c:crushed/Tin", 2);
        addmacerator("c:raw_materials/Iron", "c:crushed/Iron", 2);
        addmacerator("c:raw_materials/Gold", "c:crushed/Gold", 2);
        addmacerator("c:raw_materials/Copper", "c:crushed/Copper", 2);
        addmacerator("c:raw_materials/Lead", "c:crushed/Lead", 2);
        addmacerator("c:ingots/Osmium", "c:dusts/Osmium");
        addmacerator("c:ingots/Tantalum", "c:dusts/Tantalum");
        addmacerator("c:ingots/Cadmium", "c:dusts/Cadmium");
        addmacerator("c:ingots/Electrum", "c:dusts/Electrum");
        addmacerator("c:ingots/Invar", "c:dusts/Invar");
        addmacerator("c:gems/Quartz", "c:dusts/Quartz");

        addmacerator("c:ingots/Bronze", "c:dusts/Bronze");
        addmacerator("c:ingots/Lead", "c:dusts/Lead");
        addmacerator("c:plateDense/Lead", "c:dusts/Lead", 9);
        addmacerator(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.QUARTZ, 4));
        addmacerator(new ItemStack(Items.FLINT, 2), new ItemStack(IUItem.iudust.getStack(60), 1));
        addmacerator(new ItemStack(Items.WHEAT_SEEDS, 16), IUItem.biochaff);
        addmacerator(new ItemStack(IUItem.crops.getStack(0), 16), IUItem.biochaff);
        addmacerator(new ItemStack(Blocks.CLAY), "c:dusts/Clay");
        addmacerator("c:crushed/Copper", "c:dusts/Copper");
        addmacerator("c:plateDense/Obsidian", "c:dusts/Obsidian", 9);
        addmacerator("c:plateDense/Lapis", "c:dusts/Lapis", 9);
        addmacerator(new ItemStack(Blocks.TALL_GRASS, 8), IUItem.biochaff);
        addmacerator("c:ingots/Copper", "c:dusts/Copper");
        addmacerator(new ItemStack(Items.BONE, 1), new ItemStack(Items.WHITE_DYE, 4));
        addmacerator(new ItemStack(Items.MELON, 8), IUItem.biochaff);
        addmacerator("saplings", 4, IUItem.biochaff);
        addmacerator("c:plateDense/Iron", "c:dusts/Iron", 9);
        addmacerator("c:crushed/Iron", "c:dusts/Iron");
        addmacerator("c:gems/Emerald", new ItemStack(IUItem.iudust.getStack(74), 1));

        addmacerator(new ItemStack(IUItem.apatite.getItem(2), 1), new ItemStack(IUItem.iudust.getStack(66), 1));
        addmacerator(new ItemStack(IUItem.apatite.getItem(3), 1), new ItemStack(IUItem.iudust.getStack(69), 1));
        addmacerator(new ItemStack(IUItem.apatite.getItem(4), 1), new ItemStack(IUItem.iudust.getStack(70), 1));


        addmacerator(new ItemStack(Items.COAL), "c:dusts/Coal");
        addmacerator("c:crushed/Lead", "c:dusts/Lead");
        addmacerator("c:crushed/Tin", "c:dusts/Tin");
        addmacerator("c:crushed/Gold", "c:dusts/Gold");
        addmacerator("c:plateDense/Copper", "c:dusts/Copper", 9);
        addmacerator("c:plateDense/Tin", "c:dusts/Tin", 9);
        addmacerator("c:plateDense/Gold", "c:dusts/Gold", 9);
        addmacerator(new ItemStack(Blocks.ICE), new ItemStack(Items.SNOWBALL));
        addmacerator(new ItemStack(Items.PUMPKIN_SEEDS, 16), IUItem.biochaff);
        addmacerator("c:plates/Lapis", "c:dusts/Lapis");
        addmacerator("c:plateDense/Bronze", "c:dusts/Bronze", 9);

        addmacerator(IUItem.biochaff, new ItemStack(Blocks.DIRT));
        addmacerator(new ItemStack(Blocks.PUMPKIN, 8), IUItem.biochaff);
        addmacerator("c:gems/Diamond", "c:dusts/Diamond");
        addmacerator(new ItemStack(Items.RABBIT, 8), IUItem.biochaff);
        addmacerator("c:storage_blocks/Coal", "c:dusts/Coal", 9);
        addmacerator(new ItemStack(Blocks.OBSIDIAN), IUItem.obsidianDust);
        addmacerator(new ItemStack(Items.SUGAR_CANE, 8), IUItem.biochaff);
        addmacerator("c:ingots/Gold", "c:dusts/Gold");
        addmacerator("c:ingots/Iron", "c:dusts/Iron");
        addmacerator("c:ingots/Tin", "c:dusts/Tin");
        addmacerator(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.SAND));
        addmacerator("leaves", 8, IUItem.biochaff);
        addmacerator(new ItemStack(Items.MELON_SEEDS, 16), IUItem.biochaff);
        addmacerator(new ItemStack(Items.POTATO, 8), IUItem.biochaff);
        addmacerator("c:purifiedcrushed/Gold", "c:dusts/Gold", 1);
        addmacerator("c:purifiedcrushed/Iron", "c:dusts/Iron", 1);
        addmacerator("c:purifiedcrushed/Tin", "c:dusts/Tin", 1);
        addmacerator("c:purifiedcrushed/Copper", "c:dusts/Copper", 1);
        addmacerator("c:purifiedcrushed/Lead", "c:dusts/Lead", 1);
        addmacerator(new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT));
        addmacerator(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GLOWSTONE_DUST, 4));
        addmacerator(new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(Items.REDSTONE, 9));
        addmacerator(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.SAND));
        addmacerator(
                new ItemStack(IUItem.energy_crystal.getItem(), 1),
                IUItem.energiumDust, 9
        );
        addmacerator(new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 5));
        addmacerator(new ItemStack(Items.WHEAT, 8), IUItem.biochaff);
        addmacerator(new ItemStack(Blocks.LAPIS_BLOCK), "c:dusts/Lapis", 9);
        addmacerator(new ItemStack(Items.LAPIS_LAZULI), "c:dusts/Lapis", 1);
        addmacerator(new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE));


        for (int i = 0; i < BlockRaws.Type.values().length; i++) {
            addmacerator("c:storage_blocks/" + BlockRaws.Type.values()[i].getName(), "c:crushed/" + BlockRaws.Type.values()[i].name(), 18);
        }
        addmacerator("c:storage_blocks/raw_iron", "c:crushed/iron", 18);
        addmacerator("c:storage_blocks/raw_copper", "c:crushed/copper", 18);
        addmacerator("c:storage_blocks/raw_gold", "c:crushed/gold", 18);
    }

    private static void addmacerator(ItemStack input, String output, int i) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
        stack.setCount(i);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    private static void addmacerator(ItemStack input, String output) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
        stack.setCount(1);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    private static void addmacerator(String input, int i, ItemStack stack) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, i)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addmacerator(String input, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addmacerator(String input, String output) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
        stack.setCount(1);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );


    }

    public static void addmacerator(String input, String output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );


    }

    public static void addmacerator(ItemStack input, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addmacerator1(ItemStack input, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(output)
                        ),
                        new RecipeOutput(null, input)
                )
        );


    }

    public static void addmacerator(ItemStack input, int i, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        input = input.copy();
        input.setCount(i);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addmacerator(Item input, int i, Item output) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack input2 = new ItemStack(input, i);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input2)
                        ),
                        new RecipeOutput(null, new ItemStack(output))
                )
        );


    }

    public static void addmacerator(Item input, Item output) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack input2 = new ItemStack(input, 1);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input2)
                        ),
                        new RecipeOutput(null, new ItemStack(output))
                )
        );


    }

    public static void addmacerator(ItemStack input, int i, ItemStack output, int j) {
        final IInputHandler input1 = Recipes.inputFactory;
        input = input.copy();
        input.setCount(i);
        output = output.copy();
        output.setCount(j);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addmacerator(ItemStack input, ItemStack output, int j) {
        final IInputHandler input1 = Recipes.inputFactory;
        output = output.copy();
        output.setCount(j);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

}
