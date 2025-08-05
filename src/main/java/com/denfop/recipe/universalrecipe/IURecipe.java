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

import java.util.List;
import java.util.Map;

public class IURecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String recipeType;
    private final List<IInputItemStack> inputs;
    private final List<ItemStack> outputs;
    private final Map<String, Object> params;

    public IURecipe(ResourceLocation id, String recipeType, List<IInputItemStack> inputs, List<ItemStack> outputs, Map<String, Object> params) {
        this.id = id;
        this.recipeType = recipeType;
        this.inputs = inputs;
        this.outputs = outputs;
        this.params = params;
    }

    @Override
    public boolean matches(Container inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        if (outputs == null)
            return ItemStack.EMPTY;
        return  outputs.get(0);
    }



    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        if (outputs == null)
            return ItemStack.EMPTY;
        return  outputs.get(0);
    }


    @Override
    public ResourceLocation getId() {
        return id;
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
}
