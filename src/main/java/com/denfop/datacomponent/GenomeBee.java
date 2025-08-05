package com.denfop.datacomponent;


import com.denfop.api.bee.genetics.EnumGenetic;
import com.denfop.api.bee.genetics.GeneticTraits;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record GenomeBee(Map<EnumGenetic, GeneticTraits> geneticTraitsMap) {


    public static final Codec<GenomeBee> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Codec.INT).fieldOf("traits_ordinals")
                            .forGetter(genome -> genome.geneticTraitsMap.values()
                                    .stream()
                                    .map(GeneticTraits::ordinal)
                                    .collect(Collectors.toList()))
            ).apply(instance, GenomeBee::newFromOrdinals)
    );
    public static final StreamCodec<ByteBuf, GenomeBee> STREAM_CODEC =
            StreamCodec.composite(
                    DataComponentsInit.INT_ARRAY,
                    GenomeBee::toIntList,
                    GenomeBee::newFromOrdinals
            );

    private static List<Integer> toIntList(GenomeBee genome) {
        return genome.geneticTraitsMap().values().stream()
                .map(Enum::ordinal)
                .collect(Collectors.toList());
    }

    private static GenomeBee newFromOrdinals(List<Integer> ordinals) {
        Map<EnumGenetic, GeneticTraits> map = new HashMap<>();
        for (int ord : ordinals) {
            GeneticTraits gt = GeneticTraits.values()[ord];
            map.put(gt.getGenetic(), gt);
        }
        return new GenomeBee(map);
    }

    @Override
    public Map<EnumGenetic, GeneticTraits> geneticTraitsMap() {
        return geneticTraitsMap;
    }
}
