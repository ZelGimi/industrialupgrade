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

import static com.denfop.api.space.SpaceInit.regSatellite;
import static com.denfop.recipe.universalrecipe.PlanetSerializer.stringList;

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


        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");
        if (!stringList.contains("satellite_"+name)) {
            regSatellite.add(() -> new Satellite(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr)), texture, level, (IPlanet) SpaceNet.instance.getBodyFromName(planetStr), temperature, pressure, distance,
                    type, oxygen, colonies, angle, time, size, rotation));
            stringList.add("satellite_"+name);
        }
        return new SatelliteRecipe(name, systemStr, textureStr, level, planetStr, temperature, pressure, distance, type, oxygen, colonies, angle, time, size, rotation);
    }));

    public static final StreamCodec<RegistryFriendlyByteBuf, SatelliteRecipe> STREAM_CODEC =
            StreamCodec.of(
                    (buf, recipe) -> {
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.name);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.systemName);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.texturePath);
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.level.name());
                        ByteBufCodecs.STRING_UTF8.encode(buf, recipe.planetName);
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
                        String system1 = ByteBufCodecs.STRING_UTF8.decode(buf);
                        String textureStr = ByteBufCodecs.STRING_UTF8.decode(buf);
                        EnumLevels level = EnumLevels.valueOf(ByteBufCodecs.STRING_UTF8.decode(buf).toUpperCase());
                        String planetStr = ByteBufCodecs.STRING_UTF8.decode(buf);
                        int temperature = ByteBufCodecs.VAR_INT.decode(buf);
                        boolean pressure = ByteBufCodecs.BOOL.decode(buf);
                        double distance = ByteBufCodecs.DOUBLE.decode(buf);
                        EnumType type = EnumType.valueOf(ByteBufCodecs.STRING_UTF8.decode(buf).toUpperCase());
                        boolean oxygen = ByteBufCodecs.BOOL.decode(buf);
                        boolean colonies = ByteBufCodecs.BOOL.decode(buf);
                        int angle = ByteBufCodecs.VAR_INT.decode(buf);
                        double time = ByteBufCodecs.DOUBLE.decode(buf);
                        double size = ByteBufCodecs.DOUBLE.decode(buf);
                        double rotation = ByteBufCodecs.DOUBLE.decode(buf);
                        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");
                        if (!stringList.contains("satellite_"+name)) {
                            regSatellite.add(() -> new Satellite(name, SpaceNet.instance.getSystem().stream()
                                    .filter(s -> s.getName().equals(system1.toLowerCase()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + system1)), texture, level, (IPlanet) SpaceNet.instance.getBodyFromName(planetStr), temperature, pressure, distance,
                                    type, oxygen, colonies, angle, time, size, rotation));
                            stringList.add("satellite_"+name);
                        }
                        return new SatelliteRecipe(
                                name, system1, textureStr, level, planetStr,
                                temperature, pressure, distance, type,
                                oxygen, colonies, angle, time, size, rotation
                        );
                    }
            );


    @Override
    public MapCodec<SatelliteRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, SatelliteRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
