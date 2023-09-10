package com.denfop.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class InputHandler implements IInputHandler {

    public InputHandler() {
    }

    public IInputItemStack getInput(ItemStack stack) {
        return new InputItemStack(stack);
    }

    public IInputItemStack getInput(ItemStack stack, int amount) {
        return new InputItemStack(stack, amount);
    }


    public IInputItemStack getInput(String name) {
        return new InputOreDict(name);
    }

    public IInputItemStack getInput(String name, int amount) {
        return new InputOreDict(name, amount);
    }

    public IInputItemStack getInput(String name, int amount, int metaOverride) {
        return new InputOreDict(name, amount, metaOverride);
    }

    public IInputItemStack getInput(Fluid fluid) {
        return new InputFluidStack(fluid);
    }

    public IInputItemStack getInput(Fluid fluid, int amount) {
        return new InputFluidStack(fluid, amount);
    }

}
