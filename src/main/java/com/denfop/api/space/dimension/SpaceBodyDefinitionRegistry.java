package com.denfop.api.space.dimension;

import com.denfop.IUItem;
import com.denfop.api.space.*;
import com.denfop.blocks.FluidName;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;

public final class SpaceBodyDefinitionRegistry {

    public static final String STONE = "stone";
    public static final String TOP = "top";
    public static final String SUBSURFACE = "subsurface";
    public static final String RIM = "rim";
    public static final String COBBLE = "cobble";
    public static final String FLUID = "fluid";

    private static final Map<String, SpaceBodyDefinition> REGISTRY = new HashMap<>();
    private static final Set<String> RESOURCES_FILLED = new HashSet<>();

    private SpaceBodyDefinitionRegistry() {
    }

    public static void clear() {
        REGISTRY.clear();
        RESOURCES_FILLED.clear();
    }

    public static void register(final String bodyName, final SpaceBodyDefinition definition) {
        REGISTRY.put(normalize(bodyName), definition);
    }

    public static SpaceBodyDefinition get(final String bodyName) {
        return REGISTRY.get(normalize(bodyName));
    }

    public static SpaceBodyDefinition getOrCreate(final String bodyName) {
        return REGISTRY.computeIfAbsent(normalize(bodyName), k -> SpaceBodyDefinition.create());
    }

    public static SpaceBodyDefinition getOrBuild(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        if (REGISTRY.isEmpty()) {
            bootstrapDefaults();
        }

        final String name = normalize(body.name());
        final SpaceBodyDefinition definition = getOrCreate(name);

        ensureBaseLayers(definition, mode);

        if (!RESOURCES_FILLED.contains(name)) {
            fillResourcesFromBody(definition, body, mode);
            RESOURCES_FILLED.add(name);
        }

        return definition;
    }

    private static String normalize(final String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static void bootstrapDefaults() {
        clear();


        for (SpaceBodyRef body : SpaceBodyCatalog.allBodies()) {
            register(body.name(), SpaceBodyDefinition.create());
        }

        registerSolidOverride("mercury",
                IUItem.space_stone.getItemStack(15),
                IUItem.space_cobblestone.getItemStack(15),
                Blocks.BASALT.defaultBlockState(),
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        );

        registerSolidOverride("moon",
                IUItem.space_stone1.getItemStack(2),
                IUItem.space_cobblestone1.getItemStack(2),
                Blocks.END_STONE.defaultBlockState(),
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        );

        registerSolidOverride("venus",
                IUItem.space_stone1.getItemStack(13),
                IUItem.space_cobblestone1.getItemStack(13),
                Blocks.BLACKSTONE.defaultBlockState(),
                Blocks.BASALT.defaultBlockState()
        );

        registerSolidOverride("earth",
                ItemStack.EMPTY,
                ItemStack.EMPTY,
                Blocks.STONE.defaultBlockState(),
                Blocks.COBBLESTONE.defaultBlockState()
        );

        registerSolidOverride("mars",
                IUItem.space_stone.getItemStack(14),
                IUItem.space_cobblestone.getItemStack(14),
                Blocks.RED_SANDSTONE.defaultBlockState(),
                Blocks.TERRACOTTA.defaultBlockState()
        );

        registerSolidOverride("ceres",
                IUItem.space_stone.getItemStack(3),
                IUItem.space_cobblestone.getItemStack(3),
                Blocks.TUFF.defaultBlockState(),
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        );

        registerSolidOverride("asteroid",
                IUItem.space_stone.getItemStack(1),
                IUItem.space_cobblestone.getItemStack(1),
                Blocks.END_STONE.defaultBlockState(),
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        );

        registerSolidOverride("phobos",
                IUItem.space_stone1.getItemStack(4),
                IUItem.space_cobblestone1.getItemStack(4),
                Blocks.END_STONE.defaultBlockState(),
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        );

        registerSolidOverride("deimos",
                IUItem.space_stone.getItemStack(5),
                IUItem.space_cobblestone.getItemStack(5),
                Blocks.END_STONE.defaultBlockState(),
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        );

        registerSolidOverride("io",
                IUItem.space_stone.getItemStack(12),
                IUItem.space_cobblestone.getItemStack(12),
                Blocks.BLACKSTONE.defaultBlockState(),
                Blocks.BASALT.defaultBlockState()
        );

        registerSolidOverride("callisto",
                IUItem.space_stone.getItemStack(2),
                IUItem.space_cobblestone.getItemStack(2),
                Blocks.END_STONE.defaultBlockState(),
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        );

        registerSolidOverride("ganymede",
                IUItem.space_stone.getItemStack(10),
                IUItem.space_cobblestone.getItemStack(10),
                Blocks.END_STONE.defaultBlockState(),
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        );

        registerSolidOverride("europe",
                IUItem.space_stone.getItemStack(9),
                IUItem.space_cobblestone.getItemStack(9),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("enceladus",
                IUItem.space_stone.getItemStack(7),
                IUItem.space_cobblestone.getItemStack(7),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("titan",
                IUItem.space_stone1.getItemStack(9),
                IUItem.space_cobblestone1.getItemStack(9),
                Blocks.ORANGE_TERRACOTTA.defaultBlockState(),
                Blocks.BROWN_CONCRETE_POWDER.defaultBlockState()
        );

        registerSolidOverride("dione",
                IUItem.space_stone.getItemStack(6),
                IUItem.space_cobblestone.getItemStack(6),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("mimas",
                IUItem.space_stone1.getItemStack(0),
                IUItem.space_cobblestone1.getItemStack(0),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("rhea",
                IUItem.space_stone1.getItemStack(7),
                IUItem.space_cobblestone1.getItemStack(7),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("tethys",
                IUItem.space_stone1.getItemStack(8),
                IUItem.space_cobblestone1.getItemStack(8),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("jupiter",
                ItemStack.EMPTY,
                ItemStack.EMPTY,
                Blocks.YELLOW_TERRACOTTA.defaultBlockState(),
                Blocks.ORANGE_TERRACOTTA.defaultBlockState()
        );

        registerSolidOverride("saturn",
                ItemStack.EMPTY,
                ItemStack.EMPTY,
                Blocks.SANDSTONE.defaultBlockState(),
                Blocks.SMOOTH_SANDSTONE.defaultBlockState()
        );

        registerSolidOverride("uranus",
                ItemStack.EMPTY,
                ItemStack.EMPTY,
                Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState(),
                Blocks.CYAN_TERRACOTTA.defaultBlockState()
        );

        registerSolidOverride("neptune",
                ItemStack.EMPTY,
                ItemStack.EMPTY,
                Blocks.BLUE_TERRACOTTA.defaultBlockState(),
                Blocks.CYAN_TERRACOTTA.defaultBlockState()
        );

        registerSolidOverride("titania",
                IUItem.space_stone1.getItemStack(10),
                IUItem.space_cobblestone1.getItemStack(10),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("umbriel",
                IUItem.space_stone1.getItemStack(12),
                IUItem.space_cobblestone1.getItemStack(12),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("oberon",
                IUItem.space_stone1.getItemStack(3),
                IUItem.space_cobblestone1.getItemStack(3),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("ariel",
                IUItem.space_stone.getItemStack(0),
                IUItem.space_cobblestone.getItemStack(0),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("miranda",
                IUItem.space_stone1.getItemStack(1),
                IUItem.space_cobblestone1.getItemStack(1),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("triton",
                IUItem.space_stone1.getItemStack(11),
                IUItem.space_cobblestone1.getItemStack(11),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("proteus",
                IUItem.space_stone1.getItemStack(6),
                IUItem.space_cobblestone1.getItemStack(6),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("pluto",
                IUItem.space_stone1.getItemStack(5),
                IUItem.space_cobblestone1.getItemStack(5),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("charon",
                IUItem.space_stone.getItemStack(4),
                IUItem.space_cobblestone.getItemStack(4),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("eris",
                IUItem.space_stone.getItemStack(8),
                IUItem.space_cobblestone.getItemStack(8),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("makemake",
                IUItem.space_stone.getItemStack(13),
                IUItem.space_cobblestone.getItemStack(13),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );

        registerSolidOverride("haumea",
                IUItem.space_stone.getItemStack(11),
                IUItem.space_cobblestone.getItemStack(11),
                Blocks.PACKED_ICE.defaultBlockState(),
                Blocks.BLUE_ICE.defaultBlockState()
        );
    }

    private static void ensureBaseLayers(final SpaceBodyDefinition definition, final SpaceGenerationMode mode) {
        if (definition.getLayer(STONE) != null
                && definition.getLayer(TOP) != null
                && definition.getLayer(SUBSURFACE) != null
                && definition.getLayer(RIM) != null
                && definition.getLayer(COBBLE) != null
                && definition.getLayer(FLUID) != null) {
            return;
        }

        final SpaceBodyDefinition defaults = buildDefaultDefinition(mode);

        if (definition.getLayer(STONE) == null) {
            definition.layer(STONE, defaults.getLayer(STONE));
        }
        if (definition.getLayer(TOP) == null) {
            definition.layer(TOP, defaults.getLayer(TOP));
        }
        if (definition.getLayer(SUBSURFACE) == null) {
            definition.layer(SUBSURFACE, defaults.getLayer(SUBSURFACE));
        }
        if (definition.getLayer(RIM) == null) {
            definition.layer(RIM, defaults.getLayer(RIM));
        }
        if (definition.getLayer(COBBLE) == null) {
            definition.layer(COBBLE, defaults.getLayer(COBBLE));
        }
        if (definition.getLayer(FLUID) == null) {
            definition.layer(FLUID, defaults.getLayer(FLUID));
        }

        if (definition.getFluids().isEmpty() && !defaults.getFluids().isEmpty()) {
            for (SpaceFluidPocket fluidPocket : defaults.getFluids()) {
                definition.fluid(fluidPocket);
            }
        }
    }

    private static SpaceBodyDefinition buildDefaultDefinition(final SpaceGenerationMode mode) {
        return switch (mode) {
            case NONE -> SpaceBodyDefinition.create()
                    .layer(STONE, Blocks.AIR.defaultBlockState())
                    .layer(TOP, Blocks.AIR.defaultBlockState())
                    .layer(SUBSURFACE, Blocks.AIR.defaultBlockState())
                    .layer(RIM, Blocks.AIR.defaultBlockState())
                    .layer(COBBLE, Blocks.AIR.defaultBlockState())
                    .layer(FLUID, Blocks.AIR.defaultBlockState());

            case VOLCANIC -> SpaceBodyDefinition.create()
                    .layer(STONE, Blocks.BLACKSTONE.defaultBlockState())
                    .layer(TOP, Blocks.BLACKSTONE.defaultBlockState())
                    .layer(SUBSURFACE, Blocks.BLACKSTONE.defaultBlockState())
                    .layer(RIM, Blocks.MAGMA_BLOCK.defaultBlockState())
                    .layer(COBBLE, Blocks.BASALT.defaultBlockState())
                    .layer(FLUID, flowingLava())
                    .fluid(new SpaceFluidPocket(
                            flowingLava(),
                            100,
                            -36,
                            16,
                            SpaceVentType.LAVA,
                            true, FluidName.fluidpahoehoe_lava.getInstance().get()
                    ));

            case DUSTY_DESERT -> SpaceBodyDefinition.create()
                    .layer(STONE, Blocks.RED_SANDSTONE.defaultBlockState())
                    .layer(TOP, Blocks.RED_SANDSTONE.defaultBlockState())
                    .layer(SUBSURFACE, Blocks.TERRACOTTA.defaultBlockState())
                    .layer(RIM, Blocks.RED_TERRACOTTA.defaultBlockState())
                    .layer(COBBLE, Blocks.RED_SAND.defaultBlockState())
                    .layer(FLUID, Blocks.AIR.defaultBlockState());

            case ICY_SHELL -> SpaceBodyDefinition.create()
                    .layer(STONE, Blocks.PACKED_ICE.defaultBlockState())
                    .layer(TOP, Blocks.PACKED_ICE.defaultBlockState())
                    .layer(SUBSURFACE, Blocks.BLUE_ICE.defaultBlockState())
                    .layer(RIM, Blocks.CALCITE.defaultBlockState())
                    .layer(COBBLE, Blocks.SNOW_BLOCK.defaultBlockState())
                    .layer(FLUID, Blocks.AIR.defaultBlockState());

            case CRYO_CHEMICAL -> SpaceBodyDefinition.create()
                    .layer(STONE, Blocks.PACKED_ICE.defaultBlockState())
                    .layer(TOP, Blocks.PACKED_ICE.defaultBlockState())
                    .layer(SUBSURFACE, Blocks.CALCITE.defaultBlockState())
                    .layer(RIM, Blocks.BLUE_ICE.defaultBlockState())
                    .layer(COBBLE, Blocks.SNOW_BLOCK.defaultBlockState())
                    .layer(FLUID, Blocks.AIR.defaultBlockState());

            case ASTEROID_FIELD, AIRLESS_BARREN, AIRLESS_CRATERED -> SpaceBodyDefinition.create()
                    .layer(STONE, Blocks.END_STONE.defaultBlockState())
                    .layer(TOP, Blocks.END_STONE.defaultBlockState())
                    .layer(SUBSURFACE, Blocks.TUFF.defaultBlockState())
                    .layer(RIM, Blocks.DEEPSLATE.defaultBlockState())
                    .layer(COBBLE, Blocks.COBBLED_DEEPSLATE.defaultBlockState())
                    .layer(FLUID, Blocks.AIR.defaultBlockState());
        };
    }

    private static void registerSolidOverride(
            final String bodyName,
            final ItemStack stoneStack,
            final ItemStack cobbleStack,
            final BlockState stoneFallback,
            final BlockState cobbleFallback
    ) {
        final SpaceBodyDefinition definition = getOrCreate(bodyName);

        final BlockState stone = block(stoneStack, stoneFallback);
        final BlockState cobble = block(cobbleStack, cobbleFallback);

        definition
                .layer(STONE, stone)
                .layer(TOP, stone)
                .layer(SUBSURFACE, stone)
                .layer(RIM, cobble)
                .layer(COBBLE, cobble);
    }

    private static void registerSolidOverride(
            final String bodyName,
            final BlockItem stoneStack,
            final BlockItem cobbleStack,
            final BlockState stoneFallback,
            final BlockState cobbleFallback
    ) {
        final SpaceBodyDefinition definition = getOrCreate(bodyName);

        final BlockState stone = block(stoneStack, stoneFallback);
        final BlockState cobble = block(cobbleStack, cobbleFallback);

        definition
                .layer(STONE, stone)
                .layer(TOP, stone)
                .layer(SUBSURFACE, stone)
                .layer(RIM, cobble)
                .layer(COBBLE, cobble);
    }

    private static BlockState flowingLava() {
        return FluidName.fluidpahoehoe_lava.getInstance().get().getSource().defaultFluidState().createLegacyBlock();
    }

    private static void fillResourcesFromBody(
            final SpaceBodyDefinition definition,
            final SpaceBodyRef body,
            final SpaceGenerationMode mode
    ) {
        final List<IBaseResource> resources = getResources(body);
        if (resources.isEmpty()) {
            return;
        }

        final Map<Block, SpaceOreEntry> uniqueOres = new LinkedHashMap<>();
        final Map<Fluid, SpaceFluidPocket> uniqueFluids = new LinkedHashMap<>();

        for (IBaseResource resource : resources) {
            final int weight = normalizeWeight(resource);

            final ItemStack itemStack = resource.getItemStack();
            if (itemStack != null && !itemStack.isEmpty() && isOreStack(itemStack)) {
                final BlockState oreState = block(itemStack, null);
                if (oreState != null && !oreState.isAir()) {
                    final Block oreBlock = oreState.getBlock();

                    final SpaceOreEntry candidate = new SpaceOreEntry(
                            oreState,
                            weight,
                            oreVeinSize(mode, weight),
                            oreMinY(mode),
                            oreMaxY(mode)
                    );

                    final SpaceOreEntry current = uniqueOres.get(oreBlock);
                    if (current == null || candidate.weight() > current.weight()) {
                        uniqueOres.put(oreBlock, candidate);
                    }
                }
            }

            final FluidStack fluidStack = resource.getFluidStack();
            if (fluidStack != null && !fluidStack.isEmpty()) {
                final Fluid normalizedFluid = normalizeFluid(fluidStack.getFluid());
                final BlockState fluidState = flowing(fluidStack, null);

                if (normalizedFluid != null && fluidState != null && !fluidState.isAir()) {
                    final SpaceFluidPocket candidate = new SpaceFluidPocket(
                            fluidState,
                            weight,
                            fluidMinY(mode),
                            fluidMaxY(mode),
                            classifyVent(normalizedFluid, mode),
                            mode == SpaceGenerationMode.VOLCANIC
                                    || mode == SpaceGenerationMode.ICY_SHELL
                                    || mode == SpaceGenerationMode.CRYO_CHEMICAL, fluidStack.getFluid()
                    );

                    final SpaceFluidPocket current = uniqueFluids.get(normalizedFluid);
                    if (current == null || candidate.weight() > current.weight()) {
                        uniqueFluids.put(normalizedFluid, candidate);
                    }
                }
            }
        }

        uniqueOres.values().forEach(definition::ore);
        uniqueFluids.values().forEach(definition::fluid);
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

    private static boolean isOreStack(final ItemStack stack) {
        return sameFamily(stack, IUItem.space_ore.getItem(stack))
                || sameFamily(stack, IUItem.space_ore1.getItem(stack))
                || sameFamily(stack, IUItem.space_ore2.getItem(stack))
                || sameFamily(stack, IUItem.space_ore3.getItem(stack));
    }

    private static boolean sameFamily(final ItemStack a, final ItemStack b) {
        return a != null
                && b != null
                && !a.isEmpty()
                && !b.isEmpty()
                && a.getItem() == b.getItem();
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

    private static BlockState block(final ItemStack stack, final BlockState fallback) {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
            return fallback;
        }
        final BlockState state = blockItem.getBlock().defaultBlockState();
        return state.isAir() ? fallback : state;
    }

    private static BlockState block(final BlockItem blockItem, final BlockState fallback) {
        final BlockState state = blockItem.getBlock().defaultBlockState();
        return state.isAir() ? fallback : state;
    }

    private static BlockState flowing(final FluidStack stack, final BlockState fallback) {
        if (stack == null || stack.isEmpty()) {
            return fallback;
        }

        final Fluid fluid = stack.getFluid();

        FlowingFluid flowingFluid = (FlowingFluid) fluid;

        final BlockState state = flowingFluid.getSource().defaultFluidState().createLegacyBlock();
        return state.isAir() ? fallback : state;
    }

    private static Fluid normalizeFluid(final Fluid fluid) {
        if (fluid == null) {
            return null;
        }
        if (fluid instanceof FlowingFluid flowingFluid) {
            return flowingFluid.getSource();
        }
        return fluid;
    }

    private static int oreVeinSize(final SpaceGenerationMode mode, final int weight) {
        final int base = switch (mode) {
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

    private static SpaceVentType classifyVent(final Fluid fluid, final SpaceGenerationMode mode) {
        final Fluid source = normalizeFluid(fluid);

        if (source == null) {
            return mode == SpaceGenerationMode.ICY_SHELL || mode == SpaceGenerationMode.CRYO_CHEMICAL
                    ? SpaceVentType.CRYO
                    : SpaceVentType.STEAM;
        }

        if (source == normalizeFluid(net.minecraft.world.level.material.Fluids.LAVA)
                || mode == SpaceGenerationMode.VOLCANIC) {
            return SpaceVentType.LAVA;
        }

        if (mode == SpaceGenerationMode.ICY_SHELL || mode == SpaceGenerationMode.CRYO_CHEMICAL) {
            return SpaceVentType.CRYO;
        }

        if (mode == SpaceGenerationMode.DUSTY_DESERT) {
            return SpaceVentType.GAS;
        }

        return SpaceVentType.STEAM;
    }
}