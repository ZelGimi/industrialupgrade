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
import java.util.List;

import static com.denfop.api.space.SpaceInit.regPlanet;

public class PlanetSerializer implements RecipeSerializer<PlanetRecipe> {
    public static List<String> stringList = new ArrayList<>();
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

        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");
        if (!stringList.contains("planet_"+name)) {
            regPlanet.add(() -> new Planet(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr)), texture, levelStr, (IStar) SpaceNet.instance.getBodyFromName(starStr), temp, pressure, distance, typeStr,
                    oxygen, colonies, angle, time, size, rotation));
            stringList.add("planet_"+name);
        }
        return new PlanetRecipe(name, systemStr, textureStr, levelStr, starStr, temp, pressure, distance, typeStr, oxygen, colonies, angle, time, size, rotation);
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlanetRecipe> STREAM_CODEC =
            StreamCodec.of(
                    (buf, recipe) -> {
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.name);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.systemName);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.texturePath);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.level.name());
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.starName);
                        ByteBufCodecs.VAR_INT.encode(buf, recipe.temperature);
                        ByteBufCodecs.BOOL.encode(buf, recipe.pressure);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.distance);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.type.name());
                        ByteBufCodecs.BOOL.encode(buf, recipe.oxygen);
                        ByteBufCodecs.BOOL.encode(buf, recipe.colonies);
                        ByteBufCodecs.VAR_INT.encode(buf, recipe.angle);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.time);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.size);
                        ByteBufCodecs.DOUBLE.encode(buf, recipe.rotation);
                    },
                    buf -> {
                        String name = ByteBufCodecs.STRING_UTF8.decode(buf);
                        String systemStr = ByteBufCodecs.STRING_UTF8.decode(buf);
                        String textureStr = ByteBufCodecs.STRING_UTF8.decode(buf);
                        EnumLevels level = EnumLevels.valueOf(ByteBufCodecs.STRING_UTF8.decode(buf));
                        String starStr = ByteBufCodecs.STRING_UTF8.decode(buf);
                        int temp = ByteBufCodecs.VAR_INT.decode(buf);
                        boolean pressure = ByteBufCodecs.BOOL.decode(buf);
                        double distance = ByteBufCodecs.DOUBLE.decode(buf);
                        EnumType typeStr = EnumType.valueOf(ByteBufCodecs.STRING_UTF8.decode(buf));
                        boolean oxygen = ByteBufCodecs.BOOL.decode(buf);
                        boolean colonies = ByteBufCodecs.BOOL.decode(buf);
                        int angle = ByteBufCodecs.VAR_INT.decode(buf);
                        double time = ByteBufCodecs.DOUBLE.decode(buf);
                        double size = ByteBufCodecs.DOUBLE.decode(buf);
                        double rotation = ByteBufCodecs.DOUBLE.decode(buf);
                        ResourceLocation texture = ResourceLocation.parse(textureStr+ ".png");
                        if (!stringList.contains("planet_"+name)) {
                            regPlanet.add(() -> new Planet(name, SpaceNet.instance.getSystem().stream()
                                    .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr)), texture, level, (IStar) SpaceNet.instance.getBodyFromName(starStr), temp, pressure, distance, typeStr,
                                    oxygen, colonies, angle, time, size, rotation));
                            stringList.add("planet_"+name);
                        }
                        return new PlanetRecipe(
                                name, systemStr, textureStr, level, starStr,
                                temp, pressure, distance, typeStr,
                                oxygen, colonies, angle, time, size, rotation
                        );
                    }
            );

    @Override
    public MapCodec<PlanetRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, PlanetRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
