package com.denfop.api.recipe;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static com.denfop.register.RegisterOreDictionary.list_baseore1;
import static com.denfop.register.RegisterOreDictionary.list_string;

public class ReplicatorRecipe {

    public static void init() {
        add(Items.IRON_INGOT, 1.066);
        add(Items.COAL, 0.9144);
        add(IUItem.bronzeIngot, 0.9611);
        add("forge:ingots/Tin", 1.082);
        add("forge:ingots/Steel", 1.066);
        add("forge:ingots/Copper", 0.9174);
        add("forge:ingots/Silver", 79.25);
        add(IUItem.rubber, 100.7);
        add(Items.REDSTONE, 1.221);
        add(Items.GLOWSTONE_DUST, 39.94);
        add(Items.LAPIS_LAZULI, 6.633);
        add(Blocks.GLASS, 0.29);
        add(Items.DIAMOND, 44.41);
        add(Blocks.COBBLESTONE, 0.010);
        add(Blocks.SAND, 0.15);
        add(Items.CLAY_BALL, 23.08);
        add(Items.GOLD_INGOT, 8.456);
        add("forge:ingots/Lead", 5.576);
        add(Blocks.STONE, 0.150);
        add(Blocks.GRASS, 26.35);
        add(Blocks.DIRT, 0.148);
        add(Blocks.GRAVEL, 0.527);
        add(Blocks.GLASS, 0.29);
        add(Blocks.GLASS_PANE, 0.109);
        add(Blocks.SANDSTONE, 0.61);
        add(new ItemStack(Blocks.RED_SAND), 266);
        add(new ItemStack(Blocks.CHISELED_SANDSTONE), 0.623);
        add(new ItemStack(Blocks.SMOOTH_SANDSTONE), 0.612);
        add(Blocks.BRICKS, 92.9);
        add(Blocks.MOSSY_COBBLESTONE, 259.6);
        add(Blocks.ICE, 30.04);
        add(Blocks.SNOW, 11.6);
        add(Blocks.CLAY, 92.34);
        add(Blocks.NETHERRACK, 40.29);
        add(Blocks.SOUL_SAND, 80.57);
        add(Blocks.GLOWSTONE, 159.8);
        add(Blocks.STONE_BRICKS, 0.152);
        add(Blocks.NETHERRACK, 161.7);
        add(Blocks.BLACK_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.BLUE_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.BROWN_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.GRAY_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.CYAN_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.GREEN_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.ORANGE_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.LIME_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.MAGENTA_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.PINK_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.RED_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.YELLOW_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.PURPLE_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.WHITE_GLAZED_TERRACOTTA, 8.645);
        add(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 8.645);
        add(Items.FLINT, 0.667);
        add(Items.SNOWBALL, 7.472);
        add(Items.BRICK, 23.22);
        add(Items.NETHER_BRICK, 40.43);
        add(Blocks.GOLD_ORE, 16.49);
        add(Blocks.IRON_ORE, 1.711);
        add(Blocks.LAPIS_BLOCK, 59.7);
        add(Blocks.GOLD_BLOCK, 76.11);
        add(Blocks.IRON_BLOCK, 9.601);
        add(Blocks.DIAMOND_BLOCK, 399.7);
        add(Blocks.EMERALD_BLOCK, 3616);
        add(Blocks.REDSTONE_BLOCK, 11);
        add(Blocks.COPPER_ORE, 1.415);
        add(IUItem.tinOre, 1.744);
        add(IUItem.uraniumOre, 22.26);
        add(IUItem.leadOre, 10.73);
        add(Blocks.COAL_BLOCK, 8.24);
        add(IUItem.copperBlock, 8.266);
        add(IUItem.tinBlock, 9.749);
        add(IUItem.bronzeBlock, 8.659);
        add(IUItem.uraniumBlock, 20.67);
        add(IUItem.leadBlock, 50.2);
        add(Items.CHARCOAL, 30.12);
        add(IUItem.Plutonium, 291.3);
        add(IUItem.smallUran235, 5.74);
        add(IUItem.Uran238, 2.296);
        add(IUItem.iridiumOre, 35);
        add(new ItemStack(Blocks.OAK_PLANKS), 5.019);
        add(new ItemStack(Blocks.SPRUCE_PLANKS), 5.7);
        add(new ItemStack(Blocks.BIRCH_PLANKS), 9.37);
        add(new ItemStack(Blocks.JUNGLE_PLANKS), 13.47);
        add(new ItemStack(Blocks.ACACIA_PLANKS), 107.7);
        add(new ItemStack(Blocks.DARK_OAK_PLANKS), 5.019);

        add(new ItemStack(Blocks.OAK_LOG), 36.92);
        add(new ItemStack(Blocks.SPRUCE_LOG), 34.06);
        add(new ItemStack(Blocks.BIRCH_LOG), 63.43);
        add(new ItemStack(Blocks.JUNGLE_LOG), 80.65);
        add(new ItemStack(Blocks.ACACIA_LOG), 646);
        add(new ItemStack(Blocks.DARK_OAK_LOG), 29.98);
        add(IUItem.rubWood.getItem(0), 1018);
        add(Items.STICK, 1.696);
        add(new ItemStack(Blocks.OAK_SAPLING), 60.12);
        add(new ItemStack(Blocks.SPRUCE_SAPLING), 119.8);
        add(new ItemStack(Blocks.BIRCH_SAPLING), 147.8);
        add(new ItemStack(Blocks.JUNGLE_SAPLING), 960.6);
        add(new ItemStack(Blocks.ACACIA_SAPLING), 1473);
        add(new ItemStack(Blocks.DARK_OAK_SAPLING), 235.6);
        add(IUItem.rubberSapling.getItemStack(), 3881);


        add(new ItemStack(Blocks.DANDELION), 370.8);
        add(new ItemStack(Blocks.POPPY), 639.3);
        add(new ItemStack(Blocks.BLUE_ORCHID), 12900);
        add(new ItemStack(Blocks.ALLIUM), 5248);
        add(new ItemStack(Blocks.AZURE_BLUET), 2042);
        add(new ItemStack(Blocks.RED_TULIP), 4203);
        add(new ItemStack(Blocks.ORANGE_TULIP), 4381);
        add(new ItemStack(Blocks.WHITE_TULIP), 5317);
        add(new ItemStack(Blocks.PINK_TULIP), 7690);
        add(new ItemStack(Blocks.OXEYE_DAISY), 3228);

        add(new ItemStack(Blocks.BROWN_MUSHROOM, 1), 973.8);
        add(new ItemStack(Blocks.RED_MUSHROOM, 1), 1946);
        add(new ItemStack(Blocks.CACTUS, 1), 4190);
        add(new ItemStack(Blocks.PUMPKIN, 1), 88320);
        add(new ItemStack(Blocks.MELON), 81490);
        add(new ItemStack(Blocks.LILY_PAD), 1704);


        add(new ItemStack(Blocks.SUNFLOWER), 4968);
        add(new ItemStack(Blocks.LILAC), 4737);
        add(new ItemStack(Blocks.LARGE_FERN), 5007);
        add(new ItemStack(Blocks.ROSE_BUSH), 5169);

        add(Items.WHEAT_SEEDS, 61.45);
        add(Items.WHEAT, 17490);
        add(new ItemStack(Blocks.SUGAR_CANE), 3074);
        add(Items.MELON, 9054);
        add(Items.MELON_SEEDS, 9054);
        add(Items.PUMPKIN_SEEDS, 88320);
        add(Items.CARROT, 30820);
        add(Items.POTATO, 28160);
        add(new ItemStack(Items.BLACK_DYE), 2056);
        add(new ItemStack(Items.RED_DYE), 639.3);
        add(new ItemStack(Items.GREEN_DYE), 4190);
        add(new ItemStack(Items.BROWN_DYE), 4881);
        add(new ItemStack(Items.PURPLE_DYE), 3230);
        add(new ItemStack(Items.CYAN_DYE), 2098);
        add(new ItemStack(Items.LIGHT_GRAY_DYE), 2042);
        add(new ItemStack(Items.GRAY_DYE), 5346);
        add(new ItemStack(Items.PINK_DYE), 329.7);
        add(new ItemStack(Items.LIME_DYE), 2105);
        add(new ItemStack(Items.YELLOW_DYE), 370.8);
        add(new ItemStack(Items.LIGHT_BLUE_DYE), 13.41);
        add(new ItemStack(Items.MAGENTA_DYE), 325.2);
        add(new ItemStack(Items.ORANGE_DYE), 505);
        add(new ItemStack(Items.BONE_MEAL), 20.18);

        add(Items.APPLE, 52.69);
        add(Items.MUSHROOM_STEW, 2923);
        add(Items.BREAD, 52.69);
        add(Items.COOKED_PORKCHOP, 82.32);
        add(Items.GOLDEN_APPLE, 120.3);
        add(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), 661.6);
        add(Items.ENDER_PEARL, 1001);
        add(Items.BLAZE_ROD, 2003);
        add(Items.BLAZE_POWDER, 400.7);
        add(IUItem.latex, 400);
        add(Items.SLIME_BALL, 133.2);
        add(Items.LEATHER, 80.57);
        add(Items.GUNPOWDER, 2.361);
        add(Items.STRING, 146.8);
        add(Items.BONE, 80.57);

        for (String s : list_string) {
            add("forge:ingots/" + s, 25);
        }
        for (String s : list_baseore1) {
            add("forge:ingots/" + s, 25);
        }
        for (String s : list_string) {
            add("forge:storage_blocks/" + s, 25 * 9 * 0.9);
        }
        for (String s : list_baseore1) {
            add("forge:storage_blocks/" + s, 25 * 9 * 0.9);
        }
        add(ItemStackHelper.fromData(IUItem.iudust, 1, 66), 8);
        add(ItemStackHelper.fromData(IUItem.iudust, 1, 69), 8);
        add(ItemStackHelper.fromData(IUItem.iudust, 1, 70), 8);
        add(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 461), 5);
        add(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 462), 3);
        add(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 463), 3);
        add(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 481), 6.5);
    }

    public static double getInBuckets(ItemStack request) {
        double ret = Recipes.recipes.getRecipeOutput("replicator", false, request).output.metadata.getDouble("matter");
        return ret;
    }

    public static void add(ItemStack stack, double col) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        CompoundTag tag = new CompoundTag();
        tag.putDouble("matter", col / 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "replicator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(stack)
                        ),
                        new RecipeOutput(tag, stack)
                )
        );
    }

    public static void add(Item stack, double col) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        CompoundTag tag = new CompoundTag();
        tag.putDouble("matter", col / 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "replicator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(stack))
                        ),
                        new RecipeOutput(tag, new ItemStack(stack))
                )
        );
    }

    public static void add(Block block, double col) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        CompoundTag tag = new CompoundTag();
        tag.putDouble("matter", col / 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "replicator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(block))
                        ),
                        new RecipeOutput(tag, new ItemStack(block))
                )
        );
    }

    public static void add(String stack, double col) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        CompoundTag tag = new CompoundTag();
        tag.putDouble("matter", col / 1000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "replicator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(stack)
                        ),
                        new RecipeOutput(tag, input1.getInput(stack).getInputs().get(0))));

    }

}
