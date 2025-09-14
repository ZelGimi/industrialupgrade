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

public class SpaceBodyRecipe implements Recipe<Container> {
    private final ResourceLocation id;

    public final String bodyName;
    public final Integer percent;
    public final Integer chance;
    public final String roverType;
    public final String operationType;
    public final List<IInputItemStack> input;
    public SpaceBodyRecipe(ResourceLocation id, String bodyName, Integer percent, Integer chance, String roverType, String operationType, List<IInputItemStack> input) {
        this.id=id;
        this.bodyName = bodyName;
        this.percent = percent;
        this.chance = chance;
        this.roverType = roverType;
        this.operationType = operationType;
        this.input = input;
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
        return Register.RECIPE_SERIALIZER_BODY_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.BODY_RECIPE.get();
    }
}
