package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.register.Register;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;

public class IURecipe implements Recipe<CraftingInput> {
    private final String recipeType;
    private final List<IInputItemStack> inputs;
    private final List<ItemStack> outputs;
    private final Map<String, Object> params;
    private final boolean isFluid;
    private final List<FluidStack> fluidStacks;
    private final List<FluidStack> outputsFluid;


    public IURecipe(String recipeType, Boolean isFluidRecipe, List<IInputItemStack> inputs, List<FluidStack> fluidStacks, List<ItemStack> outputs1, List<FluidStack> outputsFluid, Map<String, Object> params) {
        this.recipeType = recipeType;
        this.inputs = inputs;
        this.outputs = outputs1;
        this.params = params;
        this.fluidStacks = fluidStacks;
        this.outputsFluid = outputsFluid;
        this.isFluid = isFluidRecipe;
    }

    public List<FluidStack> getFluidStacks() {
        return fluidStacks;
    }

    public List<FluidStack> getOutputsFluid() {
        return outputsFluid;
    }

    @Override
    public boolean matches(CraftingInput inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider provider) {
        if (outputs == null || outputs.isEmpty())
            return ItemStack.EMPTY;
        return outputs.get(0);
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        if (outputs == null || outputs.isEmpty())
            return ItemStack.EMPTY;
        return outputs.get(0);
    }


    public List<IInputItemStack> getInputs() {
        return inputs;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_IU.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.UNIVERSAL_RECIPE_TYPE.get();
    }


    public CompoundTag getParamsTag() {
        CompoundTag compound = new CompoundTag();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof Boolean) {
                compound.putBoolean(entry.getKey(), (Boolean) entry.getValue());
            }
            if (entry.getValue() instanceof Number) {
                compound.putDouble(entry.getKey(), ((Number) entry.getValue()).doubleValue());
            }
        }
        return compound;
    }

    public boolean isFluid() {
        return isFluid;
    }
}
