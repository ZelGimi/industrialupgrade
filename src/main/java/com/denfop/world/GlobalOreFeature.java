package com.denfop.world;

import com.denfop.config.ModConfig;
import com.denfop.datagen.loader.CustomOreConfig;
import com.denfop.datagen.loader.OreConfigLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GlobalOreFeature extends Feature<NoneFeatureConfiguration> {

    public GlobalOreFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    private static BlockState getBlockStateFast(WorldGenLevel level, BulkSectionAccess bulk, BlockPos pos) {
        if (level.isOutsideBuildHeight(pos)) {
            return null;
        }

        LevelChunkSection section = bulk.getSection(pos);
        if (section == null) {
            return null;
        }

        return section.getBlockState(
                SectionPos.sectionRelative(pos.getX()),
                SectionPos.sectionRelative(pos.getY()),
                SectionPos.sectionRelative(pos.getZ())
        );
    }

    private static boolean setBlockFast(WorldGenLevel level, BulkSectionAccess bulk, BlockPos pos, BlockState newState) {
        if (level.isOutsideBuildHeight(pos) || !level.ensureCanWrite(pos)) {
            return false;
        }

        LevelChunkSection section = bulk.getSection(pos);
        if (section == null) {
            return false;
        }

        section.setBlockState(
                SectionPos.sectionRelative(pos.getX()),
                SectionPos.sectionRelative(pos.getY()),
                SectionPos.sectionRelative(pos.getZ()),
                newState,
                false
        );

        return true;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        if (!ModConfig.COMMON.defaultSpawnEnabled.get()) {
            return false;
        }

        WorldGenLevel level = ctx.level();
        RandomSource random = ctx.random();
        BlockPos origin = ctx.origin();

        boolean placedAny = false;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        try (BulkSectionAccess bulk = new BulkSectionAccess(level)) {
            for (CustomOreConfig config : OreConfigLoader.ORES.values()) {
                int veins = Mth.nextInt(random, config.minVeins(), config.maxVeins());

                for (int i = 0; i < veins; i++) {
                    if (random.nextDouble() > config.chance()) {
                        continue;
                    }

                    int baseX = origin.getX() + random.nextInt(16);
                    int baseY = Mth.nextInt(random, config.minHeight(), config.maxHeight());
                    int baseZ = origin.getZ() + random.nextInt(16);

                    if (level.isOutsideBuildHeight(baseY)) {
                        continue;
                    }

                    int veinSize = Mth.nextInt(random, config.minVeinSize(), config.maxVeinSize());

                    for (int j = 0; j < veinSize; j++) {
                        int x = baseX + random.nextInt(3) - 1;
                        int y = baseY + random.nextInt(3) - 1;
                        int z = baseZ + random.nextInt(3) - 1;

                        if (level.isOutsideBuildHeight(y)) {
                            continue;
                        }

                        mutablePos.set(x, y, z);

                        if (!level.ensureCanWrite(mutablePos)) {
                            continue;
                        }

                        BlockState currentState = getBlockStateFast(level, bulk, mutablePos);
                        if (currentState.is(config.replaceBlock())) {
                            if (setBlockFast(level, bulk, mutablePos, config.oreBlock().defaultBlockState())) {
                                placedAny = true;
                            }
                        }
                    }
                }
            }
        }

        return placedAny;
    }
}