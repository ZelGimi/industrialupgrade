package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.register.Register;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class SmelteryRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String operation;
    private final List<IInputItemStack> inputs;
    private final List<IInputItemStack> outputs;

    public SmelteryRecipe(ResourceLocation id,String operation, List<IInputItemStack> inputs, List<IInputItemStack> outputs) {
        this.operation = operation;
        this.id=id;
        this.inputs = inputs;
        this.outputs = outputs;
    }


    public String getOperation() {
        return operation;
    }

    public List<IInputItemStack> getInputs() {
        return inputs;
    }

    public List<IInputItemStack> getOutputs() {
        return outputs;
    }
    @Override
    public boolean matches(Container inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }



    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_SMELTERY_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.SMELTERY_RECIPE.get();
    }
}
