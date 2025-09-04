package com.denfop.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.LinkedList;
import java.util.List;

import static com.denfop.world.vein.AlgorithmVein.volcano;

public class WorldGenVolcano extends Feature<NoneFeatureConfiguration> {
    public static List<GeneratorVolcano> generatorVolcanoList = new LinkedList<>();

    public WorldGenVolcano(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159749_) {
        int y = p_159749_.level().getHeight(Heightmap.Types.WORLD_SURFACE_WG, p_159749_.origin().getX(), p_159749_.origin().getZ());

        Holder<Biome> holder = p_159749_.level().getBiome(new BlockPos(p_159749_.origin().getX(), y, p_159749_.origin().getZ()));
        DimensionType dim = p_159749_.level().registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).get(BuiltinDimensionTypes.OVERWORLD);
        if ((holder.is(BiomeTags.IS_MOUNTAIN) || holder.is(BiomeTags.IS_HILL)) && p_159749_.level().dimensionType() == dim) {
            ChunkPos pos = new ChunkPos(p_159749_.origin());
            if (volcano != null) {
                if (volcano.point.x == Math.abs(pos.x) % 24 && volcano.point.y == Math.abs(pos.z) % 24) {
                    generatorVolcanoList.add(new GeneratorVolcano(p_159749_));
                    return true;
                }
            }


        }
        return false;
    }
}
