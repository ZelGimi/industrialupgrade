package com.denfop.recipes;

import com.denfop.IUCore;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.register.RegisterOreDictionary;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import ic2.core.item.type.DustResourceType;
import ic2.core.ref.ItemName;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
        addmacerator(Ic2Items.plateobsidian, Ic2Items.obsidianDust);
        addmacerator(new ItemStack(Blocks.WOOL), new ItemStack(Items.STRING, 2));
        addmacerator("ingotBronze", "dustBronze");
        addmacerator("ingotLead", "dustLead");
        addmacerator("plateDenseLead", "dustLead", 9);
        addmacerator(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.QUARTZ, 4));
        addmacerator(new ItemStack(Items.WHEAT_SEEDS, 16), Ic2Items.biochaff);
        if (IUCore.isHasVersion("ic2", "220")) {
            addmacerator(new ItemStack(Items.EMERALD), ItemName.dust.getItemStack(DustResourceType.emerald));
        }
        addmacerator("crushedCopper", "dustCopper");
        addmacerator("oreGold", "crushedGold", 2);
        addmacerator("plateDenseObsidian", "dustObsidian", 9);
        addmacerator("plateDenseLapis", "dustLapis", 9);
        addmacerator(new ItemStack(Blocks.TALLGRASS, 8), Ic2Items.biochaff);
        addmacerator("ingotCopper", "dustCopper");
        addmacerator(new ItemStack(Items.BONE, 1), new ItemStack(Items.DYE, 4, 15));
        addmacerator(new ItemStack(Items.MELON, 8), Ic2Items.biochaff);
        addmacerator("treeSapling", 4, Ic2Items.biochaff);
        addmacerator("plateDenseIron", "dustIron", 9);
        addmacerator("crushedIron", "dustIron");
        if (IUCore.isHasVersion("ic2", "220")) {
            addmacerator(new ItemStack(Items.ENDER_PEARL), ItemName.dust.getItemStack(DustResourceType.ender_pearl));
        }
        addmacerator(new ItemStack(Items.COAL), "dustCoal");
        addmacerator("crushedLead", "dustLead");
        addmacerator("crushedTin", "dustTin");
        addmacerator("crushedGold", "dustGold");
        addmacerator("plateDenseCopper", "dustCopper", 9);
        addmacerator("plateDenseTin", "dustTin", 9);
        addmacerator("plateDenseGold", "dustGold", 9);
        addmacerator(new ItemStack(Blocks.ICE), new ItemStack(Items.SNOWBALL));
        addmacerator(new ItemStack(Items.PUMPKIN_SEEDS, 16), Ic2Items.biochaff);
        addmacerator("plateLapis", "dustLapis");
        addmacerator("plateDenseBronze", "dustBronze", 9);
        addmacerator("plateSteel", "dustIron");
        addmacerator("plateObsidian", "dustIron");
        if (IUCore.isHasVersion("ic2", "220")) {
            addmacerator(new ItemStack(Blocks.NETHERRACK), ItemName.dust.getItemStack(DustResourceType.netherrack));
        }
        addmacerator(Ic2Items.biochaff, new ItemStack(Blocks.DIRT));
        addmacerator(new ItemStack(Blocks.PUMPKIN, 8), Ic2Items.biochaff);
        addmacerator("gemDiamond", "dustDiamond");
        addmacerator(new ItemStack(Items.RABBIT, 8), Ic2Items.biochaff);
        addmacerator("blockCoal", "dustCoal", 9);
        addmacerator(new ItemStack(Blocks.OBSIDIAN), Ic2Items.obsidianDust);
        addmacerator(new ItemStack(Items.REEDS, 8), Ic2Items.biochaff);
        addmacerator("ingotGold", "dustGold");
        addmacerator("ingotIron", "dustIron");
        addmacerator("ingotTin", "dustTin");
        addmacerator(Ic2Items.plantBall, Ic2Items.biochaff);
        addmacerator(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.SAND));
        addmacerator("treeLeaves", 8, Ic2Items.biochaff);
        addmacerator("oreUranium", "crushedUranium", 2);
        addmacerator(new ItemStack(Items.MELON_SEEDS, 16), Ic2Items.biochaff);
        addmacerator(new ItemStack(Items.POTATO, 8), Ic2Items.biochaff);
        addmacerator("oreIron", "crushedIron", 2);
        addmacerator("oreTin", "crushedTin", 2);
        addmacerator("oreCopper", "crushedCopper", 2);
        addmacerator("oreLead", "crushedLead", 2);
        addmacerator("crushedPurifiedGold", "dustGold", 1);
        addmacerator("crushedPurifiedIron", "dustIron", 1);
        addmacerator("crushedPurifiedTin", "dustTin", 1);
        addmacerator("crushedPurifiedCopper", "dustCopper", 1);
        addmacerator("crushedPurifiedLead", "dustLead", 1);
        addmacerator(new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT));
        addmacerator(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GLOWSTONE_DUST, 4));
        addmacerator(new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(Items.REDSTONE, 9));
        addmacerator(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.SAND));
        addmacerator(
                new ItemStack(Ic2Items.energyCrystal.getItem(), 1, OreDictionary.WILDCARD_VALUE),
                ItemName.dust.getItemStack(DustResourceType.energium), 9
        );
        addmacerator(new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 5, 0));
        addmacerator(new ItemStack(Items.WHEAT, 8), Ic2Items.biochaff);
        addmacerator(new ItemStack(Blocks.LAPIS_BLOCK), "dustLapis", 9);
        addmacerator(new ItemStack(Items.DYE, 1, 4), "dustLapis", 1);
        addmacerator(new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE));

    }

    private static void addmacerator(ItemStack input, String output, int i) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(i);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    private static void addmacerator(ItemStack input, String output) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(1);
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    private static void addmacerator(String input, int i, ItemStack stack) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, i)
                        ),
                        new RecipeOutput(null, stack)
                )
        );
    }

    public static void addmacerator(String input, String output) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(1);
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );


    }

    public static void addmacerator(String input, String output, int n) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(n);
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forOreDict(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );


    }

    public static void addmacerator(ItemStack input, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addmacerator(ItemStack input, int i, ItemStack output) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        input = input.copy();
        input.setCount(i);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addmacerator(ItemStack input, int i, ItemStack output, int j) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        input = input.copy();
        input.setCount(i);
        output = output.copy();
        output.setCount(j);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

    public static void addmacerator(ItemStack input, ItemStack output, int j) {
        final IRecipeInputFactory input1 = Recipes.inputFactory;
        output = output.copy();
        output.setCount(j);
        com.denfop.api.Recipes.recipes.addRecipe(
                "macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );


    }

}
