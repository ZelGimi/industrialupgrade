package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.System;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

import static com.denfop.api.space.SpaceInit.regSystem;
import static com.denfop.recipe.universalrecipe.PlanetSerializer.stringList;

public class SystemSerializer implements RecipeSerializer<SystemRecipe> {

    public static final MapCodec<SystemRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(SystemRecipe::getName),
            Codec.INT.fieldOf("distance").forGetter(SystemRecipe::getDistanceFromStar)
    ).apply(instance, (name, distanceFromStar) -> {
        if (!stringList.contains("system_"+name)) {
            regSystem.add(() -> new System(name, distanceFromStar));
            stringList.add("system_"+name);
        }
        return new SystemRecipe(name, distanceFromStar);
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, SystemRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, SystemRecipe::getName,
                    ByteBufCodecs.VAR_INT, SystemRecipe::getDistanceFromStar,
                    (name, distanceFromStar) -> {
                        if (!stringList.contains("system_"+name)) {
                            regSystem.add(() -> new System(name, distanceFromStar));
                            stringList.add("system_"+name);
                        }
                        return new SystemRecipe(name, distanceFromStar);
                    }
            );


    @Override
    public MapCodec<SystemRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, SystemRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
