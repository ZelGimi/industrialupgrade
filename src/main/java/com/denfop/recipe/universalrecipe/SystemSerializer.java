package com.denfop.recipe.universalrecipe;

import com.denfop.IUCore;
import com.denfop.api.space.*;
import com.denfop.api.space.System;
import com.denfop.recipe.InputOreDict;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.denfop.api.space.SpaceInit.*;

public class SystemSerializer implements RecipeSerializer<SystemRecipe> {
    public static final SystemSerializer INSTANCE = new SystemSerializer();

    @Override
    public SystemRecipe fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();
        int distanceFromStar = json.get("name").getAsInt();
        regSystem.add(() ->  new System(name,distanceFromStar));
        return new SystemRecipe(id, "", Collections.emptyList(), "");
    }

    @Override
    public SystemRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        return new SystemRecipe(id, "", new ArrayList<>(), "");
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SystemRecipe recipe) {


    }
}
