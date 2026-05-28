package com.denfop.datagen.loader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public record CustomOreConfig(
        Block oreBlock,
        Block replaceBlock,
        int minHeight,
        int maxHeight,
        int minVeinSize,
        int maxVeinSize,
        int minVeins,
        int maxVeins,
        double chance
) {
    public static final Codec<CustomOreConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("ore_block").forGetter(CustomOreConfig::oreBlock),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("replace_block").forGetter(CustomOreConfig::replaceBlock),
            Codec.INT.fieldOf("min_height").forGetter(CustomOreConfig::minHeight),
            Codec.INT.fieldOf("max_height").forGetter(CustomOreConfig::maxHeight),
            Codec.INT.fieldOf("min_vein_size").forGetter(CustomOreConfig::minVeinSize),
            Codec.INT.fieldOf("max_vein_size").forGetter(CustomOreConfig::maxVeinSize),
            Codec.INT.fieldOf("min_veins_per_chunk").forGetter(CustomOreConfig::minVeins),
            Codec.INT.fieldOf("max_veins_per_chunk").forGetter(CustomOreConfig::maxVeins),
            Codec.DOUBLE.fieldOf("chance").forGetter(CustomOreConfig::chance)
    ).apply(instance, CustomOreConfig::new));
}
