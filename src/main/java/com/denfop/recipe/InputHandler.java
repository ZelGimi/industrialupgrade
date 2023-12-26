package com.denfop.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class InputHandler implements IInputHandler {

    public InputHandler() {
    }

    public IInputItemStack getInput(ItemStack stack) {
        return new InputItemStack(stack);
    }

    @Override
    public IInputItemStack getInput(final Object var1) {
        if(var1 instanceof ItemStack)
            return this.getInput((ItemStack)var1);
        if(var1 instanceof Fluid)
            return this.getInput((Fluid)var1);
        if(var1 instanceof String)
            return this.getInput((String)var1);
        if(var1 instanceof Item)
            return this.getInput(new ItemStack((Item) var1));
        return null;
    }
    @Override
    public IInputItemStack getInput(final Object var1, int i) {
        if(var1 instanceof ItemStack)
            return this.getInput((ItemStack)var1,i);
        if(var1 instanceof Fluid)
            return this.getInput((Fluid)var1,i);
        if(var1 instanceof String)
            return this.getInput((String)var1,i);
        return null;
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
