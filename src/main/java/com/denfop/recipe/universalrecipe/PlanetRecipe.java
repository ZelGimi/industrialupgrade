package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.EnumType;
import com.denfop.api.space.IStar;
import com.denfop.api.space.ISystem;
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

public class PlanetRecipe implements Recipe<Container> {
    public final ResourceLocation id;
    public final String name;
    public final String system;
    public final ResourceLocation texture;
    public final EnumLevels level;
    public final String star;
    public final int temperature;
    public final boolean pressure;
    public final double distance;
    public final EnumType type;
    public final boolean oxygen;
    public final boolean colonies;
    public final int angle;
    public final double time;
    public final double size;
    public final double rotation;

    public PlanetRecipe(ResourceLocation id, String name, String system, ResourceLocation texture, EnumLevels level, String star,
                        int temperature, boolean pressure, double distance, EnumType type,
                        boolean oxygen, boolean colonies, int angle, double time, double size, double rotation) {
        this.id = id;
        this.name = name;
        this.system = system;
        this.texture = texture;
        this.level = level;
        this.star = star;
        this.temperature = temperature;
        this.pressure = pressure;
        this.distance = distance;
        this.type = type;
        this.oxygen = oxygen;
        this.colonies = colonies;
        this.angle = angle;
        this.time = time;
        this.size = size;
        this.rotation = rotation;
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
        return Register.RECIPE_SERIALIZER_PLANET_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.PLANET_RECIPE.get();
    }
}
