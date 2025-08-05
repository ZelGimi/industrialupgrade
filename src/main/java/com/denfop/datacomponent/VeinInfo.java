package com.denfop.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record VeinInfo(String type, int x, int z) {
    public static final Codec<VeinInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("type").forGetter(VeinInfo::type),
            Codec.INT.fieldOf("x").forGetter(VeinInfo::x),
            Codec.INT.fieldOf("z").forGetter(VeinInfo::z)
    ).apply(instance, VeinInfo::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, VeinInfo> STREAM_CODEC = StreamCodec.of(
            (buf, info) -> {
                buf.writeUtf(info.type());
                buf.writeInt(info.x());
                buf.writeInt(info.z());
            },
            buf -> new VeinInfo(
                    buf.readUtf(),
                    buf.readInt(),
                    buf.readInt()
            )
    );
}
