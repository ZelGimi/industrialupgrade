package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.worldgen.SpaceOreFeatureConfig;
import com.denfop.api.space.dimension.worldgen.SpaceWorldgenFluidHelper;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class SpaceGenericOreFeature extends Feature<SpaceOreFeatureConfig> {


    private static final float COUNT_SCALE = 0.45F;
    private static final float SIZE_SCALE = 0.72F;
    private static final float CHANCE_SCALE = 0.75F;

    public SpaceGenericOreFeature(final Codec<SpaceOreFeatureConfig> codec) {
        super(codec);
    }

    private static int scaledInt(final int base, final int min) {
        return Math.max(min, Mth.ceil(base * 0.1f));
    }

    private static float scaledRadius(final float base, final float min) {
        return Math.max(min, base * 0.1f);
    }

    private static float scaledChance(final int y, final SpaceOreFeatureConfig config) {
        final float depthFactor =
                1.0F - (y - config.minY()) / (float) Math.max(1, config.maxY() + 30 - config.minY());
        final float base =
                Mth.clamp(config.baseChance() + depthFactor * config.depthBonus(), 0.0F, 1.0F);
        return Mth.clamp(base * 0.4f, 0.0F, 1.0F);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceOreFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final BlockPos origin = context.origin();
        final SpaceOreFeatureConfig config = context.config();
        final RandomSource random = context.random();

        if (origin.getY() < config.minY() || origin.getY() > config.maxY() + 30) {
            return false;
        }

        final ResourceLocation oreId = ResourceLocation.tryParse(config.oreBlockId());
        if (oreId == null) {
            return false;
        }

        final Block oreBlock = BuiltInRegistries.BLOCK.get(oreId);
        if (oreBlock == Blocks.AIR) {
            return false;
        }

        final Set<Block> replaceable = resolveReplaceable(config.replaceableBlockIds());
        final BlockState oreState = oreBlock.defaultBlockState();


        return placeCaveWall(level, origin, random, oreState, replaceable, config);
    }

    private Set<Block> resolveReplaceable(final List<String> ids) {
        final Set<Block> replaceable = new HashSet<>();

        for (final String id : ids) {
            final ResourceLocation key = ResourceLocation.tryParse(id);
            if (key == null) {
                continue;
            }

            final Block block = BuiltInRegistries.BLOCK.get(key);
            if (block != Blocks.AIR) {
                replaceable.add(block);
            }
        }

        if (replaceable.isEmpty()) {
            replaceable.add(Blocks.STONE);
            replaceable.add(Blocks.DEEPSLATE);
            replaceable.add(Blocks.TUFF);
        }

        return replaceable;
    }

    private boolean placeVein(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        final float angle = random.nextFloat() * (float) Math.PI;

        final float reach = Math.max(1.1F, (config.pathLength() / 8.0F) * SIZE_SCALE);
        final int padding = Mth.ceil(((reach * 2.0F + 1.0F) / 2.0F));

        final double minX = origin.getX() + Math.sin(angle) * reach;
        final double maxX = origin.getX() - Math.sin(angle) * reach;
        final double minZ = origin.getZ() + Math.cos(angle) * reach;
        final double maxZ = origin.getZ() - Math.cos(angle) * reach;

        final double minY = origin.getY() + random.nextInt(3) - 2;
        final double maxY = origin.getY() + random.nextInt(3) - 2;

        final int scaledVerticalRadius = scaledInt(Math.max(2, config.verticalRadius()), 1);

        final int startX = origin.getX() - Mth.ceil(reach) - padding;
        final int startY = origin.getY() - scaledVerticalRadius - padding;
        final int startZ = origin.getZ() - Mth.ceil(reach) - padding;

        final int width = 2 * (Mth.ceil(reach) + padding);
        final int height = 2 * (scaledVerticalRadius + padding);

        for (int x = startX; x <= startX + width; ++x) {
            for (int z = startZ; z <= startZ + width; ++z) {
                if (startY <= level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z)) {
                    return doPlaceVanillaLikeVein(
                            level,
                            random,
                            ore,
                            replaceable,
                            config,
                            minX,
                            maxX,
                            minZ,
                            maxZ,
                            minY,
                            maxY,
                            startX,
                            startY,
                            startZ,
                            width,
                            height
                    );
                }
            }
        }

        return false;
    }

    private boolean doPlaceVanillaLikeVein(
            final WorldGenLevel level,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config,
            final double minX,
            final double maxX,
            final double minZ,
            final double maxZ,
            final double minY,
            final double maxY,
            final int startX,
            final int startY,
            final int startZ,
            final int width,
            final int height
    ) {
        int placed = 0;
        final int nodeCount = scaledInt(Math.max(4, config.size()), 2);
        final BitSet bitset = new BitSet(width * height * width);
        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        final double[] nodes = new double[nodeCount * 4];

        for (int i = 0; i < nodeCount; ++i) {
            final float t = (float) i / (float) nodeCount;

            final double x = Mth.lerp(t, minX, maxX);
            final double y = Mth.lerp(t, minY, maxY);
            final double z = Mth.lerp(t, minZ, maxZ);

            final double thicknessBase =
                    (0.75D + random.nextDouble() * 0.5D)
                            * Math.max(1.0D, config.horizontalRadius() * SIZE_SCALE);

            final double radius = ((Mth.sin((float) Math.PI * t) + 1.0F) * thicknessBase + 1.0D) / 2.0D;

            nodes[i * 4] = x;
            nodes[i * 4 + 1] = y;
            nodes[i * 4 + 2] = z;
            nodes[i * 4 + 3] = radius;
        }

        for (int i = 0; i < nodeCount - 1; ++i) {
            if (nodes[i * 4 + 3] <= 0.0D) {
                continue;
            }

            for (int j = i + 1; j < nodeCount; ++j) {
                if (nodes[j * 4 + 3] <= 0.0D) {
                    continue;
                }

                final double dx = nodes[i * 4] - nodes[j * 4];
                final double dy = nodes[i * 4 + 1] - nodes[j * 4 + 1];
                final double dz = nodes[i * 4 + 2] - nodes[j * 4 + 2];
                final double dr = nodes[i * 4 + 3] - nodes[j * 4 + 3];

                if (dr * dr > dx * dx + dy * dy + dz * dz) {
                    if (dr > 0.0D) {
                        nodes[j * 4 + 3] = -1.0D;
                    } else {
                        nodes[i * 4 + 3] = -1.0D;
                    }
                }
            }
        }

        try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(level)) {
            for (int i = 0; i < nodeCount; ++i) {
                final double radius = nodes[i * 4 + 3];
                if (radius < 0.0D) {
                    continue;
                }

                final double centerX = nodes[i * 4];
                final double centerY = nodes[i * 4 + 1];
                final double centerZ = nodes[i * 4 + 2];

                final int minBlockX = Math.max(Mth.floor(centerX - radius), startX);
                final int minBlockY = Math.max(Mth.floor(centerY - radius), startY);
                final int minBlockZ = Math.max(Mth.floor(centerZ - radius), startZ);

                final int maxBlockX = Math.max(Mth.floor(centerX + radius), minBlockX);
                final int maxBlockY = Math.max(Mth.floor(centerY + radius), minBlockY);
                final int maxBlockZ = Math.max(Mth.floor(centerZ + radius), minBlockZ);

                for (int x = minBlockX; x <= maxBlockX; ++x) {
                    final double normX = ((double) x + 0.5D - centerX) / radius;
                    final double normXSq = normX * normX;
                    if (normXSq >= 1.0D) {
                        continue;
                    }

                    for (int y = minBlockY; y <= maxBlockY; ++y) {
                        final double normY = ((double) y + 0.5D - centerY) / radius;
                        final double normXYSq = normXSq + normY * normY;
                        if (normXYSq >= 1.0D) {
                            continue;
                        }

                        for (int z = minBlockZ; z <= maxBlockZ; ++z) {
                            final double normZ = ((double) z + 0.5D - centerZ) / radius;
                            final double normXYZSq = normXYSq + normZ * normZ;

                            if (normXYZSq >= 1.0D || level.isOutsideBuildHeight(y)) {
                                continue;
                            }

                            final int bitIndex =
                                    (x - startX)
                                            + (y - startY) * width
                                            + (z - startZ) * width * height;

                            if (bitset.get(bitIndex)) {
                                continue;
                            }

                            bitset.set(bitIndex);
                            mutablePos.set(x, y, z);

                            if (!level.ensureCanWrite(mutablePos)) {
                                continue;
                            }

                            final LevelChunkSection section = bulkSectionAccess.getSection(mutablePos);
                            if (section == null) {
                                continue;
                            }

                            final int localX = SectionPos.sectionRelative(x);
                            final int localY = SectionPos.sectionRelative(y);
                            final int localZ = SectionPos.sectionRelative(z);
                            final BlockState currentState = section.getBlockState(localX, localY, localZ);

                            if (canPlaceCustomOre(
                                    currentState,
                                    bulkSectionAccess::getBlockState,
                                    random,
                                    config,
                                    replaceable,
                                    mutablePos
                            )) {
                                section.setBlockState(localX, localY, localZ, ore, false);
                                placed++;
                            }
                        }
                    }
                }
            }
        }

        return placed > 0;
    }

    private boolean canPlaceCustomOre(
            final BlockState currentState,
            final Function<BlockPos, BlockState> adjacentStateAccessor,
            final RandomSource random,
            final SpaceOreFeatureConfig config,
            final Set<Block> replaceable,
            final BlockPos.MutableBlockPos pos
    ) {
        if (!replaceable.contains(currentState.getBlock())) {
            return false;
        }

        final AdjacentStateLevelAccessor level = adjacencyLevelAccessor(adjacentStateAccessor);

        if (config.requireNearAir() && !level.isAdjacentToAir(pos)) {
            return false;
        }

        if (config.caveOnly() && !level.isAdjacentToAir(pos)) {
            return false;
        }

        if (config.requireNearFluid() && !level.isAdjacentToFluid(pos)) {
            return false;
        }

        if (config.requireNearLava() && !level.isAdjacentToLava(pos)) {
            return false;
        }

        final float chance = scaledChance(pos.getY(), config);
        return random.nextFloat() <= chance;
    }

    private AdjacentStateLevelAccessor adjacencyLevelAccessor(final Function<BlockPos, BlockState> accessor) {
        return new AdjacentStateLevelAccessor(accessor);
    }

    private boolean placeCluster(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        final int lobes = scaledInt(Math.max(3, config.size() / 2), 1);
        final int horizontalRadius = scaledInt(Math.max(1, config.horizontalRadius()), 1);
        final int verticalRadius = scaledInt(Math.max(1, config.verticalRadius()), 1);

        int placed = 0;

        try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(level)) {
            for (int i = 0; i < lobes; i++) {
                final BlockPos center = origin.offset(
                        random.nextIntBetweenInclusive(-horizontalRadius, horizontalRadius),
                        random.nextIntBetweenInclusive(-verticalRadius, verticalRadius),
                        random.nextIntBetweenInclusive(-horizontalRadius, horizontalRadius)
                );

                final float radius = scaledRadius(
                        1.8F + random.nextFloat() * Math.max(2.0F, config.size() * 0.18F),
                        1.15F
                );

                placed += placeBlob(level, bulkSectionAccess, center, radius, ore, replaceable, random, config);
            }
        }

        return placed > 0;
    }

    private boolean placeLayered(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        int placed = 0;
        final float tiltX = random.nextFloat() * 0.35F - 0.175F;
        final float tiltZ = random.nextFloat() * 0.35F - 0.175F;
        final int halfWidth = scaledInt(Math.max(4, config.horizontalRadius()), 2);
        final int verticalRadius = scaledInt(Math.max(1, config.verticalRadius()), 1);

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(level)) {
            for (int layer = -verticalRadius; layer <= verticalRadius; layer++) {
                final int y = origin.getY() + layer;

                for (int x = -halfWidth; x <= halfWidth; x++) {
                    for (int z = -halfWidth; z <= halfWidth; z++) {
                        final double shiftedY = y + x * tiltX + z * tiltZ;
                        if (Math.abs(shiftedY - origin.getY()) > verticalRadius + 0.75D) {
                            continue;
                        }
                        if ((x * x + z * z) > halfWidth * halfWidth) {
                            continue;
                        }

                        pos.set(origin.getX() + x, Mth.floor(shiftedY), origin.getZ() + z);
                        if (tryPlaceOre(level, bulkSectionAccess, pos, ore, replaceable, random, config)) {
                            placed++;
                        }
                    }
                }
            }
        }

        return placed > 0;
    }

    private boolean placeScattered(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        int placed = 0;
        final int attempts = scaledInt(config.size() * 3, 1);
        final int horizontalRadius = scaledInt(Math.max(1, config.horizontalRadius() * 2), 1);
        final int verticalRadius = scaledInt(Math.max(1, config.verticalRadius()), 1);

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(level)) {
            for (int i = 0; i < attempts; i++) {
                pos.set(
                        origin.getX() + random.nextIntBetweenInclusive(-horizontalRadius, horizontalRadius),
                        origin.getY() + random.nextIntBetweenInclusive(-verticalRadius, verticalRadius),
                        origin.getZ() + random.nextIntBetweenInclusive(-horizontalRadius, horizontalRadius)
                );

                if (tryPlaceOre(level, bulkSectionAccess, pos, ore, replaceable, random, config)) {
                    placed++;
                }
            }
        }

        return placed > 0;
    }

    private boolean placeDeep(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        if (origin.getY() > (config.minY() + config.maxY()) / 2) {
            return false;
        }

        return placeCluster(level, origin.below(random.nextInt(8)), random, ore, replaceable, config);
    }

    private boolean placeCaveWall(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        int placed = 0;
        final int attempts = scaledInt(config.size(), 1);
        final int horizontalRadius = scaledInt(Math.max(1, config.horizontalRadius()), 1);
        final int verticalRadius = scaledInt(Math.max(1, config.verticalRadius()), 1);

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(level)) {
            for (int i = 0; i < attempts; i++) {
                cursor.set(
                        origin.getX() + random.nextIntBetweenInclusive(-horizontalRadius, horizontalRadius),
                        origin.getY() + random.nextIntBetweenInclusive(-verticalRadius, verticalRadius),
                        origin.getZ() + random.nextIntBetweenInclusive(-horizontalRadius, horizontalRadius)
                );

                for (Direction direction : Direction.values()) {
                    final BlockPos attached = cursor.relative(direction);
                    if (tryPlaceOreOnWall(level, bulkSectionAccess, attached, cursor, ore, replaceable, random, config)) {
                        placed++;
                    }
                }
            }
        }

        return placed > 0;
    }

    private boolean placeGeological(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        if (config.requireNearLava() && !SpaceWorldgenFluidHelper.isAdjacentToLava(level, origin)) {
            return false;
        }
        if (config.requireNearFluid() && !SpaceWorldgenFluidHelper.isAdjacentToFluid(level, origin)) {
            return false;
        }

        return placeCluster(level, origin, random, ore, replaceable, config);
    }

    private boolean placeFracture(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        int placed = 0;
        int yDrift = 0;
        int x = origin.getX();
        int z = origin.getZ();

        final int steps = scaledInt(config.pathLength(), 1);
        final int verticalRadius = scaledInt(Math.max(1, config.verticalRadius()), 1);

        try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(level)) {
            for (int step = 0; step < steps; step++) {
                x += random.nextIntBetweenInclusive(-1, 1);
                z += random.nextIntBetweenInclusive(-1, 1);
                yDrift += random.nextIntBetweenInclusive(-1, 1);

                final BlockPos center = new BlockPos(x, origin.getY() + yDrift, z);

                for (int depth = -verticalRadius; depth <= verticalRadius; depth++) {
                    placed += placeBlob(
                            level,
                            bulkSectionAccess,
                            center.offset(0, depth, 0),
                            scaledRadius(1.4F + random.nextFloat(), 1.0F),
                            ore,
                            replaceable,
                            random,
                            config
                    );
                }
            }
        }

        return placed > 0;
    }

    private boolean placeCore(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        final float factor =
                1.0F - (origin.getY() - config.minY()) / (float) Math.max(1, config.maxY() - config.minY());

        if (random.nextFloat() > factor) {
            return false;
        }

        return placeCluster(level, origin, random, ore, replaceable, config);
    }

    private boolean placeGradient(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final BlockState ore,
            final Set<Block> replaceable,
            final SpaceOreFeatureConfig config
    ) {
        final float chance = scaledChance(origin.getY(), config);

        if (random.nextFloat() > chance) {
            return false;
        }

        return placeVein(level, origin, random, ore, replaceable, config);
    }

    private int placeBlob(
            final WorldGenLevel level,
            final BulkSectionAccess bulkSectionAccess,
            final BlockPos center,
            final float radius,
            final BlockState ore,
            final Set<Block> replaceable,
            final RandomSource random,
            final SpaceOreFeatureConfig config
    ) {
        final int r = Mth.ceil(radius);
        int placed = 0;
        final double radiusSq = radius * radius;

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    final double distSq = x * x + y * y + z * z;
                    if (distSq > radiusSq) {
                        continue;
                    }

                    final double normalized = distSq / radiusSq;
                    if (random.nextFloat() < 0.15F * normalized) {
                        continue;
                    }

                    pos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    if (tryPlaceOre(level, bulkSectionAccess, pos, ore, replaceable, random, config)) {
                        placed++;
                    }
                }
            }
        }

        return placed;
    }

    private boolean tryPlaceOre(
            final WorldGenLevel level,
            final BulkSectionAccess bulkSectionAccess,
            final BlockPos.MutableBlockPos pos,
            final BlockState ore,
            final Set<Block> replaceable,
            final RandomSource random,
            final SpaceOreFeatureConfig config
    ) {
        if (pos.getY() < config.minY() || pos.getY() > config.maxY() + 30) {
            return false;
        }

        if (!level.ensureCanWrite(pos) || level.isOutsideBuildHeight(pos.getY())) {
            return false;
        }

        final LevelChunkSection section = bulkSectionAccess.getSection(pos);
        if (section == null) {
            return false;
        }

        final int localX = SectionPos.sectionRelative(pos.getX());
        final int localY = SectionPos.sectionRelative(pos.getY());
        final int localZ = SectionPos.sectionRelative(pos.getZ());

        final BlockState current = section.getBlockState(localX, localY, localZ);
        if (!replaceable.contains(current.getBlock())) {
            return false;
        }

        if (config.requireNearAir() && !SpaceWorldgenFluidHelper.isAdjacentToAir(level, pos)) {
            return false;
        }
        if (config.requireNearFluid() && !SpaceWorldgenFluidHelper.isAdjacentToFluid(level, pos)) {
            return false;
        }

        if (config.caveOnly() && !SpaceWorldgenFluidHelper.isAdjacentToAir(level, pos)) {
            return false;
        }

        final float chance = scaledChance(pos.getY(), config);

        if (random.nextFloat() > chance) {
            return false;
        }

        section.setBlockState(localX, localY, localZ, ore, false);
        return true;
    }

    private boolean tryPlaceOreOnWall(
            final WorldGenLevel level,
            final BulkSectionAccess bulkSectionAccess,
            final BlockPos orePos,
            final BlockPos airPos,
            final BlockState ore,
            final Set<Block> replaceable,
            final RandomSource random,
            final SpaceOreFeatureConfig config
    ) {
        if (!level.getBlockState(airPos).isAir()) {
            return false;
        }

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(orePos);
        return tryPlaceOre(level, bulkSectionAccess, mutable, ore, replaceable, random, config);
    }

    private static final class AdjacentStateLevelAccessor {
        private final Function<BlockPos, BlockState> accessor;

        private AdjacentStateLevelAccessor(final Function<BlockPos, BlockState> accessor) {
            this.accessor = accessor;
        }

        public boolean isAdjacentToAir(final BlockPos pos) {
            for (Direction direction : Direction.values()) {
                if (this.accessor.apply(pos.relative(direction)).isAir()) {
                    return true;
                }
            }
            return false;
        }

        public boolean isAdjacentToFluid(final BlockPos pos) {
            for (Direction direction : Direction.values()) {
                if (!this.accessor.apply(pos.relative(direction)).getFluidState().isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        public boolean isAdjacentToLava(final BlockPos pos) {
            for (Direction direction : Direction.values()) {
                if (this.accessor.apply(pos.relative(direction)).is(Blocks.LAVA)) {
                    return true;
                }
            }
            return false;
        }
    }
}