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

import static com.denfop.api.space.SpaceInit.regPlanet;

public class PlanetSerializer implements RecipeSerializer<PlanetRecipe> {

    public static final MapCodec<PlanetRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(p -> p.name),
            Codec.STRING.fieldOf("system").forGetter(p -> p.systemName),
            Codec.STRING.fieldOf("texture").forGetter(p -> p.texturePath),
            Codec.STRING.xmap(EnumLevels::valueOf, EnumLevels::name)
                    .fieldOf("level").forGetter(p -> p.level),
            Codec.STRING.fieldOf("star").forGetter(p -> p.starName),
            Codec.INT.fieldOf("temperature").forGetter(p -> p.temperature),
            Codec.BOOL.fieldOf("pressure").forGetter(p -> p.pressure),
            Codec.DOUBLE.fieldOf("distance").forGetter(p -> p.distance),
            Codec.STRING.xmap(EnumType::valueOf, EnumType::name)
                    .fieldOf("type").forGetter(p -> p.type),
            Codec.BOOL.fieldOf("oxygen").forGetter(p -> p.oxygen),
            Codec.BOOL.fieldOf("colonies").forGetter(p -> p.colonies),
            Codec.INT.fieldOf("angle").forGetter(p -> p.angle),
            Codec.DOUBLE.fieldOf("time").forGetter(p -> p.time),
            Codec.DOUBLE.fieldOf("size").forGetter(p -> p.size),
            Codec.DOUBLE.fieldOf("rotation").forGetter(p -> p.rotation)
    ).apply(instance, (name, systemStr, textureStr, levelStr, starStr, temp, pressure, distance, typeStr, oxygen, colonies, angle, time, size, rotation) -> {
        ISystem system = SpaceNet.instance.getSystem().stream()
                .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr));

        IStar star = (IStar) SpaceNet.instance.getBodyFromName(starStr);


        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");

        regPlanet.add(() -> new Planet(name, system, texture, levelStr, star, temp, pressure, distance, typeStr,
                oxygen, colonies, angle, time, size, rotation));

        return new PlanetRecipe("", Collections.emptyList(), "");
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlanetRecipe> STREAM_CODEC = StreamCodec.of(PlanetSerializer::toNetwork, PlanetSerializer::fromNetwork);

    private static PlanetRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new PlanetRecipe("", new ArrayList<>(), "");
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, PlanetRecipe p_320586_) {

    }

    @Override
    public MapCodec<PlanetRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, PlanetRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
