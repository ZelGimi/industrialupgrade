package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.items.resource.ItemRawMetals;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class MaceratorRecipe {

    public static final String[] recipe = {"raw", "ingot"};
    public static final String[] recipe1 = {"crushed", "dust"};
    public static final int[] number = {2, 1};

    public static void recipe() {


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
        for (int j = 0; j < recipe.length; j++) {
            for (int i = 0; i < RegisterOreDictionary.list_baseore1.size(); i++) {
                addmacerator(
                        recipe[j] + RegisterOreDictionary.list_baseore1.get(i),
                        recipe1[j] + RegisterOreDictionary.list_baseore1.get(i),
                        number[j]
                );

            }
        }

        addmacerator(IUItem.plateobsidian, IUItem.obsidianDust);


        addmacerator("rawOsmium", "crushedOsmium",2);
        addmacerator("rawTantalum", "crushedTantalum",2);
        addmacerator("rawCadmium", "crushedCadmium",2);
        addmacerator("rawTin", "crushedTin",2);
        addmacerator("rawIron", "crushedIron",2);
        addmacerator("rawGold", "crushedGold",2);
        addmacerator("rawCopper", "crushedCopper",2);
        addmacerator("rawLead", "crushedLead",2);
        addmacerator("ingotOsmium", "dustOsmium");
        addmacerator("ingotTantalum", "dustTantalum");
        addmacerator("ingotCadmium", "dustCadmium");
        addmacerator("ingotElectrum", "dustElectrum");
        addmacerator("ingotInvar", "dustInvar");

        addmacerator("ingotBronze", "dustBronze");
        addmacerator("ingotLead", "dustLead");
        addmacerator("plateDenseLead", "dustLead", 9);
        addmacerator(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.QUARTZ, 4));
        addmacerator(new ItemStack(Items.FLINT, 2), new ItemStack(IUItem.iudust, 1, 60));
        addmacerator(new ItemStack(Items.WHEAT_SEEDS, 16), IUItem.biochaff);
        addmacerator(new ItemStack(IUItem.crops, 16), IUItem.biochaff);
        addmacerator(new ItemStack(Blocks.CLAY), "dustClay");
        addmacerator("crushedCopper", "dustCopper");
        addmacerator("plateDenseObsidian", "dustObsidian", 9);
        addmacerator("plateDenseLapis", "dustLapis", 9);
        addmacerator(new ItemStack(Blocks.TALLGRASS, 8), IUItem.biochaff);
        addmacerator("ingotCopper", "dustCopper");
        addmacerator(new ItemStack(Items.BONE, 1), new ItemStack(Items.DYE, 4, 15));
        addmacerator(new ItemStack(Items.MELON, 8), IUItem.biochaff);
        addmacerator("treeSapling", 4, IUItem.biochaff);
        addmacerator("plateDenseIron", "dustIron", 9);
        addmacerator("crushedIron", "dustIron");

        addmacerator(new ItemStack(IUItem.apatite, 1, 2), new ItemStack(IUItem.iudust, 1, 66));
        addmacerator(new ItemStack(IUItem.apatite, 1, 3), new ItemStack(IUItem.iudust, 1, 69));
        addmacerator(new ItemStack(IUItem.apatite, 1, 4), new ItemStack(IUItem.iudust, 1, 70));


        addmacerator(new ItemStack(Items.COAL), "dustCoal");
        addmacerator("crushedLead", "dustLead");
        addmacerator("crushedTin", "dustTin");
        addmacerator("crushedGold", "dustGold");
        addmacerator("plateDenseCopper", "dustCopper", 9);
        addmacerator("plateDenseTin", "dustTin", 9);
        addmacerator("plateDenseGold", "dustGold", 9);
        addmacerator(new ItemStack(Blocks.ICE), new ItemStack(Items.SNOWBALL));
        addmacerator(new ItemStack(Items.PUMPKIN_SEEDS, 16), IUItem.biochaff);
        addmacerator("plateLapis", "dustLapis");
        addmacerator("plateDenseBronze", "dustBronze", 9);

        addmacerator(IUItem.biochaff, new ItemStack(Blocks.DIRT));
        addmacerator(new ItemStack(Blocks.PUMPKIN, 8), IUItem.biochaff);
        addmacerator("gemDiamond", "dustDiamond");
        addmacerator(new ItemStack(Items.RABBIT, 8), IUItem.biochaff);
        addmacerator("blockCoal", "dustCoal", 9);
        addmacerator(new ItemStack(Blocks.OBSIDIAN), IUItem.obsidianDust);
        addmacerator(new ItemStack(Items.REEDS, 8), IUItem.biochaff);
        addmacerator("ingotGold", "dustGold");
        addmacerator("ingotIron", "dustIron");
        addmacerator("ingotTin", "dustTin");
        addmacerator(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.SAND));
        addmacerator("treeLeaves", 8, IUItem.biochaff);
        addmacerator(new ItemStack(Items.MELON_SEEDS, 16), IUItem.biochaff);
        addmacerator(new ItemStack(Items.POTATO, 8), IUItem.biochaff);
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
                new ItemStack(IUItem.energy_crystal, 1, OreDictionary.WILDCARD_VALUE),
                IUItem.energiumDust, 9
        );
        addmacerator(new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 5, 0));
        addmacerator(new ItemStack(Items.WHEAT, 8), IUItem.biochaff);
        addmacerator(new ItemStack(Blocks.LAPIS_BLOCK), "dustLapis", 9);
        addmacerator(new ItemStack(Items.DYE, 1, 4), "dustLapis", 1);
        addmacerator(new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE));

    }

    private static void addmacerator(ItemStack input, String output, int i) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
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
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(1);
        final IInputHandler input1 = Recipes.inputFactory;
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

    public static void addmacerator(String input, String output) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(1);
        final IInputHandler input1 = Recipes.inputFactory;
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
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        stack.setCount(n);
        final IInputHandler input1 = Recipes.inputFactory;
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
