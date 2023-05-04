package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CompressorRecipe {

    public static final String[] recipe = {"plate", "smalldust", "verysmalldust"};
    public static final String[] recipe1 = {"doubleplate", "dust", "smalldust"};

    public static void recipe() {
        final Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipe3 = Recipes.compressor.getRecipes();
        final Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iter1 = recipe3.iterator();
        while (iter1.hasNext()) {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe2 = iter1.next();
            List<ItemStack> list = (List<ItemStack>) recipe2.getOutput();
            if (list.get(0).isItemEqual(Ic2Items.silverBlock)) {
                iter1.remove();
            }
            if (list.get(0).isItemEqual(Ic2Items.iridiumOre)) {
                iter1.remove();
            }
        }

        addcompressor(new ItemStack(IUItem.sunnarium, 1, 3), new ItemStack(IUItem.sunnarium, 1, 2));
        addcompressor(Ic2Items.carbonPlate, 9, new ItemStack(IUItem.compresscarbon));
        addcompressor(Ic2Items.advancedAlloy, 9, new ItemStack(IUItem.compresscarbonultra));
        addcompressor(Ic2Items.iridiumPlate, 4, new ItemStack(IUItem.compressIridiumplate));
        addcompressor(new ItemStack(IUItem.cell_all, 1, 1), 1, new ItemStack(IUItem.neutronium));
        addcompressor(new ItemStack(IUItem.compressIridiumplate), 9, new ItemStack(IUItem.doublecompressIridiumplate));
        addcompressor("doubleplateTungsten", 1, new ItemStack(IUItem.cell_all));
        addcompressor(new ItemStack(IUItem.neutronium), 9, new ItemStack(IUItem.neutroniumingot, 1));
        addcompressor(new ItemStack(Ic2Items.cell.getItem()), 1, Ic2Items.airCell);

        for (int i = 0; i < RegisterOreDictionary.itemNames().size(); i++) {

            addcompressor(
                    "block" + RegisterOreDictionary.itemNames().get(i),
                    1,
                    "doubleplate" + RegisterOreDictionary.itemNames().get(i)
            );


        }
        for (int i = 0; i < RegisterOreDictionary.itemNames1().size(); i++) {

            addcompressor(
                    "block" + RegisterOreDictionary.itemNames1().get(i),
                    1,
                    "doubleplate" + RegisterOreDictionary.itemNames1().get(i)
            );


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
        addcompressor(Ic2Items.uraniumOre, 1, new ItemStack(IUItem.itemiu, 1, 2));
        addcompressor(Ic2Items.crushedUraniumOre, 1, new ItemStack(IUItem.itemiu, 1, 2));
        addcompressor(
                "plateCopper",
                9,
                "plateDenseCopper"
        );
        addcompressor(
                new ItemStack(Items.SNOWBALL, 4, 1),
                new ItemStack(Blocks.SNOW)
        );
        addcompressor(
                new ItemStack(Items.NETHERBRICK, 4, 1),
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
                new ItemStack(Items.REDSTONE, 9, 1),
                new ItemStack(Blocks.REDSTONE_BLOCK)
        );
        addcompressor(
                "dustTinyObsidian",
                9,
                "dustObsidian"
        );
        addcompressor(
                new ItemStack(Items.BLAZE_POWDER, 5, 1),
                new ItemStack(Items.BLAZE_ROD)
        );
        addcompressor(
                new ItemStack(Items.BRICK, 4, 1),
                new ItemStack(Blocks.BRICK_BLOCK)
        );
        addcompressor(
                Ic2Items.FluidCell,
                ModUtils.getCellFromFluid("ic2air")
        );
        addcompressor(
                "dustTinyBronze",
                9,
                "dustBronze"
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
                "dustTinyLapis",
                9,
                "dustLapis"
        );
        addcompressor(
                new ItemStack(Items.DYE, 4, 9),
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
                Ic2Items.RTGPellets,
                Ic2Items.Uran235
        );
        addcompressor(
                Ic2Items.mixedMetalIngot,
                Ic2Items.advancedAlloy
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
                1,
                "plateLapis"
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
                Ic2Items.smallPlutonium,
                9,
                Ic2Items.Plutonium
        );
        addcompressor(
                "dustTinyLithium",
                9,
                "dustLithium"
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
                Ic2Items.coal_chunk,
                1,
                new ItemStack(Items.DIAMOND)
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
                Ic2Items.energiumDust,
                9,
                Ic2Items.energyCrystal
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
                Ic2Items.coalBall,
                1,
                Ic2Items.compressedCoalBall
        );
        addcompressor(
                Ic2Items.carbonMesh,
                1,
                Ic2Items.carbonPlate
        );
    }


    public static void addcompressor(ItemStack input, int n, ItemStack output) {

        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input, n)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addcompressor(String input, int n, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, n)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addcompressor(String input, int n, String output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, n)
                        ),
                        new RecipeOutput(null, OreDictionary.getOres(output).get(0))
                )
        );
    }

    public static void addcompressor(ItemStack input, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input, 1)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

}
