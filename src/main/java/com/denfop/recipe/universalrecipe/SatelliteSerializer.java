package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.*;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Collections;

import static com.denfop.api.space.SpaceInit.regSatellite;
import static com.denfop.recipe.universalrecipe.PlanetSerializer.stringList;

public class SatelliteSerializer implements RecipeSerializer<SatelliteRecipe> {
    public static final SatelliteSerializer INSTANCE = new SatelliteSerializer();

    @Override
    public SatelliteRecipe fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();

        String systemStr = json.get("system").getAsString().toLowerCase();
        String textureStr = json.get("texture").getAsString();
        EnumLevels level = EnumLevels.valueOf(json.get("level").getAsString().toUpperCase());
        String planetStr = json.get("planet").getAsString();
        int temperature = json.get("temperature").getAsInt();
        boolean pressure = json.get("pressure").getAsBoolean();
        double distance = json.get("distance").getAsDouble();
        EnumType type = EnumType.valueOf(json.get("type").getAsString().toUpperCase());
        boolean oxygen = json.get("oxygen").getAsBoolean();
        boolean colonies = json.get("colonies").getAsBoolean();
        int angle = json.get("angle").getAsInt();
        double time = json.get("time").getAsDouble();
        double size = json.get("size").getAsDouble();
        double rotation = json.get("rotation").getAsDouble();
        ResourceLocation texture = ResourceLocation.tryParse(textureStr + ".png");
        if (!stringList.contains("satellite_"+name)) {
            regSatellite.add(() -> new Satellite(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr)), texture, level, (IPlanet) SpaceNet.instance.getBodyFromName(planetStr), temperature, pressure, distance,
                    type, oxygen, colonies, angle, time, size, rotation));
            stringList.add("satellite_"+name);
        }
        return new SatelliteRecipe(id,name, systemStr, textureStr, level, planetStr, temperature, pressure, distance, type, oxygen, colonies, angle, time, size, rotation);

    }

    @Override
    public SatelliteRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String name = buf.readUtf();
        String systemName = buf.readUtf();
        String texturePath = buf.readUtf();
        EnumLevels level = EnumLevels.valueOf(buf.readUtf().toUpperCase());
        String planetName = buf.readUtf();
        int temperature = buf.readVarInt();
        boolean pressure = buf.readBoolean();
        double distance = buf.readDouble();
        EnumType type = EnumType.valueOf(buf.readUtf().toUpperCase());
        boolean oxygen = buf.readBoolean();
        boolean colonies = buf.readBoolean();
        int angle = buf.readVarInt();
        double time = buf.readDouble();
        double size = buf.readDouble();
        double rotation = buf.readDouble();
        ResourceLocation texture = ResourceLocation.tryParse(texturePath + ".png");
        if (!stringList.contains("satellite_"+name)) {
            regSatellite.add(() -> new Satellite(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemName.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemName)), texture, level, (IPlanet) SpaceNet.instance.getBodyFromName(planetName), temperature, pressure, distance,
                    type, oxygen, colonies, angle, time, size, rotation));
            stringList.add("satellite_"+name);
        }
        return new SatelliteRecipe(
                id, name, systemName, texturePath, level, planetName,
                temperature, pressure, distance, type, oxygen, colonies,
                angle, time, size, rotation
        );
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SatelliteRecipe recipe) {
        buf.writeUtf(recipe.name);
        buf.writeUtf(recipe.systemName);
        buf.writeUtf(recipe.texturePath);
        buf.writeUtf(recipe.level.name());
        buf.writeUtf(recipe.planetName);
        buf.writeVarInt(recipe.temperature);
        buf.writeBoolean(recipe.pressure);
        buf.writeDouble(recipe.distance);
        buf.writeUtf(recipe.type.name());
        buf.writeBoolean(recipe.oxygen);
        buf.writeBoolean(recipe.colonies);
        buf.writeVarInt(recipe.angle);
        buf.writeDouble(recipe.time);
        buf.writeDouble(recipe.size);
        buf.writeDouble(recipe.rotation);
    }
}
