package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.ISystem;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.Star;
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

import static com.denfop.api.space.SpaceInit.regStar;
import static com.denfop.recipe.universalrecipe.PlanetSerializer.stringList;

public class StarSerializer implements RecipeSerializer<StarRecipe> {

    public static final MapCodec<StarRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(recipe -> recipe.name),
            Codec.STRING.fieldOf("system").forGetter(recipe -> recipe.systemName),
            Codec.STRING.fieldOf("texture").forGetter(recipe -> recipe.texturePath),
            Codec.INT.fieldOf("angle").forGetter(recipe -> recipe.angle),
            Codec.DOUBLE.fieldOf("size").forGetter(recipe -> recipe.size)
    ).apply(instance, (name, systemStr, textureStr, angle, size) -> {

        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");
        if (!stringList.contains("star_"+name)) {
            regStar.add(() -> new Star(name, SpaceNet.instance.getSystem().stream()
                    .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr)), texture, angle, size));
            stringList.add("star_"+name);
        }
        return new StarRecipe(name, systemStr, textureStr, angle, size);
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, StarRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, recipe -> recipe.name,
                    ByteBufCodecs.STRING_UTF8, recipe -> recipe.systemName,
                    ByteBufCodecs.STRING_UTF8, recipe ->recipe.texturePath,
                    ByteBufCodecs.VAR_INT, recipe -> recipe.angle,
                    ByteBufCodecs.DOUBLE, recipe -> recipe.size,
                    (name, systemStr, textureStr, angle, size) -> {
                        ISystem system = SpaceNet.instance.getSystem().stream()
                                .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr));

                        ResourceLocation texture = ResourceLocation.parse(textureStr+ ".png");
                        if (!stringList.contains("star_"+name)) {
                            regStar.add(() -> new Star(name, system, texture, angle, size));
                            stringList.add("star_"+name);
                        }
                        return new StarRecipe(name, systemStr, textureStr, angle, size);
                    }
            );


    @Override
    public MapCodec<StarRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, StarRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
