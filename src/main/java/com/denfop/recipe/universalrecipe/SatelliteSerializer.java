package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Collections;

import static com.denfop.api.space.SpaceInit.regSatellite;

public class SatelliteSerializer implements RecipeSerializer<SatelliteRecipe> {

    public static final MapCodec<SatelliteRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(s -> s.name),
            Codec.STRING.fieldOf("system").forGetter(s -> s.systemName),
            Codec.STRING.fieldOf("texture").forGetter(s -> s.texturePath),
            Codec.STRING.xmap(str -> EnumLevels.valueOf(str.toUpperCase()), EnumLevels::name).fieldOf("level").forGetter(s -> s.level),
            Codec.STRING.fieldOf("planet").forGetter(s -> s.planetName),
            Codec.INT.fieldOf("temperature").forGetter(s -> s.temperature),
            Codec.BOOL.fieldOf("pressure").forGetter(s -> s.pressure),
            Codec.DOUBLE.fieldOf("distance").forGetter(s -> s.distance),
            Codec.STRING.xmap(str -> EnumType.valueOf(str.toUpperCase()), EnumType::name).fieldOf("type").forGetter(s -> s.type),
            Codec.BOOL.fieldOf("oxygen").forGetter(s -> s.oxygen),
            Codec.BOOL.fieldOf("colonies").forGetter(s -> s.colonies),
            Codec.INT.fieldOf("angle").forGetter(s -> s.angle),
            Codec.DOUBLE.fieldOf("time").forGetter(s -> s.time),
            Codec.DOUBLE.fieldOf("size").forGetter(s -> s.size),
            Codec.DOUBLE.fieldOf("rotation").forGetter(s -> s.rotation)
    ).apply(instance, (name, systemStr, textureStr, level, planetStr, temperature, pressure, distance, type, oxygen, colonies, angle, time, size, rotation) -> {
        ISystem system = SpaceNet.instance.getSystem().stream()
                .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr));

        IPlanet planet = (IPlanet) SpaceNet.instance.getBodyFromName(planetStr);
        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");

        regSatellite.add(() -> new Satellite(name, system, texture, level, planet, temperature, pressure, distance,
                type, oxygen, colonies, angle, time, size, rotation));

        return new SatelliteRecipe("", Collections.emptyList(), "");
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, SatelliteRecipe> STREAM_CODEC = StreamCodec.of(SatelliteSerializer::toNetwork, SatelliteSerializer::fromNetwork);

    private static SatelliteRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new SatelliteRecipe("", new ArrayList<>(), "");
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, SatelliteRecipe p_320586_) {

    }

    @Override
    public MapCodec<SatelliteRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, SatelliteRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
