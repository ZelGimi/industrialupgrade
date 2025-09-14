package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.System;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Collections;

import static com.denfop.api.space.SpaceInit.regSystem;
import static com.denfop.recipe.universalrecipe.PlanetSerializer.stringList;

public class SystemSerializer implements RecipeSerializer<SystemRecipe> {
    public static final SystemSerializer INSTANCE = new SystemSerializer();

    @Override
    public SystemRecipe fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();
        int distanceFromStar = json.get("distance").getAsInt();
        if (!stringList.contains("system_"+name)) {
            regSystem.add(() -> new System(name, distanceFromStar));
            stringList.add("system_"+name);
        }
        return new SystemRecipe(id, name,distanceFromStar);
    }

    @Override
    public SystemRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String name = buf.readUtf();
        int distanceFromStar = buf.readVarInt();
        if (!stringList.contains("system_"+name)) {
            regSystem.add(() -> new System(name, distanceFromStar));
            stringList.add("system_"+name);
        }
        return new SystemRecipe(id, name, distanceFromStar);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SystemRecipe recipe) {
        buf.writeUtf(recipe.name);
        buf.writeVarInt(recipe.distance);
    }
}
