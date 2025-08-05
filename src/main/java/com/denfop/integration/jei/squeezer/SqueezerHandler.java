package com.denfop.integration.jei.squeezer;


import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class SqueezerHandler {

    private static final List<SqueezerHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final FluidStack output;

    public SqueezerHandler(
            ItemStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<SqueezerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SqueezerHandler addRecipe(
            ItemStack input,
            FluidStack output
    ) {
        SqueezerHandler recipe = new SqueezerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        addRecipe(
                new ItemStack(IUItem.rawLatex.getItem()),
                new FluidStack(FluidName.fluidrawlatex.getInstance().get(), 100)
        );
        addRecipe(
                new ItemStack(Items.MELON_SEEDS),
                new FluidStack(FluidName.fluidseedoil.getInstance().get(), 35)
        );
        addRecipe(
                new ItemStack(Items.PUMPKIN_SEEDS),
                new FluidStack(FluidName.fluidseedoil.getInstance().get(), 35)
        );
        addRecipe(
                new ItemStack(IUItem.crops.getStack(0)),
                new FluidStack(FluidName.fluidseedoil.getInstance().get(), 50)
        );
    }

    public ItemStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }


}
