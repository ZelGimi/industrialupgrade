package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.*;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Collections;

import static com.denfop.api.space.SpaceInit.regAsteroid;
import static com.denfop.recipe.universalrecipe.PlanetSerializer.stringList;

public class AsteroidSerializer implements RecipeSerializer<AsteroidRecipe> {
    public static final AsteroidSerializer INSTANCE = new AsteroidSerializer();

    @Override
    public AsteroidRecipe fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();

        String systemStr = json.get("system").getAsString();
        String textureStr = json.get("texture").getAsString();
        EnumLevels level = EnumLevels.valueOf(json.get("level").getAsString().toUpperCase());
        String starStr = json.get("star").getAsString();
        int temperature = json.get("temperature").getAsInt();
        double minLocation = json.get("minLocation").getAsDouble();
        double distance = json.get("distance").getAsDouble();
        EnumType type = EnumType.valueOf(json.get("type").getAsString().toUpperCase());
        double maxLocation = json.get("maxLocation").getAsDouble();
        boolean colonies = json.get("colonies").getAsBoolean();
        int angle = json.get("angle").getAsInt();
        int amount = json.get("amount").getAsInt();
        double time = json.get("time").getAsDouble();
        double size = json.get("size").getAsDouble();
        double rotation = json.get("rotation").getAsDouble();

        ResourceLocation texture = ResourceLocation.tryParse(textureStr + ".png");

        if (!stringList.contains("asteroid_"+name)) {
            regAsteroid.add(() -> new Asteroid(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr)), texture, level, (IStar) SpaceNet.instance.getBodyFromName(starStr), temperature,
                    distance, type, colonies, angle, time, size, rotation, minLocation, maxLocation, amount));
            stringList.add("asteroid_"+name);
        }
        return new AsteroidRecipe(id,name, systemStr, textureStr, level, starStr, temperature, distance, type, colonies, angle,
                time, size, rotation, minLocation, maxLocation, amount);
    }

    @Override
    public AsteroidRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String name = buf.readUtf();
        String systemName = buf.readUtf();
        String texturePath = buf.readUtf();
        EnumLevels level = EnumLevels.valueOf(buf.readUtf().toUpperCase());
        String starName = buf.readUtf();
        int temperature = buf.readVarInt();
        double distance = buf.readDouble();
        EnumType type = EnumType.valueOf(buf.readUtf().toUpperCase());
        boolean colonies = buf.readBoolean();
        int angle = buf.readVarInt();
        double time = buf.readDouble();
        double size = buf.readDouble();
        double rotation = buf.readDouble();
        double minLocation = buf.readDouble();
        double maxLocation = buf.readDouble();
        int amount = buf.readVarInt();
        if (!stringList.contains("asteroid_"+name)) {
            regAsteroid.add(() -> new Asteroid(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemName.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemName)), ResourceLocation.tryParse(texturePath), level, (IStar) SpaceNet.instance.getBodyFromName(starName), temperature,
                    distance, type, colonies, angle, time, size, rotation, minLocation, maxLocation, amount));
            stringList.add("asteroid_"+name);
        }
        return new AsteroidRecipe(id,
                name, systemName, texturePath, level, starName,
                temperature, distance, type, colonies,
                angle, time, size, rotation,
                minLocation, maxLocation, amount
        );
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, AsteroidRecipe recipe) {
        buf.writeUtf(recipe.name);
        buf.writeUtf(recipe.systemName);
        buf.writeUtf(recipe.texturePath);
        buf.writeUtf(recipe.level.name());
        buf.writeUtf(recipe.starName);
        buf.writeVarInt(recipe.temperature);
        buf.writeDouble(recipe.distance);
        buf.writeUtf(recipe.type.name());
        buf.writeBoolean(recipe.colonies);
        buf.writeVarInt(recipe.angle);
        buf.writeDouble(recipe.time);
        buf.writeDouble(recipe.size);
        buf.writeDouble(recipe.rotation);
        buf.writeDouble(recipe.minLocation);
        buf.writeDouble(recipe.maxLocation);
        buf.writeVarInt(recipe.amount);
    }
}
