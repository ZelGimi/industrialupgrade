package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.*;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;

import static com.denfop.api.space.SpaceInit.regPlanet;

public class PlanetSerializer implements RecipeSerializer<PlanetRecipe> {
    public static final PlanetSerializer INSTANCE = new PlanetSerializer();
    public static List<String> stringList = new ArrayList<>();

    @Override
    public PlanetRecipe fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();

        ResourceLocation texture = new ResourceLocation(json.get("texture").getAsString() + ".png");
        EnumLevels level = EnumLevels.valueOf(json.get("level").getAsString().toUpperCase());
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
        if (!stringList.contains("planet_"+name)) {
            regPlanet.add(() -> new Planet(name, SpaceNet.instance.getSystem().stream().filter(systems -> systems.getName().equals(json.get("system").getAsString().toLowerCase())).toList().get(0), texture, level, (IStar) SpaceNet.instance.getBodyFromName(json.get("star").getAsString()), temperature, pressure, distance, type,
                    oxygen, colonies, angle, time, size, rotation));
            stringList.add("planet_"+name);
        }
        return new PlanetRecipe(id, name, json.get("star").getAsString(), texture, level, json.get("star").getAsString(), temperature, pressure, distance, type,
                oxygen, colonies, angle, time, size, rotation);
    }

    @Override
    public PlanetRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String name = buf.readUtf();
        String systemName = buf.readUtf();
        String texturePath = buf.readUtf();
        ResourceLocation texture = new ResourceLocation(texturePath);
        EnumLevels level = EnumLevels.valueOf(buf.readUtf().toUpperCase());
        String starName = buf.readUtf();
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


        if (!stringList.contains("planet_"+name)) {
            regPlanet.add(() -> new Planet(name, SpaceNet.instance.getSystem().stream().filter(systems -> systems.getName().equals(systemName.toLowerCase())).toList().get(0), texture, level, (IStar) SpaceNet.instance.getBodyFromName(starName), temperature, pressure, distance, type,
                    oxygen, colonies, angle, time, size, rotation));
            stringList.add("planet_"+name);
        }
        return new PlanetRecipe(
                id, name, systemName, texture, level, starName,
                temperature, pressure, distance, type,
                oxygen, colonies, angle, time, size, rotation
        );
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, PlanetRecipe recipe) {
        buf.writeUtf(recipe.name);
        buf.writeUtf(recipe.system);
        buf.writeUtf(recipe.texture.toString());
        buf.writeUtf(recipe.level.name());
        buf.writeUtf(recipe.star);
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
