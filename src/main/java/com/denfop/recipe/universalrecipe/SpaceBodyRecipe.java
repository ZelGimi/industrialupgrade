package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.register.Register;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class SpaceBodyRecipe implements Recipe<CraftingInput> {


    public final String bodyName;
    public final Integer percent;
    public final Integer chance;
    public final String roverType;
    public final String operationType;
    public final List<IInputItemStack> input;

    public SpaceBodyRecipe(String bodyName, Integer percent, Integer chance, String roverType, String operationType, List<IInputItemStack> input) {
        this.bodyName = bodyName;
        this.percent = percent;
        this.chance = chance;
        this.roverType = roverType;
        this.operationType = operationType;
        this.input = input;
    }

    @Override
    public boolean matches(CraftingInput inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }





    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_BODY_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.BODY_RECIPE.get();
    }


    public String typeRover() {
        return "";
    }

    public String typeOperation() {
        return "";
    }

    public String bodyName() {
        return "";
    }

    public Integer chance() {
        return 0;
    }

    public Integer percent() {
        return 0;
    }

    public List<IInputItemStack> getInputs() {
        return input;
    }
}
