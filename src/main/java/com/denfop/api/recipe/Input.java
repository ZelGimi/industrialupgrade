package com.denfop.api.recipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

    public static Input readNBT(CompoundTag tag) {
        List<IInputItemStack> inputList = new ArrayList<>();
        ListTag itemsTag = tag.getList("Items", 10);
        for (Tag itemTag : itemsTag) {
            if (itemTag instanceof CompoundTag compoundTag) {
                inputList.add(InputItemStack.create(compoundTag));
            }
        }

        FluidStack fluid = FluidStack.EMPTY;
        if (tag.getBoolean("HasFluid") && tag.contains("Fluid", Tag.TAG_COMPOUND)) {
            fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid"));
        }

        return new Input(fluid, inputList.toArray(new IInputItemStack[0]));
    }

    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();


        ListTag itemsTag = new ListTag();
        for (IInputItemStack item : list) {
            itemsTag.add(item.writeNBT());
        }
        tag.put("Items", itemsTag);


        if (hasfluid && fluid != null && !fluid.isEmpty()) {
            CompoundTag fluidTag = new CompoundTag();
            fluid.writeToNBT(fluidTag);
            tag.put("Fluid", fluidTag);
        }

        tag.putBoolean("HasFluid", hasfluid);
        return tag;
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
