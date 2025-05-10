package com.denfop.integration.jei.colonial_resource;


import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.DataItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpaceColonyHandler {

    private static final List<SpaceColonyHandler> recipes = new ArrayList<>();
    final IBody body;
    private final List<ItemStack> input;
    List<FluidStack> output;

    public SpaceColonyHandler(
            IBody body,
            List<ItemStack> input,
            List<FluidStack> output
    ) {
        this.body = body;
        this.input = input;
        this.output = output;
    }

    public static List<SpaceColonyHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SpaceColonyHandler addRecipe(
            IBody body,
            List<ItemStack> input, List<FluidStack> output
    ) {
        SpaceColonyHandler recipe = new SpaceColonyHandler(body, input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (IBody body :
                SpaceNet.instance.getBodyList()) {
            List<ItemStack> stacks = new LinkedList<>();
            List<FluidStack> fluidStacks = new LinkedList<>();
            final List<DataItem<FluidStack>> resourceFluid = SpaceNet.instance
                    .getColonieNet()
                    .getFluidsFromBody(body);
            final List<DataItem<ItemStack>> resourceStack = SpaceNet.instance.getColonieNet().getItemsFromBody(body);
            if ((resourceFluid == null || resourceFluid.isEmpty()) && (resourceStack == null || resourceStack.isEmpty())) {
                continue;
            }

            for (DataItem<ItemStack> baseResource : resourceStack) {
                stacks.add(baseResource.getElement());
            }

            for (DataItem<FluidStack> baseResource : resourceFluid) {
                fluidStacks.add(baseResource.getElement());
            }

            addRecipe(
                    body,
                    stacks,
                    fluidStacks
            );


        }
    }


    public List<ItemStack> getInput() {
        return input;
    }


    public List<FluidStack> getOutput() {
        return output;
    }


}
