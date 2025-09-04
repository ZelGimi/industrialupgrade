package com.denfop.datacomponent;

import com.denfop.api.crop.genetics.EnumGenetic;
import com.denfop.api.crop.genetics.GeneticTraits;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record GenomeCrop(Map<EnumGenetic, GeneticTraits> geneticTraitsMap) {


    public static final Codec<GenomeCrop> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Codec.INT).fieldOf("traits_ordinals")
                            .forGetter(genome -> genome.geneticTraitsMap.values()
                                    .stream()
                                    .map(GeneticTraits::ordinal)
                                    .collect(Collectors.toList()))
            ).apply(instance, GenomeCrop::newFromOrdinals)
    );
    public static final StreamCodec<ByteBuf, GenomeCrop> STREAM_CODEC =
            StreamCodec.composite(
                    DataComponentsInit.INT_ARRAY,
                    GenomeCrop::toIntList,
                    GenomeCrop::newFromOrdinals
            );

    private static List<Integer> toIntList(GenomeCrop genome) {
        return genome.geneticTraitsMap().values().stream()
                .map(Enum::ordinal)
                .collect(Collectors.toList());
    }

    private static GenomeCrop newFromOrdinals(List<Integer> ordinals) {
        Map<EnumGenetic, GeneticTraits> map = new HashMap<>();
        for (int ord : ordinals) {
            GeneticTraits gt = GeneticTraits.values()[ord];
            map.put(gt.getGenetic(), gt);
        }
        return new GenomeCrop(map);
    }

    @Override
    public Map<EnumGenetic, GeneticTraits> geneticTraitsMap() {
        return geneticTraitsMap;
    }
}
