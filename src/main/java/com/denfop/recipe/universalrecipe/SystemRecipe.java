package com.denfop.recipe.universalrecipe;

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

public class SystemRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    public String name;
    public Integer distance;

    public SystemRecipe(ResourceLocation id, String name, Integer distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
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

    public String getName() {
        return this.name;
    }

    public Integer getDistanceFromStar() {
        return this.distance;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_SYSTEM_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.SYSTEM_RECIPE.get();
    }
}
