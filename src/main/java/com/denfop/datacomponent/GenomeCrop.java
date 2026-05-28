package com.denfop.datacomponent;

import com.denfop.api.crop.genetics.EnumGenetic;
import com.denfop.api.crop.genetics.GeneticTraits;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public record GenomeCrop(Map<EnumGenetic, GeneticTraits> geneticTraitsMap) {

    public static final Codec<GenomeCrop> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Codec.INT)
                            .fieldOf("traits_ordinals")
                            .forGetter(GenomeCrop::toIntList)
            ).apply(instance, GenomeCrop::newFromOrdinals)
    );
    public static final StreamCodec<ByteBuf, GenomeCrop> STREAM_CODEC =
            StreamCodec.composite(
                    DataComponentsInit.INT_ARRAY,
                    GenomeCrop::toIntList,
                    GenomeCrop::newFromOrdinals
            );

    public GenomeCrop {
        EnumMap<EnumGenetic, GeneticTraits> copy = new EnumMap<>(EnumGenetic.class);
        if (geneticTraitsMap != null) {
            copy.putAll(geneticTraitsMap);
        }
        geneticTraitsMap = Collections.unmodifiableMap(copy);
    }

    private static List<Integer> toIntList(GenomeCrop genome) {
        return genome.geneticTraitsMap.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().ordinal()))
                .map(e -> e.getValue().ordinal())
                .toList();
    }

    private static GenomeCrop newFromOrdinals(List<Integer> ordinals) {
        EnumMap<EnumGenetic, GeneticTraits> map = new EnumMap<>(EnumGenetic.class);

        for (int ord : ordinals) {
            if (ord < 0 || ord >= GeneticTraits.values().length) {
                continue;
            }
            GeneticTraits gt = GeneticTraits.values()[ord];
            map.put(gt.getGenetic(), gt);
        }

        return new GenomeCrop(map);
    }

    public boolean isEmpty() {
        return this.geneticTraitsMap.isEmpty();
    }
}