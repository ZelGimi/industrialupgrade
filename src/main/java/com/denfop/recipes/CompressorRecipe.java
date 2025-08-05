package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blocks.FluidName;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompressorRecipe {
    public static final String[] recipe = {"forge:plates/", "forge:smalldust/", "forge:verysmalldust/"};
    public static final String[] recipe1 = {"forge:doubleplate/", "forge:dusts/", "forge:smalldust/"};
    public static final String[] recipe2 = {"Osmium", "Tantalum", "Cadmium"};

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
        addcompressor(ModUtils.setSize(IUItem.iridiumShard, 9), IUItem.iridiumOre);

        addcompressor(new ItemStack(IUItem.sunnarium.getStack(3)), new ItemStack(IUItem.sunnarium.getStack(2)));
        addcompressor(IUItem.carbonPlate, 9, new ItemStack(IUItem.compresscarbon.getItem()));
        addcompressor(IUItem.advancedAlloy, 9, new ItemStack(IUItem.compressAlloy.getItem()));
        addcompressor(IUItem.iridiumPlate, 4, new ItemStack(IUItem.compressIridiumplate.getItem()));
        addcompressor(ModUtils.getCellFromFluid(FluidName.fluidNeutron.getInstance().get()), 1, new ItemStack(IUItem.neutronium.getItem()));
        addcompressor(new ItemStack(IUItem.compressIridiumplate.getItem()), 9, new ItemStack(IUItem.doublecompressIridiumplate.getItem()));
        addcompressor(new ItemStack(IUItem.neutronium.getItem()), 9, new ItemStack(IUItem.neutroniumingot.getItem(), 1));
        addcompressor(IUItem.coalBall, 1, IUItem.compressedCoalBall);
        addcompressor(IUItem.coal_chunk, 9, new ItemStack(IUItem.coal_chunk1.getItem()));
        addcompressor(new ItemStack(IUItem.smalldust.getStack(49)), 9, new ItemStack(IUItem.iudust.getStack(75)));
        addcompressor(new ItemStack(IUItem.smalldust.getStack(50)), 9, new ItemStack(IUItem.iudust.getStack(77)));
        addcompressor(new ItemStack(IUItem.smalldust.getStack(47)), 9, new ItemStack(IUItem.iudust.getStack(74)));
        addcompressor(new ItemStack(IUItem.smalldust.getStack(24)), 9, new ItemStack(IUItem.iudust.getStack(28)));
        addcompressor(new ItemStack(IUItem.smalldust.getStack(48)), 9, new ItemStack(IUItem.iudust.getStack(76)));

        addcompressor(new ItemStack(IUItem.smalldust.getStack(28)), 9, new ItemStack(IUItem.iudust.getStack(23)));

        for (int i = 0; i < itemNames().size(); i++) {

            addcompressor(
                    "forge:storage_blocks/" + itemNames().get(i),
                    1,
                    "forge:doubleplate/" + itemNames().get(i)
            );


        }
        for (int i = 0; i < itemNames7().size(); i++) {

            addcompressor(
                    "forge:smalldust/" + itemNames7().get(i),
                    9,
                    "forge:dusts/" + itemNames7().get(i)
            );


        }
        for (int i = 0; i < itemNames7().size(); i++) {

            addcompressor(
                    "forge:storage_blocks/" + itemNames7().get(i),
                    1,
                    "forge:doubleplate/" + itemNames7().get(i)
            );


        }
        addcompressor(
                "forge:storage_blocks/Iron",
                1,
                "forge:plateDense/Iron"
        );
        addcompressor(
                "forge:storage_blocks/Gold",
                1,
                "forge:plateDense/Gold"
        );
        addcompressor(
                "forge:storage_blocks/Lapis",
                1,
                "forge:plateDense/Lapis"
        );
        addcompressor(
                "forge:storage_blocks/Bronze",
                1,
                "forge:plateDense/Bronze"
        );
        addcompressor(
                "forge:storage_blocks/Steel",
                1,
                "forge:plateDense/Steel"
        );

        addcompressor(
                "forge:storage_blocks/Copper",
                1,
                "forge:plateDense/Copper"
        );

        addcompressor(
                "forge:storage_blocks/Tin",
                1,
                "forge:plateDense/Tin"
        );
        addcompressor(
                "forge:storage_blocks/Lead",
                1,
                "forge:plateDense/Lead"
        );
        for (int i = 0; i < itemNames1().size(); i++) {

            addcompressor(
                    "forge:storage_blocks/" + itemNames1().get(i),
                    1,
                    "forge:doubleplate/" + itemNames1().get(i)
            );


        }

        addcompressor(
                "forge:storage_blocks/Osmium",
                1,
                "forge:doubleplate/Osmium"
        );
        addcompressor(
                "forge:storage_blocks/Tantalum",
                1,
                "forge:doubleplate/Tantalum"
        );
        addcompressor(
                "forge:storage_blocks/Cadmium",
                1,
                "forge:doubleplate/Cadmium"
        );

        for (String s : recipe2) {
            for (int i = 0; i < recipe1.length; i++) {
                addcompressor(
                        recipe[i] + s,
                        9,
                        recipe1[i] + s
                );

            }
        }
        for (String s : RegisterOreDictionary.spaceElementList) {
            for (int i = 0; i < 1; i++) {
                addcompressor(
                        recipe[i] + s,
                        9,
                        recipe1[i] + s
                );

            }
        }

        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < itemNames().size(); i++) {

                addcompressor(
                        recipe[j] + itemNames().get(i),
                        9,
                        recipe1[j] + itemNames().get(i)
                );

            }
        }
        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < itemNames1().size(); i++) {
                if (j == 0) {
                    addcompressor(
                            recipe[j] + itemNames1().get(i),
                            9,
                            recipe1[j] + itemNames1().get(i)
                    );
                }

            }
        }

        addcompressor(new ItemStack(IUItem.iudust.getStack(78), 1), 1, new ItemStack(IUItem.itemiu.getStack(2), 2));
        addcompressor(IUItem.crushedUraniumOre, 1, new ItemStack(IUItem.itemiu.getStack(2), 1));
        addcompressor(
                "forge:plates/Copper",
                9,
                "forge:plateDense/Copper"
        );
        addcompressor(
                new ItemStack(Items.SNOWBALL, 4),
                new ItemStack(Blocks.SNOW)
        );
        addcompressor(
                new ItemStack(Items.NETHER_BRICK, 4),
                new ItemStack(Blocks.NETHER_BRICKS)
        );
        addcompressor(
                "forge:smalldust/Sulfur",
                9,
                "forge:dusts/Sulfur"
        );
        addcompressor(
                "forge:smalldust/Iron",
                9,
                "forge:dusts/Iron"
        );
        addcompressor(
                new ItemStack(Items.REDSTONE, 9),
                new ItemStack(Blocks.REDSTONE_BLOCK)
        );
        addcompressor(
                new ItemStack(Items.BLAZE_POWDER, 5),
                new ItemStack(Items.BLAZE_ROD)
        );
        addcompressor(
                new ItemStack(Items.BRICK, 4),
                new ItemStack(Blocks.BRICKS)
        );
        addcompressor(
                IUItem.fluidCell.getItem(),
                ModUtils.getCellFromFluid(FluidName.fluidair.getInstance().get())
        );

        addcompressor(
                "forge:smalldust/Gold",
                9,
                "forge:dusts/Gold"
        );
        addcompressor(
                "forge:plates/Bronze",
                9,
                "forge:plateDense/Bronze"
        );

        addcompressor(
                "forge:plates/Steel",
                9,
                "forge:plateDense/Steel"
        );
        addcompressor(
                "forge:plates/Lead",
                9,
                "forge:plateDense/Lead"
        );

        addcompressor(
                new ItemStack(Items.LAPIS_LAZULI, 9),
                new ItemStack(Blocks.LAPIS_BLOCK)
        );
        addcompressor(
                new ItemStack(Blocks.ICE, 2),
                new ItemStack(Blocks.PACKED_ICE)
        );
        addcompressor(
                "forge:plates/Iron",
                9,
                "forge:plateDense/Iron"
        );

        addcompressor(
                IUItem.mixedMetalIngot,
                IUItem.advancedAlloy
        );
        addcompressor(
                "forge:smalldust/Tin",
                9,
                "forge:dusts/Tin"
        );
        addcompressor(
                "forge:smalldust/Copper",
                9,
                "forge:dusts/Copper"
        );
        addcompressor(
                "forge:plates/Obsidian",
                9,
                "forge:plateDense/Obsidian"
        );
        addcompressor(
                "forge:dusts/Lapis",
                9,
                "forge:plateDense/Lapis"
        );
        addcompressor(
                "forge:ingots/Steel",
                9,
                "forge:storage_blocks/Steel"
        );
        addcompressor(
                "forge:ingots/Tin",
                9,
                "forge:storage_blocks/Tin"
        );
        addcompressor(
                IUItem.smallPlutonium,
                9,
                IUItem.Plutonium
        );
        addcompressor(
                "forge:ingots/Lead",
                9,
                "forge:storage_blocks/Lead"
        );
        addcompressor(
                "forge:ingots/Gold",
                9,
                "forge:storage_blocks/Gold"
        );

        addcompressor(
                "forge:dusts/Obsidian",
                1,
                "forge:plates/Obsidian"
        );
        addcompressor(
                "forge:ingots/Copper",
                9,
                "forge:storage_blocks/Copper"
        );
        addcompressor(
                new ItemStack(Items.CLAY_BALL),
                4,
                new ItemStack(Blocks.CLAY)
        );
        addcompressor(
                "forge:plates/Lapis",
                9,
                "forge:plateDense/Lapis"
        );
        addcompressor(
                "forge:plates/Tin",
                9,
                "forge:plateDense/Tin"
        );
        addcompressor(
                "forge:plates/Gold",
                9,
                "forge:plateDense/Gold"
        );
        addcompressor(
                IUItem.energiumDust,
                9,
                new ItemStack(IUItem.energy_crystal.getItem())
        );
        addcompressor(
                new ItemStack(Items.GLOWSTONE_DUST),
                4,
                new ItemStack(Blocks.GLOWSTONE)
        );
        addcompressor(
                "forge:ingots/Bronze",
                9,
                "forge:storage_blocks/Bronze"
        );
        addcompressor(
                new ItemStack(Blocks.SNOW),
                1,
                new ItemStack(Blocks.ICE)
        );

        addcompressor(
                IUItem.carbonMesh,
                1,
                IUItem.carbonPlate
        );
        addcompressor1(
                CropNetwork.instance.getCrop(0).getStack(),
                new ItemStack(Items.WHEAT_SEEDS)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(17).getStack(),
                CropNetwork.instance.getCrop(17).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(1).getStack(),
                new ItemStack(Items.SUGAR_CANE)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(4).getStack(),
                CropNetwork.instance.getCrop(4).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(81).getStack(),
                CropNetwork.instance.getCrop(81).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(5).getStack(),
                CropNetwork.instance.getCrop(5).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(6).getStack(),
                CropNetwork.instance.getCrop(6).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(7).getStack(),
                CropNetwork.instance.getCrop(7).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(8).getStack(),
                CropNetwork.instance.getCrop(8).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(9).getStack(),
                CropNetwork.instance.getCrop(9).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(10).getStack(),
                CropNetwork.instance.getCrop(10).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(11).getStack(),
                CropNetwork.instance.getCrop(11).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(12).getStack(),
                CropNetwork.instance.getCrop(12).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(13).getStack(),
                CropNetwork.instance.getCrop(13).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(14).getStack(),
                CropNetwork.instance.getCrop(14).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(15).getStack(),
                CropNetwork.instance.getCrop(15).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(18).getStack(),
                CropNetwork.instance.getCrop(18).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(19).getStack(),
                CropNetwork.instance.getCrop(19).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(23).getStack(),
                CropNetwork.instance.getCrop(23).getDrop().get(0)
        );
        addcompressor1(
                CropNetwork.instance.getCrop(24).getStack(),
                CropNetwork.instance.getCrop(24).getDrop().get(0)
        );
    }

    public static void addcompressor(Item input, int n, Item output) {
        ItemStack input2 = new ItemStack(input, n);
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input2)
                        ),
                        new RecipeOutput(null, new ItemStack(output))
                )
        );


    }
    public static void addcompressor1(ItemStack output, ItemStack input) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }
    public static void addcompressor(ItemStack input, int n, ItemStack output) {
        input = input.copy();
        input.setCount(n);
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addcompressor(String input, int n, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, n)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addcompressor(String input, int n, String output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, n)
                        ),
                        new RecipeOutput(null, input1.getInput(output).getInputs().get(0))
                )
        );
    }

    public static void addcompressor(ItemStack input, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addcompressor(Item input, Item output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, new ItemStack(output))
                )
        );
    }

    public static void addcompressor(Item input, ItemStack output) {
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }
}
