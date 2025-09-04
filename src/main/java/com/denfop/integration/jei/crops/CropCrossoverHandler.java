package com.denfop.integration.jei.crops;


import com.denfop.IUItem;
import com.denfop.api.crop.Crop;
import com.denfop.api.crop.CropNetwork;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CropCrossoverHandler {

    private static final List<CropCrossoverHandler> recipes = new ArrayList<>();
    public final Crop output;
    public final List<Crop> inputs;


    public CropCrossoverHandler(
            Crop output, List<Crop> inputs
    ) {
        this.output = output;
        this.inputs = inputs;
    }

    public static List<CropCrossoverHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CropCrossoverHandler addRecipe(
            Crop output, List<Crop> inputs
    ) {
        CropCrossoverHandler recipe = new CropCrossoverHandler(output, inputs);

        recipes.add(recipe);
        return recipe;
    }

    public static CropCrossoverHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        CropNetwork.instance.getCropMap().forEach((integer, crop) -> {
            if (crop.isCombine() && !crop.getCropCombine().isEmpty()) {
                addRecipe(crop, crop.getCropCombine());
            }
        });


    }

    public ItemStack getOutputs() {
        ItemStack stack = new ItemStack(IUItem.crops.getStack(0));
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putInt("crop_id", output.getId());
        return stack;
    }

    public List<ItemStack> getInputs() {
        List<ItemStack> itemStackList = new ArrayList<>();
        inputs.forEach(crop -> {
            ItemStack stack = new ItemStack(IUItem.crops.getStack(0));
            final CompoundTag nbt = ModUtils.nbt(stack);
            nbt.putInt("crop_id", crop.getId());
            itemStackList.add(stack);
        });
        return itemStackList;
    }


}
