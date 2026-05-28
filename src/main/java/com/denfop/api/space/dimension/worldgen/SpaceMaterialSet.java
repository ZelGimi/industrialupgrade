package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.denfop.api.space.dimension.SpaceFluidPocket;
import com.denfop.api.space.dimension.SpaceOreEntry;
import com.denfop.blocks.FluidName;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public record SpaceMaterialSet(
        BlockState stone,
        BlockState bridge,
        BlockState accent,
        BlockState crystal,
        BlockState ore,
        BlockState liquid,
        BlockState cavityFill
) {


    public static SpaceMaterialSet resolve(
            final WorldGenLevel level,
            final BlockPos origin,
            final SpacePlanetTraits traits,
            final SpaceDimensionProfile profile
    ) {
        final BlockState stone = pickSolid(profile.defaultBlock(), Blocks.STONE.defaultBlockState());
        final BlockState bridge = pickSolid(profile.cobbleBlock(), stone);
        final BlockState accent = pickSolid(profile.rimBlock(), pickSolid(profile.topBlock(), stone));
        final BlockState ore = firstOre(profile, accent);
        final BlockState crystal = firstCrystal(profile, ore, accent);
        final BlockState liquid = firstFluid(profile, fallbackLiquid(traits));
        final BlockState cavityFill = traits.thinAtmosphere() ? Blocks.AIR.defaultBlockState() : liquid;

        return new SpaceMaterialSet(
                stone,
                bridge,
                stone,
                crystal,
                ore,
                liquid,
                cavityFill
        );
    }


    public static SpaceMaterialSet resolve(
            final WorldGenLevel level,
            final BlockPos origin,
            final SpacePlanetTraits traits
    ) {
        final BlockState stone = dominantSolid(level, origin, traits);
        final BlockState bridge = stone;
        final BlockState accent = stone;
        final BlockState ore = resolveOreFallback(traits);
        final BlockState crystal = ore;
        final BlockState liquid = resolveLiquidFallback(traits);
        final BlockState cavityFill = traits.thinAtmosphere() ? Blocks.AIR.defaultBlockState() : liquid;

        return new SpaceMaterialSet(
                stone,
                bridge,
                stone,
                crystal,
                ore,
                liquid,
                cavityFill
        );
    }

    private static BlockState pickSolid(final BlockState candidate, final BlockState fallback) {
        if (candidate == null || candidate.isAir()) {
            return fallback;
        }
        return candidate;
    }

    private static BlockState firstOre(final SpaceDimensionProfile profile, final BlockState fallback) {
        if (profile == null || profile.ores() == null || profile.ores().isEmpty()) {
            return fallback;
        }

        for (final SpaceOreEntry entry : profile.ores()) {
            if (entry != null && entry.state() != null && !entry.state().isAir()) {
                return entry.state();
            }
        }

        return fallback;
    }

    private static BlockState firstCrystal(
            final SpaceDimensionProfile profile,
            final BlockState oreFallback,
            final BlockState accentFallback
    ) {
        if (profile != null && profile.ores() != null) {
            for (final SpaceOreEntry entry : profile.ores()) {
                if (entry != null && entry.state() != null && !entry.state().isAir()) {
                    return entry.state();
                }
            }
        }
        return !accentFallback.isAir() ? accentFallback : oreFallback;
    }

    private static BlockState firstFluid(final SpaceDimensionProfile profile, final BlockState fallback) {
        if (profile == null || profile.fluids() == null || profile.fluids().isEmpty()) {
            return fallback;
        }

        for (final SpaceFluidPocket entry : profile.fluids()) {
            if (entry != null && entry.state() != null && !entry.state().isAir()) {
                return entry.state();
            }
        }

        return fallback;
    }

    private static BlockState fallbackLiquid(final SpacePlanetTraits traits) {
        if (traits.volcanic()) {
            return FluidName.fluidpahoehoe_lava.getInstance().get().getSource().defaultFluidState().createLegacyBlock();
        }
        if (traits.cryogenic()) {
            return Blocks.POWDER_SNOW.defaultBlockState();
        }
        return Blocks.WATER.defaultBlockState();
    }

    private static BlockState dominantSolid(final WorldGenLevel level, final BlockPos origin, final SpacePlanetTraits traits) {
        final Map<Block, Integer> counts = new HashMap<>();
        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int dx = -6; dx <= 6; dx += 2) {
            for (int dy = -6; dy <= 6; dy += 2) {
                for (int dz = -6; dz <= 6; dz += 2) {
                    cursor.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    final BlockState state = level.getBlockState(cursor);

                    if (state.isAir() || !state.getFluidState().isEmpty()) {
                        continue;
                    }
                    if (state.is(Blocks.BEDROCK)) {
                        continue;
                    }

                    counts.merge(state.getBlock(), 1, Integer::sum);
                }
            }
        }

        Block best = null;
        int bestCount = 0;
        for (final Map.Entry<Block, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > bestCount) {
                best = entry.getKey();
                bestCount = entry.getValue();
            }
        }

        if (best != null) {
            return best.defaultBlockState();
        }

        if (traits.cryogenic()) {
            return Blocks.PACKED_ICE.defaultBlockState();
        }
        if (traits.volcanic()) {
            return Blocks.BASALT.defaultBlockState();
        }
        if (traits.anomaly() > 0.65D) {
            return Blocks.CALCITE.defaultBlockState();
        }

        return Blocks.STONE.defaultBlockState();
    }

    private static BlockState resolveOreFallback(final SpacePlanetTraits traits) {
        if (traits.volcanic()) {
            return Blocks.COPPER_ORE.defaultBlockState();
        }
        if (traits.cryogenic()) {
            return Blocks.REDSTONE_ORE.defaultBlockState();
        }
        if (traits.mineralRichness() > 0.8D) {
            return Blocks.DIAMOND_ORE.defaultBlockState();
        }
        if (traits.anomaly() > 0.6D) {
            return Blocks.EMERALD_ORE.defaultBlockState();
        }
        return Blocks.IRON_ORE.defaultBlockState();
    }

    private static BlockState resolveLiquidFallback(final SpacePlanetTraits traits) {
        if (traits.volcanic()) {
            return FluidName.fluidpahoehoe_lava.getInstance().get().getSource().defaultFluidState().createLegacyBlock();
        }
        if (traits.cryogenic()) {
            return Blocks.POWDER_SNOW.defaultBlockState();
        }
        return Blocks.WATER.defaultBlockState();
    }
}