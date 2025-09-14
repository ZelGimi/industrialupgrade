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
import static com.denfop.recipe.universalrecipe.PlanetSerializer.stringList;

public class StarSerializer implements RecipeSerializer<StarRecipe> {
    public static final StarSerializer INSTANCE = new StarSerializer();

    @Override
    public StarRecipe fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();

        String systemStr = json.get("system").getAsString().toLowerCase();
        String textureStr = json.get("texture").getAsString();
        int angle = json.get("angle").getAsInt();
        double size = json.get("size").getAsDouble();
        ResourceLocation texture = ResourceLocation.tryParse(textureStr + ".png");
        if (!stringList.contains("star_"+name)) {
            regStar.add(() -> new Star(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr)), texture, angle, size));
            stringList.add("star_"+name);
        }
        return new StarRecipe(id,name, systemStr, textureStr, angle, size);
    }

    @Override
    public StarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String name = buf.readUtf();
        String systemName = buf.readUtf();
        String texturePath = buf.readUtf();
        int angle = buf.readVarInt();
        double size = buf.readDouble();
        ResourceLocation texture = ResourceLocation.tryParse(texturePath + ".png");
        if (!stringList.contains("star_"+name)) {
            regStar.add(() -> new Star(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemName.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemName)), texture, angle, size));
            stringList.add(name);
        }
        return new StarRecipe(id, "star_"+name, systemName, texturePath, angle, size);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, StarRecipe recipe) {
        buf.writeUtf(recipe.name);
        buf.writeUtf(recipe.systemName);
        buf.writeUtf(recipe.texturePath);
        buf.writeVarInt(recipe.angle);
        buf.writeDouble(recipe.size);
    }
}
