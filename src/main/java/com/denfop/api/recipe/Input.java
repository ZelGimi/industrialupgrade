package com.denfop.api.recipe;

import com.denfop.recipe.IInputItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Input implements IInput {

    private final List<IInputItemStack> list;
    private final boolean hasfluid;
    private final FluidStack fluid;
    private List<FluidStack> inputsfluid;

    public Input(FluidStack fluid, IInputItemStack... inputs) {
        this.list = Arrays.asList(inputs);
        this.hasfluid = fluid != null;
        this.fluid = fluid;
        this.inputsfluid = null;
    }

    public Input(IInputItemStack... inputs) {
        this(null, inputs);

    }
    public Input(List<IInputItemStack> inputs) {
        this(null, inputs);

    }
    public Input(FluidStack fluid, List<IInputItemStack> inputs) {
        this.list = inputs;
        this.hasfluid = fluid != null;
        this.fluid = fluid;
        this.inputsfluid = null;
    }
    public Input(FluidStack... inputs) {
        this((IInputItemStack) null);
        this.inputsfluid = Arrays.asList(inputs);
    }

    @Override
    public List<IInputItemStack> getInputs() {
        return this.list;
    }

    @Override
    public boolean hasFluids() {
        return this.hasfluid;
    }

    @Override
    public FluidStack getFluid() {
        return this.fluid;
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return this.inputsfluid;
    }

    @Override
    public List<ItemStack> getStackInputs() {
        List<ItemStack> stacks = new LinkedList<>();
        for (IInputItemStack itemStack : list) {
            stacks.add(itemStack.getInputs().get(0));
        }
        return new ArrayList<>(stacks);
    }
    @Override
    public List<ItemStack> getAllStackInputs() {
        List<ItemStack> stacks = new LinkedList<>();
        for (IInputItemStack itemStack : list) {
            stacks.addAll(itemStack.getInputs());
        }
        return new ArrayList<>(stacks);
    }
}
