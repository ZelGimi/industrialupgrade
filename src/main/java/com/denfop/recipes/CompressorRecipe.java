package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blocks.FluidName;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CompressorRecipe {

    public static final String[] recipe = {"plate", "smalldust", "verysmalldust"};
    public static final String[] recipe1 = {"doubleplate", "dust", "smalldust"};
    public static final String[] recipe2 = {"Osmium", "Tantalum", "Cadmium"};

    public static void recipe() {
        addcompressor(ModUtils.setSize(IUItem.iridiumShard, 9), IUItem.iridiumOre);

        addcompressor(new ItemStack(IUItem.sunnarium, 1, 3), new ItemStack(IUItem.sunnarium, 1, 2));
        addcompressor(IUItem.carbonPlate, 9, new ItemStack(IUItem.compresscarbon));
        addcompressor(IUItem.advancedAlloy, 9, new ItemStack(IUItem.compressAlloy));
        addcompressor(IUItem.iridiumPlate, 4, new ItemStack(IUItem.compressIridiumplate));
        addcompressor(ModUtils.getCellFromFluid(FluidName.fluidNeutron.getInstance()), 1, new ItemStack(IUItem.neutronium));
        addcompressor(new ItemStack(IUItem.compressIridiumplate), 9, new ItemStack(IUItem.doublecompressIridiumplate));
        addcompressor(new ItemStack(IUItem.neutronium), 9, new ItemStack(IUItem.neutroniumingot, 1));
        addcompressor(IUItem.coalBall, 1, IUItem.compressedCoalBall);
        addcompressor(IUItem.coal_chunk, 9, new ItemStack(IUItem.coal_chunk1)

        );
        addcompressor(new ItemStack(IUItem.smalldust, 1, 49), 9, new ItemStack(IUItem.iudust, 1, 75));
        addcompressor(new ItemStack(IUItem.smalldust, 1, 50), 9, new ItemStack(IUItem.iudust, 1, 77));
        addcompressor(new ItemStack(IUItem.smalldust, 1, 47), 9, new ItemStack(IUItem.iudust, 1, 74));
        addcompressor(new ItemStack(IUItem.smalldust, 1, 24), 9, new ItemStack(IUItem.iudust, 1, 28));
        addcompressor(new ItemStack(IUItem.smalldust, 1, 28), 9, new ItemStack(IUItem.iudust, 1, 23));
        addcompressor(new ItemStack(IUItem.smalldust, 1, 48), 9, new ItemStack(IUItem.iudust, 1, 76));

        for (int i = 0; i < RegisterOreDictionary.itemNames().size(); i++) {

            addcompressor(
                    "block" + RegisterOreDictionary.itemNames().get(i),
                    1,
                    "doubleplate" + RegisterOreDictionary.itemNames().get(i)
            );


        }
        for (int i = 0; i < RegisterOreDictionary.list_baseore1.size(); i++) {

            addcompressor(
                    "smalldust" + RegisterOreDictionary.list_baseore1.get(i),
                    9,
                    "dust" + RegisterOreDictionary.list_baseore1.get(i)
            );


        }
        for (int i = 0; i < RegisterOreDictionary.list_baseore1.size(); i++) {

            addcompressor(
                    "block" + RegisterOreDictionary.list_baseore1.get(i),
                    1,
                    "doubleplate" + RegisterOreDictionary.list_baseore1.get(i)
            );


        }
        addcompressor(
                "blockIron",
                1,
                "plateDenseIron"
        );
        addcompressor(
                "blockGold",
                1,
                "plateDenseGold"
        );
        addcompressor(
                "blockLapis",
                1,
                "plateDenseLapis"
        );
        addcompressor(
                "blockBronze",
                1,
                "plateDenseBronze"
        );
        addcompressor(
                "blockSteel",
                1,
                "plateDenseSteel"
        );

        addcompressor(
                "blockCopper",
                1,
                "plateDenseCopper"
        );

        addcompressor(
                "blockTin",
                1,
                "plateDenseTin"
        );
        addcompressor(
                "blockLead",
                1,
                "plateDenseLead"
        );
        for (int i = 0; i < RegisterOreDictionary.itemNames1().size(); i++) {

            addcompressor(
                    "block" + RegisterOreDictionary.itemNames1().get(i),
                    1,
                    "doubleplate" + RegisterOreDictionary.itemNames1().get(i)
            );


        }

        addcompressor(
                "blockOsmium",
                1,
                "doubleplateOsmium"
        );
        addcompressor(
                "blockTantalum",
                1,
                "doubleplateTantalum"
        );
        addcompressor(
                "blockCadmium",
                1,
                "doubleplateCadmium"
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
            for (int i = 0; i < RegisterOreDictionary.itemNames().size(); i++) {

                addcompressor(
                        recipe[j] + RegisterOreDictionary.itemNames().get(i),
                        9,
                        recipe1[j] + RegisterOreDictionary.itemNames().get(i)
                );

            }
        }
        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < RegisterOreDictionary.itemNames1().size(); i++) {
                if (j == 0) {
                    addcompressor(
                            recipe[j] + RegisterOreDictionary.itemNames1().get(i),
                            9,
                            recipe1[j] + RegisterOreDictionary.itemNames1().get(i)
                    );
                }

            }
        }

        addcompressor(new ItemStack(IUItem.iudust, 1, 78), 1, new ItemStack(IUItem.itemiu, 2, 2));
        addcompressor(IUItem.crushedUraniumOre, 1, new ItemStack(IUItem.itemiu, 1, 2));
        addcompressor(
                "plateCopper",
                9,
                "plateDenseCopper"
        );
        addcompressor(
                new ItemStack(Items.SNOWBALL, 4),
                new ItemStack(Blocks.SNOW)
        );
        addcompressor(
                new ItemStack(Items.NETHERBRICK, 4),
                new ItemStack(Blocks.NETHER_BRICK)
        );
        addcompressor(
                "dustTinySulfur",
                9,
                "dustSulfur"
        );
        addcompressor(
                "dustTinyIron",
                9,
                "dustIron"
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
                new ItemStack(Blocks.BRICK_BLOCK)
        );
        addcompressor(
                IUItem.FluidCell,
                ModUtils.getCellFromFluid(FluidName.fluidair.getInstance())
        );

        addcompressor(
                "dustTinyGold",
                9,
                "dustGold"
        );
        addcompressor(
                "plateBronze",
                9,
                "plateDenseBronze"
        );

        addcompressor(
                "plateSteel",
                9,
                "plateDenseSteel"
        );
        addcompressor(
                "plateLead",
                9,
                "plateDenseLead"
        );

        addcompressor(
                new ItemStack(Items.DYE, 9, 4),
                new ItemStack(Blocks.LAPIS_BLOCK)
        );
        addcompressor(
                new ItemStack(Blocks.ICE, 2),
                new ItemStack(Blocks.PACKED_ICE)
        );
        addcompressor(
                "plateIron",
                9,
                "plateDenseIron"
        );

        addcompressor(
                IUItem.mixedMetalIngot,
                IUItem.advancedAlloy
        );
        addcompressor(
                "dustTinyTin",
                9,
                "dustTin"
        );
        addcompressor(
                "dustTinyCopper",
                9,
                "dustCopper"
        );
        addcompressor(
                "plateObsidian",
                9,
                "plateDenseObsidian"
        );
        addcompressor(
                "dustLapis",
                9,
                "plateDenseLapis"
        );
        addcompressor(
                "dustLapis",
                1,
                "plateLapis"
        );
        addcompressor(
                "ingotSteel",
                9,
                "blockSteel"
        );
        addcompressor(
                "ingotTin",
                9,
                "blockTin"
        );
        addcompressor(
                IUItem.smallPlutonium,
                9,
                IUItem.Plutonium
        );
        addcompressor(
                "ingotLead",
                9,
                "blockLead"
        );
        addcompressor(
                "ingotGold",
                9,
                "blockGold"
        );
        addcompressor(
                "ingotGold",
                9,
                "blockGold"
        );

        addcompressor(
                "dustObsidian",
                1,
                "plateObsidian"
        );
        addcompressor(
                "ingotCopper",
                9,
                "blockCopper"
        );
        addcompressor(
                new ItemStack(Items.CLAY_BALL),
                4,
                new ItemStack(Blocks.CLAY)
        );
        addcompressor(
                "plateLapis",
                9,
                "plateDenseLapis"
        );
        addcompressor(
                "plateTin",
                9,
                "plateDenseTin"
        );
        addcompressor(
                "plateGold",
                9,
                "plateDenseGold"
        );
        addcompressor(
                IUItem.energiumDust,
                9,
                new ItemStack(IUItem.energy_crystal)
        );
        addcompressor(
                new ItemStack(Items.GLOWSTONE_DUST),
                4,
                new ItemStack(Blocks.GLOWSTONE)
        );
        addcompressor(
                "ingotBronze",
                9,
                "blockBronze"
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
                        new RecipeOutput(null, OreDictionary.getOres(output).get(0))
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

}
