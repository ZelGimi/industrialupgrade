package com.denfop.integration.jei.spacebody;


import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpaceBodyHandler {

    private static final List<SpaceBodyHandler> recipes = new ArrayList<>();
    private final List<ItemStack> input;
    final IBody body;
    List<FluidStack>  output;

    public SpaceBodyHandler(IBody body,
            List<ItemStack> input,
                            List<FluidStack> output
    ) {
        this.body = body;
        this.input = input;
        this.output = output;
    }

    public static List<SpaceBodyHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SpaceBodyHandler addRecipe(IBody body,
            List<ItemStack> input,  List<FluidStack> output
    ) {
        SpaceBodyHandler recipe = new SpaceBodyHandler(body,input, output);
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
            final List<IBaseResource> resource = body.getResources();
            if (resource == null || resource.isEmpty())
                continue;
            cycle:
            for (IBaseResource baseResource : resource) {
                if (baseResource.getItemStack() != null) {
                    for (ItemStack stack : stacks) {
                        if (stack.isItemEqual(baseResource.getItemStack())) {
                            continue cycle;
                        }
                    }
                }
                if (baseResource.getFluidStack() != null) {
                    for (FluidStack fluidStack : fluidStacks) {
                        if (fluidStack.isFluidEqual(baseResource.getFluidStack())) {
                            continue cycle;
                        }
                    }
                }
                if (baseResource.getFluidStack() != null) {
                    fluidStacks.add(baseResource.getFluidStack());
                }
                if (baseResource.getItemStack() != null) {
                    stacks.add(baseResource.getItemStack());
                }
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
