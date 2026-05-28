package com.denfop.datacomponent;

import com.denfop.api.bee.genetics.EnumGenetic;
import com.denfop.api.bee.genetics.GeneticTraits;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public record GenomeBee(Map<EnumGenetic, GeneticTraits> geneticTraitsMap) {

    public static final Codec<GenomeBee> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Codec.INT)
                            .fieldOf("traits_ordinals")
                            .forGetter(GenomeBee::toIntList)
            ).apply(instance, GenomeBee::newFromOrdinals)
    );
    public static final StreamCodec<ByteBuf, GenomeBee> STREAM_CODEC =
            StreamCodec.composite(
                    DataComponentsInit.INT_ARRAY,
                    GenomeBee::toIntList,
                    GenomeBee::newFromOrdinals
            );

    public GenomeBee {
        EnumMap<EnumGenetic, GeneticTraits> copy = new EnumMap<>(EnumGenetic.class);
        if (geneticTraitsMap != null) {
            copy.putAll(geneticTraitsMap);
        }
        geneticTraitsMap = Collections.unmodifiableMap(copy);
    }

    private static List<Integer> toIntList(GenomeBee genome) {
        return genome.geneticTraitsMap.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().ordinal()))
                .map(e -> e.getValue().ordinal())
                .toList();
    }

    private static GenomeBee newFromOrdinals(List<Integer> ordinals) {
        EnumMap<EnumGenetic, GeneticTraits> map = new EnumMap<>(EnumGenetic.class);

        for (int ord : ordinals) {
            if (ord < 0 || ord >= GeneticTraits.values().length) {
                continue;
            }
            GeneticTraits gt = GeneticTraits.values()[ord];
            map.put(gt.getGenetic(), gt);
        }

        return new GenomeBee(map);
    }

    public boolean isEmpty() {
        return this.geneticTraitsMap.isEmpty();
    }
}