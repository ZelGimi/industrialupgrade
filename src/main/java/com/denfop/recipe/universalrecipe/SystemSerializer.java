package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.System;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Collections;

import static com.denfop.api.space.SpaceInit.regSystem;

public class SystemSerializer implements RecipeSerializer<SystemRecipe> {

    public static final MapCodec<SystemRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(SystemRecipe::getName),
            Codec.INT.fieldOf("distance").forGetter(SystemRecipe::getDistanceFromStar)
    ).apply(instance, (name, distanceFromStar) -> {
        regSystem.add(() -> new System(name, distanceFromStar));
        return new SystemRecipe("", Collections.emptyList(), "");
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, SystemRecipe> STREAM_CODEC = StreamCodec.of(SystemSerializer::toNetwork, SystemSerializer::fromNetwork);

    private static SystemRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new SystemRecipe("", new ArrayList<>(), "");
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, SystemRecipe p_320586_) {

    }

    @Override
    public MapCodec<SystemRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, SystemRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
