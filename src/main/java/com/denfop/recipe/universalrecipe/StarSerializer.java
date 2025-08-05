package com.denfop.recipe.universalrecipe;

import com.denfop.api.space.ISystem;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.Star;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Collections;

import static com.denfop.api.space.SpaceInit.regStar;

public class StarSerializer implements RecipeSerializer<StarRecipe> {

    public static final MapCodec<StarRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(recipe -> recipe.name),
            Codec.STRING.fieldOf("system").forGetter(recipe -> recipe.systemName),
            Codec.STRING.fieldOf("texture").forGetter(recipe -> recipe.texturePath),
            Codec.INT.fieldOf("angle").forGetter(recipe -> recipe.angle),
            Codec.DOUBLE.fieldOf("size").forGetter(recipe -> recipe.size)
    ).apply(instance, (name, systemStr, textureStr, angle, size) -> {
        ISystem system = SpaceNet.instance.getSystem().stream()
                .filter(s -> s.getName().equals(systemStr.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("System not found: " + systemStr));

        ResourceLocation texture = ResourceLocation.parse(textureStr + ".png");

        regStar.add(() -> new Star(name, system, texture, angle, size));

        return new StarRecipe("", Collections.emptyList(), "");
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, StarRecipe> STREAM_CODEC = StreamCodec.of(StarSerializer::toNetwork, StarSerializer::fromNetwork);

    private static StarRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {

        return new StarRecipe("", new ArrayList<>(), "");
    }

    private static void toNetwork(RegistryFriendlyByteBuf p_320738_, StarRecipe p_320586_) {

    }

    @Override
    public MapCodec<StarRecipe> codec() {
        return MAP_CODEC;
    }

    public StreamCodec<RegistryFriendlyByteBuf, StarRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
