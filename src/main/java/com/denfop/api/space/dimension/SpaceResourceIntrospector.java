package com.denfop.api.space.dimension;

import com.denfop.IUCore;
import com.denfop.api.space.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;

public final class SpaceResourceIntrospector {

    private SpaceResourceIntrospector() {
    }

    public static SpaceTerrainPalette resolveTerrain(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        return SpaceBlockResolver.resolvePalette(body, mode);
    }

    private static List<IBaseResource> getResources(final SpaceBodyRef body) {
        final IBody resolved = SpaceNet.instance.getBodyFromName(body.name());
        if (resolved == null) {
            return List.of();
        }

        if (body.isSatellite() && resolved instanceof ISatellite satellite) {
            return satellite.getResources();
        }

        if (resolved instanceof IPlanet planet) {
            return planet.getResources();
        }

        return List.of();
    }

    private static int normalizeWeight(final IBaseResource resource) {
        final int chance = Math.max(1, resource.getChance());
        final int maxChance = Math.max(chance, resource.getMaxChance());
        final int research = Math.max(0, resource.getPercentResearchBody());
        final int panel = Math.max(0, resource.getPercentPanel());

        int normalized = Math.max(1, Math.round((chance * 100.0f) / maxChance));
        normalized = Math.max(normalized, research / 5);
        normalized = Math.max(normalized, panel / 5);
        return normalized;
    }

    private static void classifyItem(
            final ItemStack stack,
            final int weight,
            final List<CandidateBlock> stoneCandidates,
            final List<CandidateBlock> cobbleCandidates,
            final List<CandidateBlock> oreCandidates
    ) {
        final Item item = stack.getItem();
        final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        if (itemId == null) {
            return;
        }

        final String path = itemId.getPath().toLowerCase(Locale.ROOT);

        final SolidKind kind = classifyPath(path);
        if (kind == null) {
            return;
        }

        final BlockState resolved = resolveBlockState(item, itemId, kind);
        if (resolved == null || resolved.isAir()) {
            return;
        }

        switch (kind) {
            case STONE -> stoneCandidates.add(new CandidateBlock(resolved, weight));
            case COBBLE -> cobbleCandidates.add(new CandidateBlock(resolved, weight));
            case ORE -> oreCandidates.add(new CandidateBlock(resolved, weight));
        }
    }

    private static SolidKind classifyPath(final String path) {
        if (containsAny(path, "ore", "vein", "cluster")) {
            return SolidKind.ORE;
        }
        if (containsAny(path, "stone", "rock", "regolith", "crust", "mantle", "basalt")) {
            return SolidKind.STONE;
        }
        if (containsAny(path, "cobble", "rubble", "gravel", "dust", "sand", "regolith")) {
            return SolidKind.COBBLE;
        }
        return null;
    }

    private static BlockState resolveBlockState(final Item item, final ResourceLocation itemId, final SolidKind kind) {
        if (item instanceof BlockItem blockItem) {
            return blockItem.getBlock().defaultBlockState();
        }

        final LinkedHashSet<ResourceLocation> candidates = new LinkedHashSet<>();
        final String namespace = itemId.getNamespace();
        final String path = itemId.getPath();

        candidates.add(ResourceLocation.tryBuild(namespace, path));

        switch (kind) {
            case STONE -> {
                candidates.add(ResourceLocation.tryBuild(namespace, path + "_stone"));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("_dust", "_stone")));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("_fragment", "_stone")));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("_chunk", "_stone")));
                candidates.add(ResourceLocation.tryBuild(IUCore.MODID, path));
                candidates.add(ResourceLocation.tryBuild(IUCore.MODID, path + "_stone"));
            }
            case COBBLE -> {
                candidates.add(ResourceLocation.tryBuild(namespace, path + "_cobblestone"));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("_stone", "_cobblestone")));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("_stone", "_regolith")));
                candidates.add(ResourceLocation.tryBuild(IUCore.MODID, path));
                candidates.add(ResourceLocation.tryBuild(IUCore.MODID, path + "_cobblestone"));
            }
            case ORE -> {
                candidates.add(ResourceLocation.tryBuild(namespace, path + "_ore"));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("raw_", "") + "_ore"));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("_ingot", "_ore")));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("_dust", "_ore")));
                candidates.add(ResourceLocation.tryBuild(namespace, path.replace("_gem", "_ore")));
                candidates.add(ResourceLocation.tryBuild(IUCore.MODID, path));
                candidates.add(ResourceLocation.tryBuild(IUCore.MODID, path + "_ore"));
            }
        }

        for (ResourceLocation id : candidates) {
            Optional<Block> block = BuiltInRegistries.BLOCK.getOptional(id);
            if (block.isPresent() && block.get() != Blocks.AIR) {
                return block.get().defaultBlockState();
            }
        }

        return null;
    }

    private static void classifyFluid(final FluidStack stack, final int weight, final List<CandidateFluid> fluids) {
        final Fluid fluid = stack.getFluid();
        final BlockState flowing = toFlowingBlockState(fluid);
        if (flowing == null || flowing.isAir()) {
            return;
        }
        fluids.add(new CandidateFluid(flowing, weight, stack.getFluid()));
    }

    private static BlockState toFlowingBlockState(final Fluid fluid) {
        if (fluid instanceof FlowingFluid flowingFluid) {
            BlockState flowingState = flowingFluid.getFlowing().defaultFluidState().createLegacyBlock();
            if (!flowingState.isAir()) {
                return flowingState;
            }
        }

        BlockState fallback = fluid.defaultFluidState().createLegacyBlock();
        return fallback.isAir() ? null : fallback;
    }

    private static BlockState pickBlock(final List<CandidateBlock> candidates) {
        return candidates.stream()
                .max(Comparator.comparingInt(CandidateBlock::weight))
                .map(CandidateBlock::state)
                .orElse(Blocks.AIR.defaultBlockState());
    }

    private static Optional<BlockState> pickFluid(final List<CandidateFluid> candidates) {
        return candidates.stream()
                .max(Comparator.comparingInt(CandidateFluid::weight))
                .map(CandidateFluid::state);
    }

    private static List<SpaceOreEntry> buildOreEntries(final List<CandidateBlock> oreCandidates, final SpaceGenerationMode mode) {
        return oreCandidates.stream()
                .sorted(Comparator.comparingInt(CandidateBlock::weight).reversed())
                .limit(8)
                .map(candidate -> new SpaceOreEntry(
                        candidate.state(),
                        candidate.weight(),
                        oreVeinSize(mode, candidate.weight()),
                        oreMinY(mode),
                        oreMaxY(mode)
                ))
                .toList();
    }

    private static List<SpaceFluidPocket> buildFluidEntries(final List<CandidateFluid> fluidCandidates, final SpaceGenerationMode mode) {
        return fluidCandidates.stream()
                .sorted(Comparator.comparingInt(CandidateFluid::weight).reversed())
                .limit(3)
                .map(candidate -> new SpaceFluidPocket(
                        candidate.state(),
                        candidate.weight(),
                        fluidMinY(mode),
                        fluidMaxY(mode),
                        classifyVent(candidate.state(), mode),
                        mode == SpaceGenerationMode.CRYO_CHEMICAL
                                || mode == SpaceGenerationMode.ICY_SHELL
                                || mode == SpaceGenerationMode.VOLCANIC, candidate.fluid()
                ))
                .toList();
    }

    private static boolean containsAny(final String path, final String... parts) {
        for (String part : parts) {
            if (path.contains(part)) {
                return true;
            }
        }
        return false;
    }

    private static int oreVeinSize(final SpaceGenerationMode mode, final int weight) {
        int base = switch (mode) {
            case VOLCANIC -> 9;
            case DUSTY_DESERT -> 7;
            case ICY_SHELL, CRYO_CHEMICAL -> 6;
            case ASTEROID_FIELD -> 10;
            default -> 7;
        };
        return Math.max(3, Math.min(14, base + weight / 30));
    }

    private static int oreMinY(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> -48;
            case DUSTY_DESERT -> -32;
            case ICY_SHELL, CRYO_CHEMICAL -> -56;
            default -> -40;
        };
    }

    private static int oreMaxY(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> 80;
            case DUSTY_DESERT -> 64;
            case ICY_SHELL, CRYO_CHEMICAL -> 48;
            default -> 72;
        };
    }

    private static int fluidMinY(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> -36;
            case CRYO_CHEMICAL, ICY_SHELL -> -56;
            default -> -28;
        };
    }

    private static int fluidMaxY(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> 16;
            case CRYO_CHEMICAL, ICY_SHELL -> 28;
            default -> 20;
        };
    }

    private static SpaceVentType classifyVent(final BlockState fluidState, final SpaceGenerationMode mode) {
        final String path = fluidState.getBlock()
                .builtInRegistryHolder()
                .key()
                .location()
                .getPath()
                .toLowerCase(Locale.ROOT);

        if (fluidState.is(Blocks.LAVA) || mode == SpaceGenerationMode.VOLCANIC) {
            return SpaceVentType.LAVA;
        }
        if (containsAny(path, "acid", "sulfur", "chlor", "brom", "iod")) {
            return SpaceVentType.ACID;
        }
        if (containsAny(path, "methane", "nitrogen", "hydrogen", "xenon", "helium")) {
            return (mode == SpaceGenerationMode.CRYO_CHEMICAL || mode == SpaceGenerationMode.ICY_SHELL)
                    ? SpaceVentType.CRYO
                    : SpaceVentType.GAS;
        }
        return mode == SpaceGenerationMode.ICY_SHELL ? SpaceVentType.CRYO : SpaceVentType.STEAM;
    }

    private enum SolidKind {
        STONE,
        COBBLE,
        ORE
    }

    private record CandidateBlock(BlockState state, int weight) {
    }

    private record CandidateFluid(BlockState state, int weight, Fluid fluid) {
    }
}