package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.ISystem;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.Star;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Collections;

import static com.denfop.api.space.SpaceInit.regStar;

public class StarSerializer implements RecipeSerializer<StarRecipe> {
    public static final StarSerializer INSTANCE = new StarSerializer();

    @Override
    public StarRecipe fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();

        ISystem system = SpaceNet.instance.getSystem().stream().filter(systems -> systems.getName().equals(json.get("system").getAsString().toLowerCase())).toList().get(0);
        ResourceLocation texture = new ResourceLocation(json.get("texture").getAsString() + ".png");
        int angle = json.get("angle").getAsInt();
        double size = json.get("size").getAsDouble();
        regStar.add(() -> new Star(name, system, texture, angle, size));
        return new StarRecipe(id, "", Collections.emptyList(), "");
    }

    @Override
    public StarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

        return new StarRecipe(id, "", new ArrayList<>(), "");
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, StarRecipe recipe) {


    }
}
