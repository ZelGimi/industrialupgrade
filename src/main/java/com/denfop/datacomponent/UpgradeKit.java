package com.denfop.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record UpgradeKit(ItemStack input, ItemStack output) {
    public static final Codec<UpgradeKit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("input").forGetter(UpgradeKit::input),
            ItemStack.CODEC.fieldOf("output").forGetter(UpgradeKit::output)
    ).apply(instance, UpgradeKit::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeKit> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            UpgradeKit::input,
            ItemStack.STREAM_CODEC,
            UpgradeKit::output,
            UpgradeKit::new
    );
}

