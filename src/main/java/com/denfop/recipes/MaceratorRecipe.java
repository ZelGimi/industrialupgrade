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

    public static final String[] recipe = {"forge:raw_materials/", "forge:ingots/"};
    public static final String[] recipe1 = {"forge:crushed/", "forge:dusts/"};
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


        addmacerator("wool", "forge:string", 4);
        addmacerator("forge:raw_materials/Osmium", "forge:crushed/Osmium", 2);
        addmacerator("forge:dusts/Osmium", "forge:smalldust/Osmium", 9);
        addmacerator("forge:dusts/Platinum", "forge:smalldust/Platinum", 9);
        addmacerator("forge:raw_materials/Tantalum", "forge:crushed/Tantalum", 2);
        addmacerator("forge:raw_materials/Cadmium", "forge:crushed/Cadmium", 2);
        addmacerator("forge:raw_materials/Tin", "forge:crushed/Tin", 2);
        addmacerator("forge:raw_materials/Iron", "forge:crushed/Iron", 2);
        addmacerator("forge:raw_materials/Gold", "forge:crushed/Gold", 2);
        addmacerator("forge:raw_materials/Copper", "forge:crushed/Copper", 2);
        addmacerator("forge:raw_materials/Lead", "forge:crushed/Lead", 2);
        addmacerator("forge:ingots/Osmium", "forge:dusts/Osmium");
        addmacerator("forge:ingots/Tantalum", "forge:dusts/Tantalum");
        addmacerator("forge:ingots/Cadmium", "forge:dusts/Cadmium");
        addmacerator("forge:ingots/Electrum", "forge:dusts/Electrum");
        addmacerator("forge:ingots/Invar", "forge:dusts/Invar");
        addmacerator("forge:gems/Quartz", "forge:dusts/Quartz");
        for (int i = 0; i < BlockRaws.Type.values().length; i++) {
            addmacerator("forge:storage_blocks/"+BlockRaws.Type.values()[i].getName(), "forge:crushed/"+BlockRaws.Type.values()[i].name(),18);
        }
        addmacerator("forge:storage_blocks/raw_iron", "forge:crushed/iron",18);
        addmacerator("forge:storage_blocks/raw_copper", "forge:crushed/copper",18);
        addmacerator("forge:storage_blocks/raw_gold", "forge:crushed/gold",18);

        addmacerator("forge:ingots/Bronze", "forge:dusts/Bronze");
        addmacerator("forge:ingots/Lead", "forge:dusts/Lead");
        addmacerator("forge:plateDense/Lead", "forge:dusts/Lead", 9);
        addmacerator(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.QUARTZ, 4));
        addmacerator(new ItemStack(Items.FLINT, 2), new ItemStack(IUItem.iudust.getStack(60), 1));
        addmacerator(new ItemStack(Items.WHEAT_SEEDS, 16), IUItem.biochaff);
        addmacerator(new ItemStack(IUItem.crops.getStack(0), 16), IUItem.biochaff);
        addmacerator(new ItemStack(Blocks.CLAY), "forge:dusts/Clay");
        addmacerator("forge:crushed/Copper", "forge:dusts/Copper");
        addmacerator("forge:plateDense/Obsidian", "forge:dusts/Obsidian", 9);
        addmacerator("forge:plateDense/Lapis", "forge:dusts/Lapis", 9);
        addmacerator(new ItemStack(Blocks.TALL_GRASS, 8), IUItem.biochaff);
        addmacerator("forge:ingots/Copper", "forge:dusts/Copper");
        addmacerator(new ItemStack(Items.BONE, 1), new ItemStack(Items.WHITE_DYE, 4));
        addmacerator(new ItemStack(Items.MELON, 8), IUItem.biochaff);
        addmacerator("saplings", 4, IUItem.biochaff);
        addmacerator("forge:plateDense/Iron", "forge:dusts/Iron", 9);
        addmacerator("forge:crushed/Iron", "forge:dusts/Iron");
        addmacerator("forge:gems/Emerald", new ItemStack(IUItem.iudust.getStack(74), 1));

        addmacerator(new ItemStack(IUItem.apatite.getItem(2), 1), new ItemStack(IUItem.iudust.getStack(66), 1));
        addmacerator(new ItemStack(IUItem.apatite.getItem(3), 1), new ItemStack(IUItem.iudust.getStack(69), 1));
        addmacerator(new ItemStack(IUItem.apatite.getItem(4), 1), new ItemStack(IUItem.iudust.getStack(70), 1));


        addmacerator(new ItemStack(Items.COAL), "forge:dusts/Coal");
        addmacerator("forge:crushed/Lead", "forge:dusts/Lead");
        addmacerator("forge:crushed/Tin", "forge:dusts/Tin");
        addmacerator("forge:crushed/Gold", "forge:dusts/Gold");
        addmacerator("forge:plateDense/Copper", "forge:dusts/Copper", 9);
        addmacerator("forge:plateDense/Tin", "forge:dusts/Tin", 9);
        addmacerator("forge:plateDense/Gold", "forge:dusts/Gold", 9);
        addmacerator(new ItemStack(Blocks.ICE), new ItemStack(Items.SNOWBALL));
        addmacerator(new ItemStack(Items.PUMPKIN_SEEDS, 16), IUItem.biochaff);
        addmacerator("forge:plates/Lapis", "forge:dusts/Lapis");
        addmacerator("forge:plateDense/Bronze", "forge:dusts/Bronze", 9);

        addmacerator(IUItem.biochaff, new ItemStack(Blocks.DIRT));
        addmacerator(new ItemStack(Blocks.PUMPKIN, 8), IUItem.biochaff);
        addmacerator("forge:gems/Diamond", "forge:dusts/Diamond");
        addmacerator(new ItemStack(Items.RABBIT, 8), IUItem.biochaff);
        addmacerator("forge:storage_blocks/Coal", "forge:dusts/Coal", 9);
        addmacerator(new ItemStack(Blocks.OBSIDIAN), IUItem.obsidianDust);
        addmacerator(new ItemStack(Items.SUGAR_CANE, 8), IUItem.biochaff);
        addmacerator("forge:ingots/Gold", "forge:dusts/Gold");
        addmacerator("forge:ingots/Iron", "forge:dusts/Iron");
        addmacerator("forge:ingots/Tin", "forge:dusts/Tin");
        addmacerator(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.SAND));
        addmacerator("leaves", 8, IUItem.biochaff);
        addmacerator(new ItemStack(Items.MELON_SEEDS, 16), IUItem.biochaff);
        addmacerator(new ItemStack(Items.POTATO, 8), IUItem.biochaff);
        addmacerator("forge:purifiedcrushed/Gold", "forge:dusts/Gold", 1);
        addmacerator("forge:purifiedcrushed/Iron", "forge:dusts/Iron", 1);
        addmacerator("forge:purifiedcrushed/Tin", "forge:dusts/Tin", 1);
        addmacerator("forge:purifiedcrushed/Copper", "forge:dusts/Copper", 1);
        addmacerator("forge:purifiedcrushed/Lead", "forge:dusts/Lead", 1);
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
        addmacerator(new ItemStack(Blocks.LAPIS_BLOCK), "forge:dusts/Lapis", 9);
        addmacerator(new ItemStack(Items.LAPIS_LAZULI), "forge:dusts/Lapis", 1);
        addmacerator(new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE));

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
