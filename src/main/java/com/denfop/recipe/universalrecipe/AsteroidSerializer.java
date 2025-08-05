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

import static com.denfop.api.space.SpaceInit.regAsteroid;

public class AsteroidSerializer implements RecipeSerializer<AsteroidRecipe> {

    public static final MapCodec<AsteroidRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(a -> a.name),
            Codec.STRING.fieldOf("system").forGetter(a -> a.systemName),
            Codec.STRING.fieldOf("texture").forGetter(a -> a.texturePath),
            Codec.STRING.xmap(s -> EnumLevels.valueOf(s.toUpperCase()), EnumLevels::name).fieldOf("level").forGetter(a -> a.level),
            Codec.STRING.fieldOf("star").forGetter(a -> a.starName),
            Codec.INT.fieldOf("temperature").forGetter(a -> a.temperature),
            Codec.DOUBLE.fieldOf("distance").forGetter(a -> a.distance),
            Codec.STRING.xmap(s -> EnumType.valueOf(s.toUpperCase()), EnumType::name).fieldOf("type").forGetter(a -> a.type),
            Codec.BOOL.fieldOf("colonies").forGetter(a -> a.colonies),
            Codec.INT.fieldOf("angle").forGetter(a -> a.angle),
            Codec.DOUBLE.fieldOf("time").forGetter(a -> a.time),
            Codec.DOUBLE.fieldOf("size").forGetter(a -> a.size),
            Codec.DOUBLE.fieldOf("rotation").forGetter(a -> a.rotation),
            Codec.DOUBLE.fieldOf("minLocation").forGetter(a -> a.minLocation),
            Codec.DOUBLE.fieldOf("maxLocation").forGetter(a -> a.maxLocation),
            Codec.INT.fieldOf("amount").forGetter(a -> a.amount)
    ).apply(instance, (name, systemStr, textureStr, level, starStr, temperature, distance, type, colonies, angle,
                       time, size, rotation, minLocation, maxLocation, amount) -> {
        ISystem system = SpaceNet.instance.getSystem().stream()
                .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr));

        IStar star = (IStar) SpaceNet.instance.getBodyFromName(starStr);
        if (star == null) throw new IllegalArgumentException("Star not found: " + starStr);

        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");


        regAsteroid.add(() -> new Asteroid(name, system, texture, level, star, temperature,
                distance, type, colonies, angle, time, size, rotation, minLocation, maxLocation, amount));

        return new AsteroidRecipe("", Collections.emptyList(), "");
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, AsteroidRecipe> STREAM_CODEC = StreamCodec.of(AsteroidSerializer::toNetwork, AsteroidSerializer::fromNetwork);

    private static AsteroidRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new AsteroidRecipe("", new ArrayList<>(), "");
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, AsteroidRecipe p_320586_) {

    }

    @Override
    public MapCodec<AsteroidRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, AsteroidRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
