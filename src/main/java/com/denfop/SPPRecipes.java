package com.denfop;


import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import ic2.core.recipe.ColourCarryingRecipe;
import ic2.core.util.ConfigUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.text.ParseException;

public final class SPPRecipes {

    static void addCraftingRecipes() {

    }

    public static void addShapedRecipe(ItemStack output, Object... inputs) {
        Recipes.advRecipes.addRecipe(output, inputs);
    }

    public static void addShapelessRecipe(ItemStack output, Object... inputs) {
        Recipes.advRecipes.addShapelessRecipe(output, inputs);
    }

    private static void addShapedColourRecipe(ItemStack output, Object... inputs) {
        ColourCarryingRecipe.addAndRegister(output, inputs);
    }

    private static void addCompressorRecipe(IRecipeInput input, ItemStack output) {
        Recipes.compressor.addRecipe(input, null, false, output);
    }

    private static void addExtrudingRecipe(IRecipeInput input, ItemStack output) {
        Recipes.metalformerExtruding.addRecipe(input, null, false, output);
    }

    private static void addcanerRecipe(IRecipeInput input, IRecipeInput input1, ItemStack output) {
        Recipes.cannerBottle.addRecipe(input, input1, output, false);
    }

    private static void addcentrifugeRecipe(IRecipeInput input, ItemStack[] output) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 5000);
        Recipes.centrifuge.addRecipe(input, nbt, false, output);
    }


    private static boolean decodeLine(int number, String[] parts) throws ParseException {
        int energy;
        IRecipeInput input = ConfigUtil.asRecipeInputWithAmount(parts[0].trim());
        if (input == null) {
            IUCore.log.warn("Skipping line {} as the input ({}) cannot be resolved", number, parts[0].trim());
            return false;
        }
        ItemStack output = ConfigUtil.asStackWithAmount(parts[1].trim());
        if (output == null) {
            String attempt = parts[1].trim();
            if (attempt.startsWith("OreDict:")) {
                NonNullList<ItemStack> nonNullList = OreDictionary.getOres(attempt.substring(attempt.indexOf(':') + 1).trim());
                if (!nonNullList.isEmpty()) {
                    output = nonNullList.get(0);
                    IUCore.log.debug("Continued on line {} as the output ({}) could be resolved to {}", number, attempt, output);
                }
            }
            if (output == null) {
                IUCore.log.warn("Skipping line {} as the output ({}) cannot be resolved", number, attempt);
                return false;
            }
        }
        try {
            energy = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            IUCore.log.warn("Skipping line {} as the energy ({}) cannot be resolved to a number", number, parts[2].trim());
            return false;
        }
        return true;
    }

}
