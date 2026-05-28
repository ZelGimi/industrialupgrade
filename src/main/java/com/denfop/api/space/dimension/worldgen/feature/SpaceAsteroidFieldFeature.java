package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.denfop.api.space.dimension.worldgen.feature.asteroid.AsteroidMaterialType;
import com.denfop.api.space.dimension.worldgen.feature.asteroid.AsteroidOreRegistry;
import com.denfop.api.space.dimension.worldgen.feature.asteroid.AsteroidOreRegistry.AsteroidOreDefinition;
import com.denfop.api.space.dimension.worldgen.feature.asteroid.AsteroidOreRegistry.DepositShape;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpaceAsteroidFieldFeature extends Feature<SpaceAsteroidFieldFeatureConfig> {

    public SpaceAsteroidFieldFeature(final Codec<SpaceAsteroidFieldFeatureConfig> codec) {
        super(codec);
    }

    private static AsteroidDescriptor createAsteroid(
            final SpaceDimensionProfile profile,
            final SpaceAsteroidFieldFeatureConfig config,
            final long seed,
            final int cellX,
            final int cellY,
            final int cellZ,
            final int minY,
            final int maxY
    ) {
        final long localSeed = mix(seed, cellX, cellY, cellZ, 0x4A91F3267B2D5D3FL);
        final RandomSource random = RandomSource.create(localSeed);

        final double centerX = (cellX * (double) config.horizontalCell()) + randomRange(random, 0.18D, 0.82D) * config.horizontalCell();
        final double centerY = (cellY * (double) config.verticalCell()) + randomRange(random, 0.18D, 0.82D) * config.verticalCell();
        final double centerZ = (cellZ * (double) config.horizontalCell()) + randomRange(random, 0.18D, 0.82D) * config.horizontalCell();

        if (centerY < minY - config.maxRadius() || centerY > maxY + config.maxRadius()) {
            return null;
        }

        final double beltA = 1.0D - Mth.clamp(Math.abs(centerY - 92.0D) / 86.0D, 0.0D, 1.0D);
        final double beltB = 1.0D - Mth.clamp(Math.abs(centerY - 208.0D) / 72.0D, 0.0D, 1.0D);
        final double belt = Math.max(beltA, beltB);
        final double cluster = valueNoise2D(seed ^ 0x25DE9A341L, cellX / 3, cellZ / 3);
        final double occupancy = 0.04D + belt * 0.46D + cluster * 0.22D;

        if (random.nextDouble() > occupancy) {
            return null;
        }

        final SizeClass sizeClass = pickSizeClass(random, occupancy);
        final AsteroidMaterialType type = pickType(profile, random, sizeClass);
        final double baseRadius = sizeClass.radiusMin + random.nextDouble() * (sizeClass.radiusMax - sizeClass.radiusMin);
        final double radiusX = baseRadius * randomRange(random, 0.78D, 1.32D);
        final double radiusY = baseRadius * randomRange(random, 0.68D, 1.18D);
        final double radiusZ = baseRadius * randomRange(random, 0.78D, 1.34D);
        final double roughness = sizeClass == SizeClass.MASSIVE ? 0.27D : randomRange(random, 0.11D, 0.24D);
        final double shellThickness = Math.max(1.25D, baseRadius * randomRange(random, 0.12D, 0.22D));
        final double yaw = random.nextDouble() * Math.PI * 2.0D;
        final double pitch = randomRange(random, -0.50D, 0.50D);
        final double fractureWidth = type == AsteroidMaterialType.HOLLOW || type == AsteroidMaterialType.SPECIAL
                ? randomRange(random, 0.0D, 1.4D)
                : randomRange(random, 0.0D, 0.8D);
        final List<VoidPocket> pockets = createPockets(random, type, baseRadius);
        final List<ImpactCrater> craters = createCraters(random, type, baseRadius);
        final List<OreDeposit> deposits = createDeposits(profile.body().name(), random, type, baseRadius, shellThickness);
        final List<AsteroidDescriptor> fragments = createFragments(profile, random, sizeClass, type, centerX, centerY, centerZ, baseRadius, yaw, pitch, shellThickness);

        return new AsteroidDescriptor(
                centerX,
                centerY,
                centerZ,
                radiusX,
                radiusY,
                radiusZ,
                baseRadius,
                yaw,
                pitch,
                roughness,
                shellThickness,
                fractureWidth,
                type,
                selectPalette(profile, type),
                pockets,
                craters,
                deposits,
                fragments
        );
    }

    private static List<VoidPocket> createPockets(final RandomSource random, final AsteroidMaterialType type, final double baseRadius) {
        final List<VoidPocket> pockets = new ArrayList<>();
        int count = switch (type) {
            case POROUS -> 4 + random.nextInt(4);
            case HOLLOW -> 1 + random.nextInt(2);
            case SPECIAL -> 1 + random.nextInt(3);
            default -> random.nextFloat() < 0.18F ? 1 : 0;
        };

        for (int i = 0; i < count; i++) {
            final Vec3 center = randomUnit(random).scale(baseRadius * randomRange(random, 0.12D, 0.45D));
            final double radius = switch (type) {
                case HOLLOW -> baseRadius * randomRange(random, 0.35D, 0.52D);
                case POROUS -> baseRadius * randomRange(random, 0.10D, 0.24D);
                case SPECIAL -> baseRadius * randomRange(random, 0.18D, 0.34D);
                default -> baseRadius * randomRange(random, 0.08D, 0.18D);
            };
            pockets.add(new VoidPocket(center, radius));
        }
        return pockets;
    }

    private static List<ImpactCrater> createCraters(final RandomSource random, final AsteroidMaterialType type, final double baseRadius) {
        final List<ImpactCrater> craters = new ArrayList<>();
        final int count = switch (type) {
            case HOLLOW, POROUS -> 2 + random.nextInt(4);
            case SPECIAL -> 3 + random.nextInt(4);
            default -> 1 + random.nextInt(3);
        };

        for (int i = 0; i < count; i++) {
            craters.add(new ImpactCrater(
                    randomUnit(random),
                    baseRadius * randomRange(random, 0.18D, 0.44D),
                    baseRadius * randomRange(random, 0.10D, 0.24D)
            ));
        }
        return craters;
    }

    private static List<OreDeposit> createDeposits(
            final String bodyName,
            final RandomSource random,
            final AsteroidMaterialType type,
            final double baseRadius,
            final double shellThickness
    ) {
        final List<AsteroidOreDefinition> supported = AsteroidOreRegistry.getDefinitions(bodyName).stream()
                .filter(definition -> definition.supports(type))
                .toList();
        if (supported.isEmpty()) {
            return List.of();
        }

        final int count = switch (type) {
            case ORE_RICH -> 4 + random.nextInt(4);
            case METALLIC -> 3 + random.nextInt(3);
            case MIXED -> 2 + random.nextInt(3);
            case ROCKY, HOLLOW -> 1 + random.nextInt(2);
            case POROUS -> random.nextBoolean() ? 1 : 2;
            case SPECIAL -> 2 + random.nextInt(3);
            case ICY -> 1 + random.nextInt(2);
        };

        final List<OreDeposit> deposits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final AsteroidOreDefinition definition = pickWeighted(random, supported);
            if (definition == null) {
                continue;
            }
            if (random.nextInt(Math.max(1, definition.rarity())) != 0) {
                continue;
            }

            final double depth = definition.minDepth() + random.nextDouble() * Math.max(0.01D, definition.maxDepth() - definition.minDepth());
            final Vec3 anchor = randomUnit(random).scale(baseRadius * depth);
            final double size = definition.minVeinSize() + random.nextDouble() * Math.max(1.0D, definition.maxVeinSize() - definition.minVeinSize());
            final Vec3 end = definition.shape() == DepositShape.VEIN
                    ? anchor.add(randomUnit(random).scale(size * randomRange(random, 1.2D, 2.2D)))
                    : anchor;
            final double shellMin = Math.max(0.0D, baseRadius - shellThickness - size * 0.5D);
            final double shellMax = Math.max(shellMin + 1.0D, baseRadius - shellThickness * 0.2D);

            deposits.add(new OreDeposit(definition.state(), definition.shape(), anchor, end, size, shellMin, shellMax));
        }
        return deposits;
    }

    private static List<AsteroidDescriptor> createFragments(
            final SpaceDimensionProfile profile,
            final RandomSource random,
            final SizeClass sizeClass,
            final AsteroidMaterialType parentType,
            final double centerX,
            final double centerY,
            final double centerZ,
            final double baseRadius,
            final double parentYaw,
            final double parentPitch,
            final double shellThickness
    ) {
        if (sizeClass.ordinal() < SizeClass.LARGE.ordinal() || random.nextFloat() > 0.62F) {
            return List.of();
        }

        final int count = 1 + random.nextInt(sizeClass == SizeClass.MASSIVE ? 4 : 3);
        final List<AsteroidDescriptor> fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final double scale = randomRange(random, 0.20D, 0.42D);
            final double radius = Math.max(2.4D, baseRadius * scale);
            final Vec3 offset = randomUnit(random).scale(baseRadius * randomRange(random, 1.4D, 2.4D));
            final AsteroidMaterialType type = random.nextFloat() < 0.65F ? parentType : pickType(profile, random, SizeClass.SMALL);
            fragments.add(new AsteroidDescriptor(
                    centerX + offset.x,
                    centerY + offset.y,
                    centerZ + offset.z,
                    radius * randomRange(random, 0.82D, 1.18D),
                    radius * randomRange(random, 0.76D, 1.12D),
                    radius * randomRange(random, 0.82D, 1.18D),
                    radius,
                    parentYaw + randomRange(random, -0.8D, 0.8D),
                    parentPitch + randomRange(random, -0.4D, 0.4D),
                    randomRange(random, 0.12D, 0.20D),
                    Math.max(1.0D, shellThickness * scale),
                    randomRange(random, 0.0D, 0.6D),
                    type,
                    selectPalette(profile, type),
                    createPockets(random, type, radius),
                    createCraters(random, type, radius),
                    createDeposits(profile.body().name(), random, type, radius, Math.max(1.0D, shellThickness * scale)),
                    List.of()
            ));
        }
        return fragments;
    }

    private static AsteroidMaterialType pickType(final SpaceDimensionProfile profile, final RandomSource random, final SizeClass sizeClass) {
        int rocky = 28;
        int metallic = 14;
        int mixed = 20;
        int porous = 12;
        int rich = sizeClass.ordinal() >= SizeClass.LARGE.ordinal() ? 12 : 8;
        int hollow = 8;
        int icy = profile.body().temperature() < -40 ? 12 : 4;
        int special = sizeClass == SizeClass.MASSIVE ? 5 : 2;

        final int total = rocky + metallic + mixed + porous + rich + hollow + icy + special;
        int pick = random.nextInt(total);
        if ((pick -= rocky) < 0) return AsteroidMaterialType.ROCKY;
        if ((pick -= metallic) < 0) return AsteroidMaterialType.METALLIC;
        if ((pick -= mixed) < 0) return AsteroidMaterialType.MIXED;
        if ((pick -= porous) < 0) return AsteroidMaterialType.POROUS;
        if ((pick -= rich) < 0) return AsteroidMaterialType.ORE_RICH;
        if ((pick -= hollow) < 0) return AsteroidMaterialType.HOLLOW;
        if ((pick -= icy) < 0) return AsteroidMaterialType.ICY;
        return AsteroidMaterialType.SPECIAL;
    }

    private static SizeClass pickSizeClass(final RandomSource random, final double occupancy) {
        final double roll = random.nextDouble() - occupancy * 0.08D;
        if (roll < 0.48D) {
            return SizeClass.SMALL;
        }
        if (roll < 0.80D) {
            return SizeClass.MEDIUM;
        }
        if (roll < 0.95D) {
            return SizeClass.LARGE;
        }
        if (roll < 0.992D) {
            return SizeClass.HUGE;
        }
        return SizeClass.MASSIVE;
    }

    private static AsteroidPalette selectPalette(final SpaceDimensionProfile profile, final AsteroidMaterialType type) {
        return switch (type) {
            case ROCKY ->
                    new AsteroidPalette(profile.topBlock(), profile.subsurfaceBlock(), profile.defaultBlock(), profile.rimBlock());
            case METALLIC ->
                    new AsteroidPalette(profile.rimBlock(), profile.cobbleBlock(), Blocks.PACKED_ICE.defaultBlockState(), profile.defaultBlock());
            case MIXED ->
                    new AsteroidPalette(profile.topBlock(), profile.defaultBlock(), profile.subsurfaceBlock(), profile.rimBlock());
            case POROUS ->
                    new AsteroidPalette(profile.cobbleBlock(), profile.topBlock(), profile.defaultBlock(), profile.rimBlock());
            case ORE_RICH ->
                    new AsteroidPalette(profile.topBlock(), profile.subsurfaceBlock(), profile.rimBlock(), profile.defaultBlock());
            case HOLLOW ->
                    new AsteroidPalette(profile.topBlock(), profile.defaultBlock(), profile.subsurfaceBlock(), profile.cobbleBlock());
            case ICY ->
                    new AsteroidPalette(Blocks.PACKED_ICE.defaultBlockState(), Blocks.BLUE_ICE.defaultBlockState(), profile.topBlock(), Blocks.SNOW_BLOCK.defaultBlockState());
            case SPECIAL ->
                    new AsteroidPalette(profile.topBlock(), profile.topBlock(), profile.cobbleBlock(), profile.cobbleBlock());
        };
    }

    private static boolean placeAsteroidSlice(
            final WorldGenLevel level,
            final SpaceDimensionProfile profile,
            final AsteroidDescriptor asteroid,
            final int minChunkX,
            final int maxChunkX,
            final int minChunkZ,
            final int maxChunkZ,
            final int minY,
            final int maxY,
            final BlockPos.MutableBlockPos mutablePos
    ) {
        final int minX = Math.max(minChunkX, Mth.floor(asteroid.centerX() - asteroid.radiusX() - 2.0D));
        final int maxX = Math.min(maxChunkX, Mth.ceil(asteroid.centerX() + asteroid.radiusX() + 2.0D));
        final int minAstY = Math.max(minY, Mth.floor(asteroid.centerY() - asteroid.radiusY() - 2.0D));
        final int maxAstY = Math.min(maxY, Mth.ceil(asteroid.centerY() + asteroid.radiusY() + 2.0D));
        final int minZ = Math.max(minChunkZ, Mth.floor(asteroid.centerZ() - asteroid.radiusZ() - 2.0D));
        final int maxZ = Math.min(maxChunkZ, Mth.ceil(asteroid.centerZ() + asteroid.radiusZ() + 2.0D));

        boolean placedAny = false;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minAstY; y <= maxAstY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    final LocalSample sample = sampleAsteroid(asteroid, x + 0.5D, y + 0.5D, z + 0.5D);
                    if (!sample.solid()) {
                        continue;
                    }

                    BlockState state = chooseBaseState(asteroid, sample);
                    final BlockState oreState = pickOreState(asteroid, sample, x, y, z);
                    if (oreState != null) {
                        state = oreState;
                    }

                    mutablePos.set(x, y, z);
                    final BlockState current = level.getBlockState(mutablePos);
                    if (!current.isAir() && !current.canBeReplaced()) {
                        continue;
                    }

                    level.setBlock(mutablePos, state, 2);
                    placedAny = true;
                }
            }
        }

        return placedAny;
    }

    private static LocalSample sampleAsteroid(final AsteroidDescriptor asteroid, final double worldX, final double worldY, final double worldZ) {
        final Vec3 rotated = rotate(worldX - asteroid.centerX(), worldY - asteroid.centerY(), worldZ - asteroid.centerZ(), asteroid.yaw(), asteroid.pitch());
        final double nx = rotated.x / asteroid.radiusX();
        final double ny = rotated.y / asteroid.radiusY();
        final double nz = rotated.z / asteroid.radiusZ();

        final double base = nx * nx + ny * ny + nz * nz;
        final double rough = (
                Math.sin((rotated.x * 0.19D) + asteroid.yaw() * 2.4D)
                        + Math.sin((rotated.y * 0.24D) - asteroid.pitch() * 2.1D)
                        + Math.sin((rotated.z * 0.17D) + asteroid.yaw() * 1.7D)
                        + Math.sin(((rotated.x + rotated.z) * 0.11D) - asteroid.pitch() * 1.9D)
        ) * 0.25D * asteroid.roughness();

        final double density = base + rough;
        if (density > 1.0D) {
            return LocalSample.VOID;
        }

        final double radial = Math.sqrt(Math.max(0.0D, base));
        final double actualRadius = asteroid.radius() * (1.0D - radial);

        for (VoidPocket pocket : asteroid.pockets()) {
            if (rotated.distanceTo(pocket.center()) <= pocket.radius()) {
                return LocalSample.VOID;
            }
        }

        for (ImpactCrater crater : asteroid.craters()) {
            final Vec3 craterCenter = crater.direction().scale(asteroid.radius() - crater.depth());
            if (rotated.distanceTo(craterCenter) <= crater.radius() && rotated.dot(crater.direction()) > asteroid.radius() * 0.15D) {
                return LocalSample.VOID;
            }
        }

        if (asteroid.fractureWidth() > 0.0D) {
            final double fracture = Math.abs(rotated.x * 0.62D + rotated.y * 0.18D - rotated.z * 0.31D);
            if (fracture < asteroid.fractureWidth() && radial > 0.24D && radial < 0.96D) {
                return LocalSample.VOID;
            }
        }

        final double shellDepth = asteroid.radius() - actualRadius;
        return new LocalSample(true, rotated, radial, shellDepth);
    }

    private static BlockState chooseBaseState(final AsteroidDescriptor asteroid, final LocalSample sample) {
        if (sample.radial() > 0.84D) {
            return asteroid.palette().surface();
        }
        if (sample.shellDepth() <= asteroid.shellThickness()) {
            return asteroid.palette().mantle();
        }
        if (asteroid.type() == AsteroidMaterialType.SPECIAL && sample.radial() < 0.28D) {
            return asteroid.palette().accent();
        }
        if (asteroid.type() == AsteroidMaterialType.ICY && sample.radial() < 0.36D) {
            return asteroid.palette().accent();
        }
        return asteroid.palette().core();
    }

    private static BlockState pickOreState(final AsteroidDescriptor asteroid, final LocalSample sample, final int x, final int y, final int z) {
        for (OreDeposit deposit : asteroid.deposits()) {
            switch (deposit.shape()) {
                case CORE -> {
                    if (sample.radial() < 0.42D && sample.rotated().distanceTo(deposit.anchor()) <= deposit.radius()) {
                        return deposit.state();
                    }
                }
                case CLUSTER -> {
                    if (sample.rotated().distanceTo(deposit.anchor()) <= deposit.radius()) {
                        return deposit.state();
                    }
                }
                case VEIN -> {
                    if (distanceToSegment(sample.rotated(), deposit.anchor(), deposit.end()) <= deposit.radius() * 0.55D) {
                        return deposit.state();
                    }
                }
                case SHELL -> {
                    final double dist = sample.rotated().length();
                    if (dist >= deposit.shellMin() && dist <= deposit.shellMax()) {
                        return deposit.state();
                    }
                }
                case SCATTER -> {
                    if (sample.rotated().distanceTo(deposit.anchor()) <= deposit.radius() && scatterPass(x, y, z, deposit.state().getBlock().hashCode())) {
                        return deposit.state();
                    }
                }
            }
        }
        return null;
    }

    private static boolean scatterPass(final int x, final int y, final int z, final int salt) {
        final long mixed = mix(0xB19F12AE3L ^ salt, x, y, z, 0xC6A4A7935BD1E995L);
        return ((mixed >>> 4) & 3L) == 0L;
    }

    private static AsteroidOreDefinition pickWeighted(final RandomSource random, final List<AsteroidOreDefinition> supported) {
        int total = 0;
        for (AsteroidOreDefinition definition : supported) {
            total += Math.max(1, definition.weight());
        }
        if (total <= 0) {
            return null;
        }
        int pick = random.nextInt(total);
        for (AsteroidOreDefinition definition : supported) {
            pick -= Math.max(1, definition.weight());
            if (pick < 0) {
                return definition;
            }
        }
        return supported.get(0);
    }

    private static Vec3 rotate(final double x, final double y, final double z, final double yaw, final double pitch) {
        final double cosY = Math.cos(yaw);
        final double sinY = Math.sin(yaw);
        final double x1 = x * cosY - z * sinY;
        final double z1 = x * sinY + z * cosY;
        final double cosP = Math.cos(pitch);
        final double sinP = Math.sin(pitch);
        final double y1 = y * cosP - z1 * sinP;
        final double z2 = y * sinP + z1 * cosP;
        return new Vec3(x1, y1, z2);
    }

    private static double distanceToSegment(final Vec3 point, final Vec3 a, final Vec3 b) {
        final Vec3 ab = b.subtract(a);
        final double lengthSqr = ab.lengthSqr();
        if (lengthSqr < 1.0E-6D) {
            return point.distanceTo(a);
        }
        final double t = Mth.clamp(point.subtract(a).dot(ab) / lengthSqr, 0.0D, 1.0D);
        return point.distanceTo(a.add(ab.scale(t)));
    }

    private static Vec3 randomUnit(final RandomSource random) {
        final double theta = random.nextDouble() * Math.PI * 2.0D;
        final double phi = Math.acos(randomRange(random, -1.0D, 1.0D));
        final double sin = Math.sin(phi);
        return new Vec3(Math.cos(theta) * sin, Math.cos(phi), Math.sin(theta) * sin);
    }

    private static double randomRange(final RandomSource random, final double min, final double max) {
        return min + random.nextDouble() * (max - min);
    }

    private static double valueNoise2D(final long seed, final int x, final int z) {
        final long mixed = mix(seed, x, 0, z, 0x9E3779B97F4A7C15L);
        return ((mixed >>> 11) & 0x3FFFL) / 16383.0D;
    }

    private static long mix(final long seed, final int x, final int y, final int z, final long salt) {
        long value = seed ^ salt;
        value ^= (long) x * 0x632BE59BD9B4E019L;
        value ^= (long) y * 0x9E3779B97F4A7C15L;
        value ^= (long) z * 0x94D049BB133111EBL;
        value ^= (value >>> 33);
        value *= 0xff51afd7ed558ccdL;
        value ^= (value >>> 33);
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= (value >>> 33);
        return value;
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceAsteroidFieldFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final SpaceAsteroidFieldFeatureConfig config = context.config();
        final SpaceDimensionProfile profile = SpaceBodyProfiles.byName(config.bodyName());
        if (!profile.isAsteroidField()) {
            return false;
        }

        final ChunkAccess chunk = level.getChunk(context.origin());
        final ChunkPos chunkPos = chunk.getPos();
        final int minChunkX = chunkPos.getMinBlockX();
        final int maxChunkX = chunkPos.getMaxBlockX();
        final int minChunkZ = chunkPos.getMinBlockZ();
        final int maxChunkZ = chunkPos.getMaxBlockZ();
        final int minY = Math.max(config.minY(), level.getMinBuildHeight());
        final int maxY = Math.min(config.maxY(), level.getMaxBuildHeight() - 1);
        final int horizontalCell = Math.max(24, config.horizontalCell());
        final int verticalCell = Math.max(24, config.verticalCell());
        final int maxRadius = Math.max(10, config.maxRadius());
        final long seed = level.getSeed();

        final int minCellX = Mth.floor((minChunkX - maxRadius) / (double) horizontalCell);
        final int maxCellX = Mth.floor((maxChunkX + maxRadius) / (double) horizontalCell);
        final int minCellZ = Mth.floor((minChunkZ - maxRadius) / (double) horizontalCell);
        final int maxCellZ = Mth.floor((maxChunkZ + maxRadius) / (double) horizontalCell);
        final int minCellY = Mth.floor((minY - maxRadius) / (double) verticalCell);
        final int maxCellY = Mth.floor((maxY + maxRadius) / (double) verticalCell);

        final List<AsteroidDescriptor> asteroids = new ArrayList<>();
        for (int cellX = minCellX; cellX <= maxCellX; cellX++) {
            for (int cellZ = minCellZ; cellZ <= maxCellZ; cellZ++) {
                for (int cellY = minCellY; cellY <= maxCellY; cellY++) {
                    final AsteroidDescriptor asteroid = createAsteroid(profile, config, seed, cellX, cellY, cellZ, minY, maxY);
                    if (asteroid != null && asteroid.intersectsChunk(minChunkX, maxChunkX, minChunkZ, maxChunkZ, minY, maxY)) {
                        asteroids.add(asteroid);
                    }
                }
            }
        }

        asteroids.sort(Comparator.comparingDouble(AsteroidDescriptor::radius).reversed());

        boolean placedAny = false;
        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (AsteroidDescriptor asteroid : asteroids) {
            placedAny |= placeAsteroidSlice(level, profile, asteroid, minChunkX, maxChunkX, minChunkZ, maxChunkZ, minY, maxY, mutablePos);
            for (AsteroidDescriptor fragment : asteroid.fragments()) {
                if (fragment.intersectsChunk(minChunkX, maxChunkX, minChunkZ, maxChunkZ, minY, maxY)) {
                    placedAny |= placeAsteroidSlice(level, profile, fragment, minChunkX, maxChunkX, minChunkZ, maxChunkZ, minY, maxY, mutablePos);
                }
            }
        }

        return placedAny;
    }

    private enum SizeClass {
        SMALL(3.4D, 6.0D),
        MEDIUM(6.5D, 10.5D),
        LARGE(11.0D, 16.0D),
        HUGE(17.0D, 24.0D),
        MASSIVE(26.0D, 34.0D);

        private final double radiusMin;
        private final double radiusMax;

        SizeClass(final double radiusMin, final double radiusMax) {
            this.radiusMin = radiusMin;
            this.radiusMax = radiusMax;
        }
    }

    private record AsteroidPalette(BlockState surface, BlockState mantle, BlockState core, BlockState accent) {
    }

    private record VoidPocket(Vec3 center, double radius) {
    }

    private record ImpactCrater(Vec3 direction, double radius, double depth) {
    }

    private record OreDeposit(BlockState state, DepositShape shape, Vec3 anchor, Vec3 end, double radius,
                              double shellMin, double shellMax) {
    }

    private record LocalSample(boolean solid, Vec3 rotated, double radial, double shellDepth) {
        private static final LocalSample VOID = new LocalSample(false, Vec3.ZERO, 0.0D, 0.0D);
    }

    private record AsteroidDescriptor(
            double centerX,
            double centerY,
            double centerZ,
            double radiusX,
            double radiusY,
            double radiusZ,
            double radius,
            double yaw,
            double pitch,
            double roughness,
            double shellThickness,
            double fractureWidth,
            AsteroidMaterialType type,
            AsteroidPalette palette,
            List<VoidPocket> pockets,
            List<ImpactCrater> craters,
            List<OreDeposit> deposits,
            List<AsteroidDescriptor> fragments
    ) {
        private boolean intersectsChunk(final int minX, final int maxX, final int minZ, final int maxZ, final int minY, final int maxY) {
            return this.centerX + this.radiusX >= minX
                    && this.centerX - this.radiusX <= maxX
                    && this.centerY + this.radiusY >= minY
                    && this.centerY - this.radiusY <= maxY
                    && this.centerZ + this.radiusZ >= minZ
                    && this.centerZ - this.radiusZ <= maxZ;
        }
    }
}
