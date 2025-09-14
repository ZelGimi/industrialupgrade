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

public class ColonyRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    final String bodyName;
    final List<IInputItemStack> input;
    final int level;

    public ColonyRecipe(ResourceLocation id, String bodyName, List<IInputItemStack> input, int level) {
        this.id = id;
        this.bodyName = bodyName;
        this.input = input;
        this.level = level;
    }


    @Override
    public boolean matches(Container inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }




    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_COLONY_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.COLONY_RECIPE.get();
    }
}
