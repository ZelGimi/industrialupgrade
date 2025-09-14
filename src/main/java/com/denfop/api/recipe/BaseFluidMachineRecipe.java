package com.denfop.api.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class BaseFluidMachineRecipe {

    public final IInputFluid input;
    public final List<FluidStack> output_fluid;
    private final RecipeOutput output;

    public BaseFluidMachineRecipe(IInputFluid input, List<FluidStack> output) {
        this.input = input;
        this.output_fluid = output;
        this.output = null;
    }

    public BaseFluidMachineRecipe(IInputFluid input, RecipeOutput output) {
        this.input = input;
        this.output_fluid = new ArrayList<>();
        this.output = output;
    }

    public BaseFluidMachineRecipe(IInputFluid input, RecipeOutput output, List<FluidStack> output_fluid) {
        this.input = input;
        this.output_fluid = output_fluid;
        this.output = output;
    }

    public static BaseFluidMachineRecipe readNBT(CompoundTag tag,RegistryAccess access) {
        IInputFluid input = InputFluid.readNBT(tag.getCompound("Input"), access);

        List<FluidStack> fluids = new ArrayList<>();
        ListTag fluidsTag = tag.getList("OutputFluids", Tag.TAG_COMPOUND);
        for (Tag fluidTag : fluidsTag) {
            if (fluidTag instanceof CompoundTag fluidCompound) {
                fluids.add(FluidStack.parseOptional(access,fluidCompound));
            }
        }

        List<ItemStack> items = new ArrayList<>();
        ListTag itemsTag = tag.getList("OutputItems", Tag.TAG_COMPOUND);
        for (Tag itemTag : itemsTag) {
            if (itemTag instanceof CompoundTag itemCompound) {
                items.add(ItemStack.parseOptional(access,itemCompound));
            }
        }

        CompoundTag metadata = tag.contains("Metadata", Tag.TAG_COMPOUND)
                ? tag.getCompound("Metadata")
                : null;

        RecipeOutput output = new RecipeOutput(metadata, items);

        return new BaseFluidMachineRecipe(input, output, fluids);
    }

    public boolean matches(List<FluidStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            if (this.input.getInputs().get(i).getFluid().equals(stacks.get(i).getFluid())) {
                return true;
            }
        }
        return false;
    }

    public RecipeOutput getOutput() {
        return this.output;
    }

    public List<FluidStack> getOutput_fluid() {
        return output_fluid;
    }

    public IInputFluid getInput() {
        return input;
    }

    public CompoundTag writeNBT(RegistryAccess registryAccess) {
        CompoundTag tag = new CompoundTag();

        tag.put("Input", input.writeNBT(registryAccess));

        ListTag fluidsTag = new ListTag();
        for (FluidStack fluid : output_fluid) {
            CompoundTag fluidTag = new CompoundTag();
            fluid.save(registryAccess,fluidTag);
            fluidsTag.add(fluidTag);
        }
        tag.put("OutputFluids", fluidsTag);

        ListTag itemsTag = new ListTag();
        if (output != null)
            for (ItemStack stack : output.items) {
                itemsTag.add(stack.save(registryAccess,new CompoundTag()));
            }
        tag.put("OutputItems", itemsTag);
        if (output != null)
            if (output.metadata != null) {
                tag.put("Metadata", output.metadata);
            }

        return tag;
    }
}
