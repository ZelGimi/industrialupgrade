package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class StringRepresentableCodec {

    private StringRepresentableCodec() {
    }

    public static <E extends Enum<E> & StringRepresentable> Codec<E> of(final E[] values) {
        final Map<String, E> lookup = Arrays.stream(values)
                .collect(Collectors.toMap(StringRepresentable::getSerializedName, Function.identity()));

        return Codec.STRING.xmap(
                key -> {
                    final E value = lookup.get(key);
                    if (value == null) {
                        throw new IllegalArgumentException("Unknown enum key: " + key);
                    }
                    return value;
                },
                StringRepresentable::getSerializedName
        );
    }
}