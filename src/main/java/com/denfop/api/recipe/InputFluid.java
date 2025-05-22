package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import com.denfop.recipe.IInputItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InputFluid implements IInputFluid {

    private final List<FluidStack> inputsfluid;
    private IInputItemStack stack = null;

    public InputFluid(FluidStack... inputs) {
        this.inputsfluid = Arrays.asList(inputs);
    }

    public InputFluid(ItemStack stack) {
        this.stack = Recipes.inputFactory.getInput(stack);
        inputsfluid = Collections.emptyList();
    }

    public InputFluid(String stack) {
        this.stack = Recipes.inputFactory.getInput(stack);
        inputsfluid = Collections.emptyList();
    }

    public InputFluid(String stack, FluidStack... inputs) {
        this.stack = Recipes.inputFactory.getInput(stack);
        inputsfluid = Arrays.asList(inputs);
    }

    public InputFluid(ItemStack stack, FluidStack... inputs) {
        this.stack = Recipes.inputFactory.getInput(stack);
        inputsfluid = Arrays.asList(inputs);
    }

    @Override
    public List<FluidStack> getInputs() {
        return this.inputsfluid;
    }

    @Override
    public IInputItemStack getStack() {
        return stack;
    }


}
