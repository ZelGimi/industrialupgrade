package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.EnumType;
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

public class AsteroidRecipe implements Recipe<CraftingInput> {


    public String name;
    public String systemName;
    public String texturePath;
    public EnumLevels level = EnumLevels.FIVE;
    public String starName;
    public Integer temperature;
    public Double distance;
    public EnumType type = EnumType.NEUTRAL;
    public Boolean colonies;
    public Integer angle;
    public Double time;
    public Double size;
    public Double rotation;
    public Double maxLocation;
    public Double minLocation;
    public Integer amount;


    public AsteroidRecipe(String name, String systemName, String texturePath, EnumLevels level, String starName, Integer temperature, Double distance, EnumType type, Boolean colonies, Integer angle, Double time, Double size, Double rotation, Double maxLocation, Double minLocation, Integer amount) {
        this.name = name;
        this.systemName = systemName;
        this.texturePath = texturePath;
        this.level = level;
        this.starName = starName;
        this.temperature = temperature;
        this.distance = distance;
        this.type = type;
        this.colonies = colonies;
        this.angle = angle;
        this.time = time;
        this.size = size;
        this.rotation = rotation;
        this.maxLocation = maxLocation;
        this.minLocation = minLocation;
        this.amount = amount;
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
        return Register.RECIPE_SERIALIZER_ASTEROID_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.ASTEROID_RECIPE.get();
    }


}
