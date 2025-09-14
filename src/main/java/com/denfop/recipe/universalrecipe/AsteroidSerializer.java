package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Collections;

import static com.denfop.api.space.SpaceInit.regAsteroid;
import static com.denfop.recipe.universalrecipe.PlanetSerializer.stringList;

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



        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");

        if (!stringList.contains("asteroid_"+name)) {
            regAsteroid.add(() -> new Asteroid(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr)), texture, level, (IStar) SpaceNet.instance.getBodyFromName(starStr), temperature,
                    distance, type, colonies, angle, time, size, rotation, minLocation, maxLocation, amount));
            stringList.add("asteroid_"+name);
        }
        return new AsteroidRecipe(name, systemStr, textureStr, level, starStr, temperature, distance, type, colonies, angle,
                time, size, rotation, minLocation, maxLocation, amount);
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, AsteroidRecipe> STREAM_CODEC =
            StreamCodec.of(
                    (buf, recipe) -> {
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.name);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.systemName);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.texturePath);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.level.name());
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.starName);
                        ByteBufCodecs.VAR_INT.encode(buf, recipe.temperature);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.distance);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.type.name());
                        ByteBufCodecs.BOOL.encode(buf, recipe.colonies);
                        ByteBufCodecs.VAR_INT.encode(buf, recipe.angle);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.time);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.size);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.rotation);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.minLocation);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.maxLocation);
                        ByteBufCodecs.VAR_INT.encode(buf, recipe.amount);
                    },
                    buf -> {
                        String name = ByteBufCodecs.STRING_UTF8.decode(buf);
                        String system = ByteBufCodecs.STRING_UTF8.decode(buf);
                        String texture = ByteBufCodecs.STRING_UTF8.decode(buf);
                        EnumLevels level = EnumLevels.valueOf(ByteBufCodecs.STRING_UTF8.decode(buf).toUpperCase());
                        String star = ByteBufCodecs.STRING_UTF8.decode(buf);
                        int temperature = ByteBufCodecs.VAR_INT.decode(buf);
                        double distance = ByteBufCodecs.DOUBLE.decode(buf);
                        EnumType type = EnumType.valueOf(ByteBufCodecs.STRING_UTF8.decode(buf).toUpperCase());
                        boolean colonies = ByteBufCodecs.BOOL.decode(buf);
                        int angle = ByteBufCodecs.VAR_INT.decode(buf);
                        double time = ByteBufCodecs.DOUBLE.decode(buf);
                        double size = ByteBufCodecs.DOUBLE.decode(buf);
                        double rotation = ByteBufCodecs.DOUBLE.decode(buf);
                        double minLocation = ByteBufCodecs.DOUBLE.decode(buf);
                        double maxLocation = ByteBufCodecs.DOUBLE.decode(buf);
                        int amount = ByteBufCodecs.VAR_INT.decode(buf);
                        if (!stringList.contains("asteroid_"+name)) {
                            regAsteroid.add(() -> new Asteroid(name, SpaceNet.instance.getSystem().stream()
                                    .filter(s -> s.getName().equals(system.toLowerCase()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + system)), ResourceLocation.parse(texture+ ".png"), level, (IStar) SpaceNet.instance.getBodyFromName(star), temperature,
                                    distance, type, colonies, angle, time, size, rotation, minLocation, maxLocation, amount));
                            stringList.add("asteroid_"+name);
                        }
                        return new AsteroidRecipe(
                                name, system, texture, level, star,
                                temperature, distance, type, colonies,
                                angle, time, size, rotation,
                                minLocation, maxLocation, amount
                        );
                    }
            );

    @Override
    public MapCodec<AsteroidRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, AsteroidRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
