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

public class StarRecipe implements Recipe<Container> {
    private final ResourceLocation id;

    public String name;
    public String systemName;
    public String texturePath;
    public Integer angle;
    public Double size;

    public StarRecipe(ResourceLocation id, String name, String systemName, String texturePath, Integer angle, Double size) {
        this.id = id;
        this.name = name;
        this.systemName = systemName;
        this.texturePath = texturePath;
        this.angle = angle;
        this.size = size;
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
        return Register.RECIPE_SERIALIZER_STAR_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.STAR_RECIPE.get();
    }
}
