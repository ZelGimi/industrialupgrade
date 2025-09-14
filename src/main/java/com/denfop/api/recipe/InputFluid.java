package com.denfop.api.recipe;


import com.denfop.api.Recipes;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InputFluid implements IInputFluid {

    private final List<FluidStack> inputsfluid;
    private IInputItemStack stack = null;

    public InputFluid(FluidStack... inputs) {
        this.inputsfluid = Arrays.asList(inputs);
    }

    public InputFluid(String stack) {
        this.stack = Recipes.inputFactory.getInput(stack);
        inputsfluid = Collections.emptyList();
    }

    public InputFluid(ItemStack stack) {
        this.stack = Recipes.inputFactory.getInput(stack);
        inputsfluid = Collections.emptyList();
    }

    public InputFluid(IInputItemStack stack) {
        this.stack = Recipes.inputFactory.getInput(stack);
        inputsfluid = Collections.emptyList();
    }

    public InputFluid(ItemStack stack, FluidStack... inputs) {
        this.stack = Recipes.inputFactory.getInput(stack);
        inputsfluid = Arrays.asList(inputs);
    }

    public InputFluid(IInputItemStack stack, FluidStack... inputs) {
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

    public static InputFluid readNBT(CompoundTag tag) {
        List<FluidStack> fluids = new ArrayList<>();
        ListTag fluidsTag = tag.getList("Fluids", Tag.TAG_COMPOUND);
        for (Tag fluidTag : fluidsTag) {
            if (fluidTag instanceof CompoundTag fluidCompound) {
                fluids.add(FluidStack.loadFluidStackFromNBT(fluidCompound));
            }
        }


        IInputItemStack stack = null;
        if (tag.contains("Stack", Tag.TAG_COMPOUND)) {
            stack = InputItemStack.create(tag.getCompound("Stack"));
        }

        InputFluid inputFluid = new InputFluid(fluids);
        if (stack != null) inputFluid.setStack(stack);

        return inputFluid;
    }
    public InputFluid(List<FluidStack> inputs) {
        this.inputsfluid = inputs;
    }
    private void setStack(IInputItemStack stack) {
        this.stack = stack;
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag fluidsTag = new ListTag();
        for (FluidStack fluid : inputsfluid) {
            CompoundTag fluidTag = new CompoundTag();
            fluid.writeToNBT(fluidTag);
            fluidsTag.add(fluidTag);
        }
        tag.put("Fluids", fluidsTag);

        if (stack != null) {
            tag.put("Stack", stack.writeNBT());
        }

        return tag;
    }

}
