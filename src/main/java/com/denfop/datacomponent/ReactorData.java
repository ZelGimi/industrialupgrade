package com.denfop.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ReactorData(BlockPos pos, String name) {
    public static final Codec<ReactorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(ReactorData::pos),
            Codec.STRING.fieldOf("name").forGetter(ReactorData::name)
    ).apply(instance, ReactorData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ReactorData> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ReactorData::pos,
            ByteBufCodecs.STRING_UTF8,
            ReactorData::name,
            ReactorData::new
    );
}
