package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SpaceAdvancedCaveFeature extends Feature<SpaceCaveFeatureConfig> {

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    public SpaceAdvancedCaveFeature(final Codec<SpaceCaveFeatureConfig> codec) {
        super(codec);
    }

    private static void addCandidate(
            final List<Node> nodes,
            final Node current,
            final boolean[][][] visited,
            final int width,
            final int height,
            final int depth,
            final int dx,
            final int dy,
            final int dz
    ) {
        final int nx = current.x + dx;
        final int ny = current.y + dy;
        final int nz = current.z + dz;

        if (nx >= 0 && nx < width && ny >= 0 && ny < height && nz >= 0 && nz < depth && !visited[nx][ny][nz]) {
            nodes.add(new Node(nx, ny, nz));
        }
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceCaveFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        if (random.nextFloat() < 0.99)
            return false;
        final BlockPos origin = context.origin();
        final SpaceCaveFeatureConfig cfg = context.config();

        final Predicate<BlockState> replaceable = state ->
                SpaceFeatureUtils.isReplaceableForCarving(state, cfg.primary(), cfg.secondary(), cfg.tertiary());

        final LocalArea area = LocalArea.from(level, origin);

        return switch (cfg.kind()) {
            case TUNNEL_CAVES -> placeTunnelCaves(level, origin, random, cfg, replaceable, area);
            case LABYRINTH_CAVES -> placeLabyrinthCaves(level, origin, random, cfg, replaceable, area);
            case SHAFT_CAVES -> placeShaftCaves(level, origin, random, cfg, replaceable, area);
            case DOME_CAVES -> placeDomeCaves(level, origin, random, cfg, replaceable, area);
            case WELL_CAVES -> placeWellCaves(level, origin, random, cfg, replaceable, area);
            case MULTI_TIER_CAVES -> placeMultiTierCaves(level, origin, random, cfg, replaceable, area);
            case BRANCHED_CAVES -> placeBranchedCaves(level, origin, random, cfg, replaceable, area);
            case SPIRAL_CAVES -> placeSpiralCaves(level, origin, random, cfg, replaceable, area);
            case ARCH_CAVES -> placeArchCaves(level, origin, random, cfg, replaceable, area);
            case CHAMBER_CAVES -> placeChamberCaves(level, origin, random, cfg, replaceable, area);
            case CANYON_CAVES -> placeCanyonCaves(level, origin, random, cfg, replaceable, area);
            case POCKET_CAVES -> placePocketCaves(level, origin, random, cfg, replaceable, area);
            case CREVICE_CAVES -> placeCreviceCaves(level, origin, random, cfg, replaceable, area);
            case AMPHITHEATER_CAVES -> placeAmphitheaterCaves(level, origin, random, cfg, replaceable, area);
            case CATHEDRAL_CAVES -> placeCathedralCaves(level, origin, random, cfg, replaceable, area);
            case GALLERY_CAVES -> placeGalleryCaves(level, origin, random, cfg, replaceable, area);
            case WINDING_CAVES -> placeWindingCaves(level, origin, random, cfg, replaceable, area);
            case CASCADE_CAVES -> placeCascadeCaves(level, origin, random, cfg, replaceable, area);
            case COLLAPSE_CAVES -> placeCollapseCaves(level, origin, random, cfg, replaceable, area);
            case VERTICAL_COLLECTORS -> placeVerticalCollectors(level, origin, random, cfg, replaceable, area);
            default -> false;
        };
    }

    private boolean placeTunnelCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int systems = Mth.nextInt(random, 2, 4);
        final List<Vec3> endpoints = new ArrayList<>(systems);

        final int minRadius = boundedMinRadius(cfg);
        final int maxRadius = boundedMaxRadius(cfg);
        final int length = boundedLength(cfg, 24, 40);

        for (int i = 0; i < systems; i++) {
            final BlockPos startPos = randomOffset(area, origin, random, 7, boundedVertical(cfg), maxRadius, maxRadius, maxRadius);
            final Vec3 start = vec(startPos);

            final float yaw = random.nextFloat() * ((float) Math.PI * 2.0F);
            final float pitch = (random.nextFloat() - 0.5F) * 0.12F;

            final Vec3 end = carveGuidedTunnel(
                    level,
                    area,
                    start,
                    yaw,
                    pitch,
                    length,
                    Math.max(2.25D, (minRadius + maxRadius) * 0.52D),
                    0.08F,
                    0.05F,
                    boundedVertical(cfg),
                    AIR,
                    replaceable,
                    random
            );
            endpoints.add(end);
        }

        for (int i = 1; i < endpoints.size(); i++) {
            if (random.nextBoolean()) {
                carveLine(
                        level,
                        area,
                        endpoints.get(i - 1),
                        endpoints.get(i),
                        Math.max(1, minRadius),
                        Math.max(1, minRadius),
                        Math.max(1, minRadius),
                        AIR,
                        replaceable
                );
            }
        }

        return true;
    }

    private boolean placeLabyrinthCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int width = Mth.nextInt(random, 3, 5);
        final int height = Mth.nextInt(random, 2, 4);
        final int depth = Mth.nextInt(random, 3, 5);

        final int roomRadius = Math.max(2, boundedMaxRadius(cfg));
        final int corridorRadius = Math.max(1, boundedMinRadius(cfg));
        final double spacing = Math.max(4.75D, roomRadius * 2.1D);

        final boolean[][][] visited = new boolean[width][height][depth];
        final ArrayDeque<Node> stack = new ArrayDeque<>();
        final List<Connection> carved = new ArrayList<>(width * height * depth * 2);
        stack.push(new Node(width / 2, 0, depth / 2));

        while (!stack.isEmpty()) {
            final Node current = stack.peek();
            visited[current.x][current.y][current.z] = true;

            final List<Node> candidates = new ArrayList<>(6);
            addCandidate(candidates, current, visited, width, height, depth, 1, 0, 0);
            addCandidate(candidates, current, visited, width, height, depth, -1, 0, 0);
            addCandidate(candidates, current, visited, width, height, depth, 0, 0, 1);
            addCandidate(candidates, current, visited, width, height, depth, 0, 0, -1);
            addCandidate(candidates, current, visited, width, height, depth, 0, 1, 0);
            addCandidate(candidates, current, visited, width, height, depth, 0, -1, 0);

            if (candidates.isEmpty()) {
                stack.pop();
                continue;
            }

            final Node next = candidates.get(random.nextInt(candidates.size()));
            carved.add(new Connection(current, next));
            stack.push(next);
        }

        final int extraLoops = Mth.nextInt(random, 2, Math.max(3, width + depth));
        for (int i = 0; i < extraLoops; i++) {
            final Node a = new Node(random.nextInt(width), random.nextInt(height), random.nextInt(depth));
            final Node b = new Node(
                    Mth.clamp(a.x + random.nextInt(3) - 1, 0, width - 1),
                    Mth.clamp(a.y + random.nextInt(3) - 1, 0, height - 1),
                    Mth.clamp(a.z + random.nextInt(3) - 1, 0, depth - 1)
            );
            if (!a.equals(b)) {
                carved.add(new Connection(a, b));
            }
        }

        final Vec3 center = vec(area.sampleAround(random, origin, 4, boundedVertical(cfg), roomRadius, roomRadius, roomRadius));
        final double xOffset = (width - 1) * spacing * 0.5D;
        final double yOffset = (height - 1) * spacing * 0.45D;
        final double zOffset = (depth - 1) * spacing * 0.5D;

        for (final Connection connection : carved) {
            final Vec3 a = area.clampCenter(
                    new Vec3(
                            center.x + connection.a.x * spacing - xOffset,
                            center.y + connection.a.y * spacing * 0.80D - yOffset,
                            center.z + connection.a.z * spacing - zOffset
                    ),
                    roomRadius,
                    roomRadius,
                    roomRadius
            );

            final Vec3 b = area.clampCenter(
                    new Vec3(
                            center.x + connection.b.x * spacing - xOffset,
                            center.y + connection.b.y * spacing * 0.80D - yOffset,
                            center.z + connection.b.z * spacing - zOffset
                    ),
                    roomRadius,
                    roomRadius,
                    roomRadius
            );

            carveEllipsoid(level, area, a, roomRadius, roomRadius, roomRadius, AIR, replaceable);
            carveEllipsoid(level, area, b, roomRadius, roomRadius, roomRadius, AIR, replaceable);
            carveLine(level, area, a, b, corridorRadius, corridorRadius, corridorRadius, AIR, replaceable);

            if (random.nextFloat() < 0.35F) {
                carvePocketCluster(level, area, a, random, Math.max(2, roomRadius - 1), replaceable);
            }
            if (random.nextFloat() < 0.35F) {
                carvePocketCluster(level, area, b, random, Math.max(2, roomRadius - 1), replaceable);
            }
        }

        return true;
    }

    private boolean placeShaftCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int shafts = Mth.nextInt(random, 1, 3);
        final int minRadius = boundedMinRadius(cfg);
        final int maxRadius = boundedMaxRadius(cfg);

        for (int i = 0; i < shafts; i++) {
            final int radius = Mth.nextInt(random, minRadius, maxRadius);
            final BlockPos shaftOrigin = randomOffset(area, origin, random, 5, 0, radius, 1, radius);

            final int topY = cfg.openToSurface()
                    ? safeSurfaceY(level, area, shaftOrigin.getX(), shaftOrigin.getZ()) - 1
                    : area.clampY(shaftOrigin.getY() + random.nextInt(Math.max(1, boundedVertical(cfg))), radius);

            final int depth = boundedLength(cfg, 12, 26);

            for (int step = 0; step < depth; step++) {
                final BlockPos center = area.clampCenter(new BlockPos(shaftOrigin.getX(), topY - step, shaftOrigin.getZ()), radius, 1, radius);
                final BlockState fill = (cfg.flooded() && step > depth - 4) ? safeFluid(cfg) : AIR;
                carveSphere(level, area, center.getX(), center.getY(), center.getZ(), radius, 1, radius, fill, replaceable);

                if (step % 8 == 0 && step > 4) {
                    final float yaw = random.nextFloat() * ((float) Math.PI * 2.0F);
                    final Vec3 branchEnd = area.clampCenter(
                            vec(center).add(
                                    Math.cos(yaw) * (5.0D + random.nextInt(5)),
                                    random.nextInt(5) - 2,
                                    Math.sin(yaw) * (5.0D + random.nextInt(5))
                            ),
                            Math.max(1, radius - 1),
                            Math.max(1, radius - 1),
                            Math.max(1, radius - 1)
                    );
                    carveLine(level, area, vec(center), branchEnd, Math.max(1, radius - 1), Math.max(1, radius - 1), Math.max(1, radius - 1), AIR, replaceable);
                }
            }
        }
        return true;
    }

    private boolean placeDomeCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int domes = Mth.nextInt(random, 2, 3);
        Vec3 previous = null;

        for (int i = 0; i < domes; i++) {
            final int rx = Mth.nextInt(random, Math.max(3, boundedMinRadius(cfg) + 1), Math.max(4, boundedMaxRadius(cfg) + 1));
            final int ry = Math.max(rx + 2, boundedMaxRadius(cfg) + 2);
            final int rz = Mth.nextInt(random, Math.max(3, boundedMinRadius(cfg) + 1), Math.max(4, boundedMaxRadius(cfg) + 1));

            final BlockPos centerPos = randomOffset(area, origin, random, 6, boundedVertical(cfg), rx, ry, rz);
            final Vec3 center = vec(centerPos);

            carveEllipsoid(level, area, center, rx, ry, rz, AIR, replaceable);
            paintShell(level, area, center, rx + 1, ry + 1, rz + 1, cfg.secondary(), replaceable);

            carveEllipsoid(
                    level,
                    area,
                    center.add(0.0D, -ry * 0.30D, 0.0D),
                    Math.max(2, rx - 1),
                    Math.max(2, ry / 2),
                    Math.max(2, rz - 1),
                    AIR,
                    replaceable
            );

            carvePocketCluster(level, area, center.add(0.0D, -ry * 0.15D, 0.0D), random, Math.max(2, boundedMinRadius(cfg) + 1), replaceable);

            if (previous != null) {
                carveLine(
                        level,
                        area,
                        previous,
                        center,
                        Math.max(1, boundedMinRadius(cfg)),
                        Math.max(1, boundedMinRadius(cfg)),
                        Math.max(1, boundedMinRadius(cfg)),
                        AIR,
                        replaceable
                );
            }

            previous = center;
        }

        return true;
    }

    private boolean placeWellCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int wells = 1;
        final int minRadius = boundedMinRadius(cfg);
        final int maxRadius = boundedMaxRadius(cfg);

        for (int i = 0; i < wells; i++) {
            final int radius = Mth.nextInt(random, minRadius, maxRadius);
            final BlockPos center = randomOffset(area, origin, random, 5, 0, radius, 1, radius);

            final int topY = cfg.openToSurface()
                    ? safeSurfaceY(level, area, center.getX(), center.getZ()) - 1
                    : area.clampY(center.getY(), radius);

            final int depth = boundedLength(cfg, 14, 24);

            for (int y = 0; y < depth; y++) {
                final BlockPos slice = area.clampCenter(new BlockPos(center.getX(), topY - y, center.getZ()), radius, 1, radius);
                carveSphere(level, area, slice.getX(), slice.getY(), slice.getZ(), radius, 1, radius, AIR, replaceable);

                if (y % 10 == 0) {
                    paintShell(level, area, vec(slice), radius + 1, 2, radius + 1, cfg.secondary(), replaceable);
                }
            }

            final BlockPos bottom = area.clampCenter(new BlockPos(center.getX(), topY - depth, center.getZ()), radius + 2, Math.max(3, radius), radius + 2);
            carveEllipsoid(level, area, vec(bottom), radius + 2, Math.max(3, radius), radius + 2, AIR, replaceable);

            if (cfg.flooded()) {
                carveEllipsoid(level, area, vec(bottom).add(0.0D, -1.0D, 0.0D), Math.max(1, radius - 1), 1, Math.max(1, radius - 1), safeFluid(cfg), replaceable);
            }

            final float yaw = random.nextFloat() * ((float) Math.PI * 2.0F);
            final Vec3 branch = area.clampCenter(
                    vec(bottom).add(Math.cos(yaw) * (5.0D + random.nextInt(5)), 0.0D, Math.sin(yaw) * (5.0D + random.nextInt(5))),
                    Math.max(1, radius - 1),
                    Math.max(1, radius - 1),
                    Math.max(1, radius - 1)
            );
            carveLine(level, area, vec(bottom), branch, Math.max(1, radius - 1), Math.max(1, radius - 1), Math.max(1, radius - 1), AIR, replaceable);
        }
        return true;
    }

    private boolean placeMultiTierCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int tiers = Mth.nextInt(random, 4, 5);
        final int verticalStep = Mth.nextInt(random, 7, 11);
        Vec3 previous = null;
        final Vec3 central = vec(area.sampleAround(random, origin, 4, boundedVertical(cfg), boundedMaxRadius(cfg), boundedMaxRadius(cfg), boundedMaxRadius(cfg)));

        for (int i = 0; i < tiers; i++) {
            final int rx = Mth.nextInt(random, Math.max(2, boundedMinRadius(cfg) + 1), Math.max(3, boundedMaxRadius(cfg)));
            final int ry = Math.max(3, rx);
            final int rz = Mth.nextInt(random, Math.max(2, boundedMinRadius(cfg) + 1), Math.max(3, boundedMaxRadius(cfg)));

            final Vec3 chamberCenter = area.clampCenter(
                    central.add(random.nextInt(9) - 4, -i * verticalStep, random.nextInt(9) - 4),
                    rx,
                    ry,
                    rz
            );

            carveEllipsoid(level, area, chamberCenter, rx, ry, rz, AIR, replaceable);
            paintShell(level, area, chamberCenter, rx + 1, ry + 1, rz + 1, cfg.secondary(), replaceable);

            if (random.nextFloat() < 0.45F) {
                carvePocketCluster(level, area, chamberCenter, random, Math.max(2, boundedMinRadius(cfg)), replaceable);
            }

            if (previous != null) {
                carveLine(
                        level,
                        area,
                        previous,
                        chamberCenter,
                        Math.max(1, boundedMinRadius(cfg)),
                        Math.max(2, boundedMinRadius(cfg) + 1),
                        Math.max(1, boundedMinRadius(cfg)),
                        AIR,
                        replaceable
                );
            }

            previous = chamberCenter;
        }

        return true;
    }

    private boolean placeBranchedCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int maxRadius = boundedMaxRadius(cfg);
        final int minRadius = boundedMinRadius(cfg);

        final Vec3 start = vec(area.clampCenter(origin.offset(-6, random.nextInt(7) - 3, -6), maxRadius, maxRadius, maxRadius));
        final Vec3 end = vec(area.clampCenter(origin.offset(6, random.nextInt(7) - 3, 6), maxRadius, maxRadius, maxRadius));

        carveLine(level, area, start, end, maxRadius, Math.max(2, maxRadius - 1), maxRadius, AIR, replaceable);

        final int branches = Mth.nextInt(random, 4, 7);
        for (int i = 0; i < branches; i++) {
            final double t = (i + 1) / (double) (branches + 1);
            final Vec3 branchStart = start.lerp(end, t);
            final float yaw = random.nextFloat() * ((float) Math.PI * 2.0F);

            final Vec3 branchEnd = area.clampCenter(
                    branchStart.add(
                            Math.cos(yaw) * (5.0D + random.nextInt(6)),
                            random.nextInt(boundedVertical(cfg)) - boundedVertical(cfg) / 2.0D,
                            Math.sin(yaw) * (5.0D + random.nextInt(6))
                    ),
                    minRadius,
                    minRadius,
                    minRadius
            );

            carveLine(level, area, branchStart, branchEnd, minRadius, Math.max(1, minRadius), minRadius, AIR, replaceable);
        }
        return true;
    }

    private boolean placeSpiralCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final Vec3 center = vec(area.sampleAround(random, origin, 3, boundedVertical(cfg), boundedMaxRadius(cfg), boundedMaxRadius(cfg), boundedMaxRadius(cfg)));
        final double turns = 1.5D + random.nextDouble() * 0.9D;
        final double radius = Math.max(4.5D, boundedMaxRadius(cfg) + 1.5D);
        final double rise = Math.max(10.0D, boundedVertical(cfg) * 1.05D);
        final int steps = Math.max(30, Mth.ceil(turns * 28.0D));
        final boolean descending = random.nextBoolean();

        Vec3 previous = null;
        for (int i = 0; i <= steps; i++) {
            final double progress = i / (double) steps;
            final double angle = turns * Math.PI * 2.0D * progress;
            final double localRadius = radius * (1.0D - progress * 0.12D);
            final double y = center.y + (descending ? -1.0D : 1.0D) * rise * (progress - 0.5D);

            final Vec3 current = area.clampCenter(
                    new Vec3(
                            center.x + Math.cos(angle) * localRadius,
                            y,
                            center.z + Math.sin(angle) * localRadius
                    ),
                    boundedMinRadius(cfg),
                    boundedMinRadius(cfg),
                    boundedMinRadius(cfg)
            );

            if (previous != null) {
                carveLine(
                        level,
                        area,
                        previous,
                        current,
                        boundedMinRadius(cfg),
                        Math.max(1, boundedMinRadius(cfg)),
                        boundedMinRadius(cfg),
                        AIR,
                        replaceable
                );
            }

            if (i % 9 == 0 && i > 0 && i < steps) {
                carvePocketCluster(level, area, current, random, Math.max(2, boundedMinRadius(cfg)), replaceable);
            }

            previous = current;
        }

        if (previous != null) {
            carveEllipsoid(
                    level,
                    area,
                    previous,
                    Math.max(2, boundedMaxRadius(cfg)),
                    Math.max(4, boundedMaxRadius(cfg) + 1),
                    Math.max(2, boundedMaxRadius(cfg)),
                    AIR,
                    replaceable
            );
        }

        return true;
    }

    private boolean placeArchCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int maxRadius = boundedMaxRadius(cfg);
        final int minRadius = boundedMinRadius(cfg);

        final Vec3 start = vec(area.clampCenter(origin.offset(-6, 0, 0), maxRadius, maxRadius + 2, maxRadius));
        final Vec3 end = vec(area.clampCenter(origin.offset(6, 0, 0), maxRadius, maxRadius + 2, maxRadius));
        carveLine(level, area, start, end, maxRadius, maxRadius + 1, maxRadius, AIR, replaceable);

        final int ribs = Mth.nextInt(random, 3, 5);
        for (int i = 0; i < ribs; i++) {
            final double t = ribs == 1 ? 0.5D : i / (double) (ribs - 1);
            final Vec3 center = start.lerp(end, t);
            placeArchRib(level, area, BlockPos.containing(center), maxRadius + 1, maxRadius + 2, cfg.secondary());

            if (random.nextBoolean()) {
                final Vec3 pocket = area.clampCenter(center.add(0.0D, 0.0D, random.nextBoolean() ? 5.0D : -5.0D), maxRadius, maxRadius, maxRadius);
                carveEllipsoid(level, area, pocket, maxRadius, Math.max(3, maxRadius - 1), maxRadius, AIR, replaceable);
                carveLine(level, area, center, pocket, minRadius, Math.max(1, minRadius), minRadius, AIR, replaceable);
            }
        }
        return true;
    }

    private boolean placeChamberCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int chambers = Mth.nextInt(random, 3, 5);
        final List<Vec3> centers = new ArrayList<>(chambers);
        final Vec3 originVec = vec(origin);

        for (int i = 0; i < chambers; i++) {
            final int rx = Mth.nextInt(random, Math.max(2, boundedMinRadius(cfg) + 1), Math.max(3, boundedMaxRadius(cfg)));
            final int ry = Math.max(3, rx - 1);
            final int rz = Mth.nextInt(random, Math.max(2, boundedMinRadius(cfg) + 1), Math.max(3, boundedMaxRadius(cfg)));

            final BlockPos centerPos = randomOffset(area, origin, random, 6, boundedVertical(cfg), rx, ry, rz);
            final Vec3 center = vec(centerPos);
            centers.add(center);

            carveEllipsoid(level, area, center, rx, ry, rz, AIR, replaceable);
        }

        centers.sort((a, b) -> Double.compare(a.distanceToSqr(originVec), b.distanceToSqr(originVec)));

        for (int i = 1; i < centers.size(); i++) {
            carveLine(level, area, centers.get(i - 1), centers.get(i), Math.max(1, boundedMinRadius(cfg)), Math.max(1, boundedMinRadius(cfg)), Math.max(1, boundedMinRadius(cfg)), AIR, replaceable);
        }
        return true;
    }

    private boolean placeCanyonCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final double yaw = random.nextDouble() * Math.PI * 2.0D;
        final Vec3 direction = new Vec3(Math.cos(yaw), 0.0D, Math.sin(yaw));

        final Vec3 start = area.clampCenter(vec(origin).add(direction.scale(-6.0D)), boundedMaxRadius(cfg), boundedMaxRadius(cfg) + 2, boundedMinRadius(cfg));
        final Vec3 end = area.clampCenter(vec(origin).add(direction.scale(6.0D)), boundedMaxRadius(cfg), boundedMaxRadius(cfg) + 2, boundedMinRadius(cfg));

        final int width = Math.max(boundedMaxRadius(cfg), 4);
        final int height = Math.max(boundedMaxRadius(cfg) + 2, 6);
        final int thickness = Math.max(boundedMinRadius(cfg), 2);

        carveLine(level, area, start, end, width, height, thickness, AIR, replaceable);

        for (int i = 0; i < 3; i++) {
            final double t = (i + 1) / 4.0D;
            final Vec3 bend = area.clampCenter(
                    start.lerp(end, t).add(
                            direction.z * (random.nextBoolean() ? 3.0D : -3.0D),
                            random.nextInt(5) - 2,
                            -direction.x * (random.nextBoolean() ? 3.0D : -3.0D)
                    ),
                    width,
                    height,
                    thickness
            );
            carveEllipsoid(level, area, bend, width - 1, height - 1, thickness, AIR, replaceable);
        }
        return true;
    }

    private boolean placePocketCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int pockets = Mth.nextInt(random, 8, 14);

        for (int i = 0; i < pockets; i++) {
            final int radius = Mth.nextInt(random, boundedMinRadius(cfg), Math.max(boundedMinRadius(cfg), boundedMaxRadius(cfg) - 1));
            final BlockPos center = randomOffset(area, origin, random, 6, boundedVertical(cfg), radius, Math.max(2, radius - 1), radius);
            final Vec3 centerVec = vec(center);

            carveEllipsoid(level, area, centerVec, radius, Math.max(2, radius - 1), radius, AIR, replaceable);

            if (random.nextFloat() < 0.20F) {
                final Vec3 nearby = area.clampCenter(
                        centerVec.add(random.nextInt(5) - 2, random.nextInt(5) - 2, random.nextInt(5) - 2),
                        1,
                        1,
                        1
                );
                carveLine(level, area, centerVec, nearby, 1, 1, 1, AIR, replaceable);
            }
        }
        return true;
    }

    private boolean placeCreviceCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int crevices = Mth.nextInt(random, 1, 3);

        for (int i = 0; i < crevices; i++) {
            final double yaw = random.nextDouble() * Math.PI * 2.0D;
            final Vec3 start = vec(randomOffset(area, origin, random, 5, boundedVertical(cfg), 1, boundedMaxRadius(cfg) + 6, 1));
            final Vec3 end = area.clampCenter(
                    start.add(Math.cos(yaw) * 8.0D, random.nextInt(boundedVertical(cfg)) - boundedVertical(cfg) * 0.5D, Math.sin(yaw) * 8.0D),
                    1,
                    boundedMaxRadius(cfg) + 6,
                    1
            );

            final int steps = Math.max(6, Mth.ceil(start.distanceTo(end) / 2.0D));
            final int halfWidth = Math.max(1, boundedMinRadius(cfg));

            for (int s = 0; s <= steps; s++) {
                final Vec3 pos = start.lerp(end, s / (double) steps);
                final int height = Mth.nextInt(random, boundedMaxRadius(cfg) + 3, boundedMaxRadius(cfg) + 6);
                final Vec3 clamped = area.clampCenter(pos, halfWidth, height, halfWidth);
                carveEllipsoid(level, area, clamped, halfWidth, height, Math.max(1, halfWidth), AIR, replaceable);
            }
        }
        return true;
    }

    private boolean placeAmphitheaterCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int radius = Math.max(boundedMaxRadius(cfg) + 1, 5);
        final BlockPos center = randomOffset(area, origin, random, 4, boundedVertical(cfg), radius, radius, radius);
        final int tiers = 4;

        for (int tier = 0; tier < tiers; tier++) {
            final int tierRadius = Math.max(2, radius - tier);
            final BlockPos tierCenter = area.clampCenter(center.offset(0, tier * 2 - 2, 0), tierRadius, 1, tierRadius);

            for (int dz = -tierRadius; dz <= 0; dz++) {
                carveSphere(level, area, tierCenter.getX(), tierCenter.getY(), tierCenter.getZ() + dz, tierRadius, 1, Math.max(2, tierRadius - 1), AIR, replaceable);
            }
        }

        final BlockPos backWall = area.clampCenter(center.offset(0, 3, Math.max(2, radius / 2)), radius - 1, Math.max(4, boundedMaxRadius(cfg)), Math.max(2, radius / 2));
        carveSphere(level, area, backWall.getX(), backWall.getY(), backWall.getZ(), radius - 1, Math.max(4, boundedMaxRadius(cfg)), Math.max(2, radius / 2), AIR, replaceable);

        final BlockPos stage = area.clampCenter(center.offset(0, -2, -Math.max(2, radius / 3)), Math.max(2, boundedMaxRadius(cfg) - 1), 1, Math.max(2, boundedMaxRadius(cfg)));
        carveSphere(level, area, stage.getX(), stage.getY(), stage.getZ(), Math.max(2, boundedMaxRadius(cfg) - 1), 1, Math.max(2, boundedMaxRadius(cfg)), AIR, replaceable);

        return true;
    }

    private boolean placeCathedralCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int naveWidth = Math.max(boundedMaxRadius(cfg), 4);
        final int naveHeight = Math.max(boundedMaxRadius(cfg) + 4, 8);

        final Vec3 start = vec(area.clampCenter(origin.offset(-5, 0, 0), naveWidth, naveHeight, naveWidth));
        final Vec3 end = vec(area.clampCenter(origin.offset(5, 0, 0), naveWidth, naveHeight, naveWidth));

        carveLine(level, area, start, end, naveWidth, naveHeight, naveWidth, AIR, replaceable);
        carveEllipsoid(level, area, area.clampCenter(end.add(3.0D, 0.0D, 0.0D), naveWidth, naveHeight + 1, naveWidth), naveWidth, naveHeight + 1, naveWidth, AIR, replaceable);

        for (int i = -1; i <= 1; i++) {
            final Vec3 aisleNorth = area.clampCenter(vec(origin.offset(i * 4, 0, 4)), Math.max(2, boundedMaxRadius(cfg) - 1), Math.max(4, boundedMaxRadius(cfg) + 1), Math.max(2, boundedMaxRadius(cfg) - 1));
            final Vec3 aisleSouth = area.clampCenter(vec(origin.offset(i * 4, 0, -4)), Math.max(2, boundedMaxRadius(cfg) - 1), Math.max(4, boundedMaxRadius(cfg) + 1), Math.max(2, boundedMaxRadius(cfg) - 1));

            carveEllipsoid(level, area, aisleNorth, Math.max(2, boundedMaxRadius(cfg) - 1), Math.max(4, boundedMaxRadius(cfg) + 1), Math.max(2, boundedMaxRadius(cfg) - 1), AIR, replaceable);
            carveEllipsoid(level, area, aisleSouth, Math.max(2, boundedMaxRadius(cfg) - 1), Math.max(4, boundedMaxRadius(cfg) + 1), Math.max(2, boundedMaxRadius(cfg) - 1), AIR, replaceable);
        }

        for (int i = -1; i <= 1; i++) {
            placeColumn(level, area, origin.offset(i * 4, -boundedMaxRadius(cfg), 0), 1, naveHeight, cfg.secondary());
            placeColumn(level, area, origin.offset(i * 4, -boundedMaxRadius(cfg), 4), 1, Math.max(4, naveHeight - 1), cfg.secondary());
            placeColumn(level, area, origin.offset(i * 4, -boundedMaxRadius(cfg), -4), 1, Math.max(4, naveHeight - 1), cfg.secondary());
        }
        return true;
    }

    private boolean placeGalleryCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int corridor = Math.max(2, boundedMaxRadius(cfg) - 1);

        final Vec3 start = vec(area.clampCenter(origin.offset(-6, 0, 0), corridor, corridor, corridor));
        final Vec3 end = vec(area.clampCenter(origin.offset(6, 0, 0), corridor, corridor, corridor));

        carveLine(level, area, start, end, corridor, corridor, corridor, AIR, replaceable);

        for (int i = 0; i < 4; i++) {
            final double t = (i + 1) / 5.0D;
            final Vec3 center = start.lerp(end, t);

            final Vec3 north = area.clampCenter(center.add(0.0D, random.nextInt(5) - 2, 5.0D), boundedMaxRadius(cfg), Math.max(3, boundedMaxRadius(cfg) - 1), boundedMaxRadius(cfg));
            final Vec3 south = area.clampCenter(center.add(0.0D, random.nextInt(5) - 2, -5.0D), boundedMaxRadius(cfg), Math.max(3, boundedMaxRadius(cfg) - 1), boundedMaxRadius(cfg));

            carveLine(level, area, center, north, Math.max(1, boundedMinRadius(cfg)), Math.max(1, boundedMinRadius(cfg)), Math.max(1, boundedMinRadius(cfg)), AIR, replaceable);
            carveLine(level, area, center, south, Math.max(1, boundedMinRadius(cfg)), Math.max(1, boundedMinRadius(cfg)), Math.max(1, boundedMinRadius(cfg)), AIR, replaceable);

            carveEllipsoid(level, area, north, boundedMaxRadius(cfg), Math.max(3, boundedMaxRadius(cfg) - 1), boundedMaxRadius(cfg), AIR, replaceable);
            carveEllipsoid(level, area, south, boundedMaxRadius(cfg), Math.max(3, boundedMaxRadius(cfg) - 1), boundedMaxRadius(cfg), AIR, replaceable);
        }
        return true;
    }

    private boolean placeWindingCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final Vec3 start = vec(randomOffset(area, origin, random, 5, boundedVertical(cfg), boundedMaxRadius(cfg), boundedMaxRadius(cfg), boundedMaxRadius(cfg)));

        carveGuidedTunnel(
                level,
                area,
                start,
                random.nextFloat() * ((float) Math.PI * 2.0F),
                (random.nextFloat() - 0.5F) * 0.18F,
                boundedLength(cfg, 16, 26),
                Math.max(2.0D, (boundedMinRadius(cfg) + boundedMaxRadius(cfg)) * 0.5D),
                0.16F,
                0.10F,
                boundedVertical(cfg),
                AIR,
                replaceable,
                random
        );
        return true;
    }

    private boolean placeCascadeCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int chambers = Mth.nextInt(random, 4, 5);
        Vec3 previous = area.clampCenter(
                vec(origin).add(0.0D, random.nextInt(6) + 3, 0.0D),
                boundedMaxRadius(cfg),
                boundedMaxRadius(cfg),
                boundedMaxRadius(cfg)
        );

        for (int i = 0; i < chambers; i++) {
            final Vec3 current = area.clampCenter(
                    previous.add(random.nextInt(9) - 4, -5.0D - random.nextInt(4), random.nextInt(9) - 4),
                    boundedMaxRadius(cfg) + 1,
                    Math.max(3, boundedMaxRadius(cfg)),
                    boundedMaxRadius(cfg) + 1
            );

            carveEllipsoid(
                    level,
                    area,
                    current,
                    boundedMaxRadius(cfg) + 1,
                    Math.max(3, boundedMaxRadius(cfg)),
                    boundedMaxRadius(cfg) + 1,
                    AIR,
                    replaceable
            );

            carveLine(
                    level,
                    area,
                    previous,
                    current,
                    Math.max(1, boundedMinRadius(cfg)),
                    Math.max(1, boundedMinRadius(cfg)),
                    Math.max(1, boundedMinRadius(cfg)),
                    AIR,
                    replaceable
            );

            if (random.nextFloat() < 0.55F) {
                carvePocketCluster(level, area, current, random, Math.max(2, boundedMinRadius(cfg) + 1), replaceable);
            }

            if (cfg.flooded() && (i == chambers - 1 || random.nextBoolean())) {
                carveEllipsoid(
                        level,
                        area,
                        current.add(0.0D, -2.0D, 0.0D),
                        Math.max(2, boundedMaxRadius(cfg) - 2),
                        1,
                        Math.max(2, boundedMaxRadius(cfg) - 2),
                        safeFluid(cfg),
                        replaceable
                );
            }

            previous = current;
        }

        return true;
    }

    private boolean placeCollapseCaves(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int radius = Math.max(boundedMaxRadius(cfg) + 1, 5);
        final BlockPos center = randomOffset(area, origin, random, 5, 0, radius, 1, radius);

        final int topY = cfg.openToSurface()
                ? safeSurfaceY(level, area, center.getX(), center.getZ()) - 1
                : area.clampY(center.getY() + 12, radius);

        final int depth = boundedLength(cfg, 18, 30);

        for (int i = 0; i < depth; i++) {
            final double progress = i / (double) Math.max(1, depth - 1);
            final int localRadius = Math.max(2, Mth.floor(Mth.lerp(progress, radius, Math.max(2, boundedMinRadius(cfg) + 1))));
            final BlockPos slice = area.clampCenter(new BlockPos(center.getX(), topY - i, center.getZ()), localRadius, 1, localRadius);

            carveSphere(level, area, slice.getX(), slice.getY(), slice.getZ(), localRadius, 1, localRadius, AIR, replaceable);

            if (i % 7 == 0 && i > 3) {
                final Vec3 side = area.clampCenter(
                        vec(slice).add(random.nextBoolean() ? 3.0D : -3.0D, 0.0D, random.nextBoolean() ? 3.0D : -3.0D),
                        Math.max(2, localRadius - 1),
                        Math.max(2, localRadius - 1),
                        Math.max(2, localRadius - 1)
                );
                carveEllipsoid(level, area, side, Math.max(2, localRadius - 1), Math.max(2, localRadius - 1), Math.max(2, localRadius - 1), AIR, replaceable);
            }
        }

        final BlockPos bottom = area.clampCenter(
                new BlockPos(center.getX(), topY - depth, center.getZ()),
                radius,
                Math.max(4, boundedMaxRadius(cfg)),
                radius
        );

        carveEllipsoid(level, area, vec(bottom), radius, Math.max(4, boundedMaxRadius(cfg)), radius, AIR, replaceable);
        carvePocketCluster(level, area, vec(bottom), random, Math.max(2, boundedMaxRadius(cfg) - 1), replaceable);
        SpaceCarvingSupport.scatterDebris(level, bottom.below(2), Math.min(radius, 5), 10, cfg.tertiary(), random.nextLong());

        return true;
    }

    private boolean placeVerticalCollectors(
            final WorldGenLevel level,
            final BlockPos origin,
            final RandomSource random,
            final SpaceCaveFeatureConfig cfg,
            final Predicate<BlockState> replaceable,
            final LocalArea area
    ) {
        final int shafts = Mth.nextInt(random, 3, 5);
        final List<BlockPos> collectors = new ArrayList<>(shafts);

        final int topY = cfg.openToSurface()
                ? safeSurfaceY(level, area, origin.getX(), origin.getZ()) - 1
                : area.clampY(origin.getY() + 16, boundedMinRadius(cfg));

        final int depth = boundedLength(cfg, 18, 30);
        final int radius = Math.max(1, boundedMinRadius(cfg));

        for (int i = 0; i < shafts; i++) {
            final BlockPos shaft = randomOffset(area, origin, random, 6, 0, radius, 1, radius);
            collectors.add(shaft);

            for (int y = 0; y < depth; y++) {
                final BlockPos slice = area.clampCenter(new BlockPos(shaft.getX(), topY - y, shaft.getZ()), radius, 1, radius);
                carveSphere(level, area, slice.getX(), slice.getY(), slice.getZ(), radius, 1, radius, AIR, replaceable);

                if (y % 9 == 0 && y > 0) {
                    carveEllipsoid(
                            level,
                            area,
                            vec(slice),
                            radius + 1,
                            2,
                            radius + 1,
                            AIR,
                            replaceable
                    );
                }
            }
        }

        final int layers = Mth.nextInt(random, 2, 4);
        for (int layer = 0; layer < layers; layer++) {
            final int y = area.clampY(topY - 5 - layer * Math.max(4, boundedVertical(cfg) / 2), radius);

            for (int i = 1; i < collectors.size(); i++) {
                final Vec3 a = vec(new BlockPos(collectors.get(i - 1).getX(), y, collectors.get(i - 1).getZ()));
                final Vec3 b = vec(new BlockPos(collectors.get(i).getX(), y, collectors.get(i).getZ()));
                carveLine(level, area, a, b, radius + 1, radius, radius + 1, AIR, replaceable);
            }
        }

        return true;
    }

    private void carvePocketCluster(
            final WorldGenLevel level,
            final LocalArea area,
            final Vec3 center,
            final RandomSource random,
            final int baseRadius,
            final Predicate<BlockState> replaceable
    ) {
        final int pockets = Mth.nextInt(random, 2, 4);

        for (int i = 0; i < pockets; i++) {
            final int rx = Math.max(2, baseRadius - 1 + random.nextInt(2));
            final int ry = Math.max(2, baseRadius - 1 + random.nextInt(2));
            final int rz = Math.max(2, baseRadius - 1 + random.nextInt(2));

            final Vec3 pocket = area.clampCenter(
                    center.add(
                            random.nextInt(7) - 3,
                            random.nextInt(5) - 2,
                            random.nextInt(7) - 3
                    ),
                    rx,
                    ry,
                    rz
            );

            carveEllipsoid(level, area, pocket, rx, ry, rz, AIR, replaceable);

            if (random.nextBoolean()) {
                carveLine(
                        level,
                        area,
                        center,
                        pocket,
                        Math.max(1, baseRadius - 2),
                        Math.max(1, baseRadius - 2),
                        Math.max(1, baseRadius - 2),
                        AIR,
                        replaceable
                );
            }
        }
    }

    private BlockPos randomOffset(
            final LocalArea area,
            final BlockPos origin,
            final RandomSource random,
            final int horizontal,
            final int vertical,
            final int rx,
            final int ry,
            final int rz
    ) {
        return area.sampleAround(random, origin, horizontal, vertical, rx, ry, rz);
    }

    private Vec3 carveGuidedTunnel(
            final WorldGenLevel level,
            final LocalArea area,
            final Vec3 start,
            final float yawStart,
            final float pitchStart,
            final int segments,
            final double radius,
            final float yawChange,
            final float pitchChange,
            final int verticalRange,
            final BlockState fill,
            final Predicate<BlockState> replaceable,
            final RandomSource random
    ) {
        Vec3 current = area.clampCenter(start, 1, 1, 1);
        float yaw = yawStart;
        float pitch = pitchStart;

        final int maxSegments = Math.min(segments, 40);

        for (int i = 0; i < maxSegments; i++) {
            final int rx = Math.max(1, Mth.floor(radius + (random.nextFloat() - 0.5F) * 0.8F));
            final int ry = Math.max(2, rx - (random.nextBoolean() ? 0 : 1));
            final int rz = Math.max(1, Mth.floor(radius + (random.nextFloat() - 0.5F) * 0.8F));

            final Vec3 clamped = area.clampCenter(current, rx, ry, rz);
            carveEllipsoid(level, area, clamped, rx, ry, rz, fill, replaceable);

            if (i % 6 == 0 && i > 0 && random.nextFloat() < 0.35F) {
                carvePocketCluster(level, area, clamped, random, Math.max(2, rx - 1), replaceable);
            }

            final double dx = Mth.cos(yaw) * 2.6D;
            final double dz = Mth.sin(yaw) * 2.6D;
            final double dy = Mth.clamp(Mth.sin(pitch) * 1.8D, -verticalRange, verticalRange);

            current = area.pushInside(clamped, dx, dy, dz, rx, ry, rz);

            yaw += (random.nextFloat() - 0.5F) * yawChange;
            pitch += (random.nextFloat() - 0.5F) * pitchChange;

            if (area.nearHorizontalEdge(current, rx, rz)) {
                yaw += (float) ((random.nextBoolean() ? 1 : -1) * 0.45F);
            }

            pitch = Mth.clamp(pitch, -0.55F, 0.55F);
        }

        return area.clampCenter(current, 1, 1, 1);
    }

    private void carveLine(
            final WorldGenLevel level,
            final LocalArea area,
            final Vec3 start,
            final Vec3 end,
            final int rx,
            final int ry,
            final int rz,
            final BlockState fill,
            final Predicate<BlockState> replaceable
    ) {
        final int crx = area.capHorizontalRadius(rx);
        final int crz = area.capHorizontalRadius(rz);
        final int cry = area.capVerticalRadius(ry);

        final double dx = end.x - start.x;
        final double dy = end.y - start.y;
        final double dz = end.z - start.z;
        final double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

        final double stepSize = Math.max(1.20D, Math.min(crx, crz) * 0.90D);
        final int steps = Math.max(1, Mth.ceil(dist / stepSize));

        for (int i = 0; i <= steps; i++) {
            final double t = i / (double) steps;
            final Vec3 pos = area.clampCenter(start.lerp(end, t), crx, cry, crz);

            final int localRx = Math.max(1, crx - ((i % 5 == 0) ? 0 : 1));
            final int localRy = Math.max(1, cry);
            final int localRz = Math.max(1, crz - ((i % 4 == 0) ? 0 : 1));

            carveSphere(
                    level,
                    area,
                    Mth.floor(pos.x),
                    Mth.floor(pos.y),
                    Mth.floor(pos.z),
                    localRx,
                    localRy,
                    localRz,
                    fill,
                    replaceable
            );
        }
    }

    private void carveEllipsoid(
            final WorldGenLevel level,
            final LocalArea area,
            final Vec3 center,
            final int rx,
            final int ry,
            final int rz,
            final BlockState fill,
            final Predicate<BlockState> replaceable
    ) {
        final int crx = area.capHorizontalRadius(rx);
        final int cry = area.capVerticalRadius(ry);
        final int crz = area.capHorizontalRadius(rz);
        final Vec3 clamped = area.clampCenter(center, crx, cry, crz);
        carveSphere(level, area, Mth.floor(clamped.x), Mth.floor(clamped.y), Mth.floor(clamped.z), crx, cry, crz, fill, replaceable);
    }

    private void carveSphere(
            final WorldGenLevel level,
            final LocalArea area,
            final int cx,
            final int cy,
            final int cz,
            final int rx,
            final int ry,
            final int rz,
            final BlockState fill,
            final Predicate<BlockState> replaceable
    ) {
        final int crx = Math.max(1, area.capHorizontalRadius(rx));
        final int cry = Math.max(1, area.capVerticalRadius(ry));
        final int crz = Math.max(1, area.capHorizontalRadius(rz));

        final int minX = Math.max(cx - crx, area.minX);
        final int maxX = Math.min(cx + crx, area.maxX);
        final int minY = Math.max(cy - cry, area.minY);
        final int maxY = Math.min(cy + cry, area.maxY);
        final int minZ = Math.max(cz - crz, area.minZ);
        final int maxZ = Math.min(cz + crz, area.maxZ);

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            final double nx = (x - cx) / (double) crx;
            final double nx2 = nx * nx;

            for (int y = minY; y <= maxY; y++) {
                final double ny = (y - cy) / (double) cry;
                final double ny2 = ny * ny;

                for (int z = minZ; z <= maxZ; z++) {
                    final double nz = (z - cz) / (double) crz;
                    final double dist = nx2 + ny2 + nz * nz;

                    if (dist > 1.0D) {
                        continue;
                    }

                    cursor.set(x, y, z);
                    if (!canWrite(level, area, cursor)) {
                        continue;
                    }

                    final BlockState current = level.getBlockState(cursor);
                    if (!replaceable.test(current)) {
                        continue;
                    }

                    level.setBlock(cursor, fill, 2);
                }
            }
        }
    }

    private void paintShell(
            final WorldGenLevel level,
            final LocalArea area,
            final Vec3 center,
            final int rx,
            final int ry,
            final int rz,
            final BlockState shell,
            final Predicate<BlockState> replaceable
    ) {
        final int crx = Math.max(1, area.capHorizontalRadius(rx));
        final int cry = Math.max(1, area.capVerticalRadius(ry));
        final int crz = Math.max(1, area.capHorizontalRadius(rz));
        final Vec3 clamped = area.clampCenter(center, crx, cry, crz);

        final int cx = Mth.floor(clamped.x);
        final int cy = Mth.floor(clamped.y);
        final int cz = Mth.floor(clamped.z);

        final int minX = Math.max(cx - crx - 1, area.minX);
        final int maxX = Math.min(cx + crx + 1, area.maxX);
        final int minY = Math.max(cy - cry - 1, area.minY);
        final int maxY = Math.min(cy + cry + 1, area.maxY);
        final int minZ = Math.max(cz - crz - 1, area.minZ);
        final int maxZ = Math.min(cz + crz + 1, area.maxZ);

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            final double nx = (x - cx) / (double) crx;
            final double nx2 = nx * nx;

            for (int y = minY; y <= maxY; y++) {
                final double ny = (y - cy) / (double) cry;
                final double ny2 = ny * ny;

                for (int z = minZ; z <= maxZ; z++) {
                    final double nz = (z - cz) / (double) crz;
                    final double dist = nx2 + ny2 + nz * nz;

                    if (dist < 0.88D || dist > 1.18D) {
                        continue;
                    }

                    cursor.set(x, y, z);
                    if (!canWrite(level, area, cursor)) {
                        continue;
                    }

                    final BlockState current = level.getBlockState(cursor);
                    if (!replaceable.test(current)) {
                        continue;
                    }

                    level.setBlock(cursor, shell, 2);
                }
            }
        }
    }

    private void placeArchRib(
            final WorldGenLevel level,
            final LocalArea area,
            final BlockPos center,
            final int radius,
            final int height,
            final BlockState state
    ) {
        final int cr = area.capHorizontalRadius(radius);
        final int ch = area.capVerticalRadius(height);
        final BlockPos clamped = area.clampCenter(center, cr, ch, 1);

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = -cr; x <= cr; x++) {
            final double normalized = x / (double) Math.max(1, cr);
            final int y = Mth.floor((1.0D - normalized * normalized) * ch);

            for (int thickness = 0; thickness < 2; thickness++) {
                cursor.set(clamped.getX() + x, clamped.getY() + y + thickness, clamped.getZ());
                if (canWrite(level, area, cursor)) {
                    final BlockState current = level.getBlockState(cursor);
                    if (current.isAir() || current.canBeReplaced()) {
                        level.setBlock(cursor, state, 2);
                    }
                }

                cursor.set(clamped.getX() + x, clamped.getY() + y + thickness, clamped.getZ() + 1);
                if (canWrite(level, area, cursor)) {
                    final BlockState current = level.getBlockState(cursor);
                    if (current.isAir() || current.canBeReplaced()) {
                        level.setBlock(cursor, state, 2);
                    }
                }
            }
        }
    }

    private void placeColumn(
            final WorldGenLevel level,
            final LocalArea area,
            final BlockPos base,
            final int radius,
            final int height,
            final BlockState state
    ) {
        final int cr = area.capHorizontalRadius(radius);
        final int ch = area.capVerticalRadius(height);
        final BlockPos clampedBase = area.clampCenter(base, cr, 1, cr);

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int y = 0; y < ch; y++) {
            for (int x = -cr; x <= cr; x++) {
                for (int z = -cr; z <= cr; z++) {
                    cursor.set(clampedBase.getX() + x, clampedBase.getY() + y, clampedBase.getZ() + z);

                    if (!canWrite(level, area, cursor)) {
                        continue;
                    }

                    final BlockState current = level.getBlockState(cursor);
                    if (current.isAir() || current.canBeReplaced()) {
                        level.setBlock(cursor, state, 2);
                    }
                }
            }
        }
    }

    private int boundedMinRadius(final SpaceCaveFeatureConfig cfg) {
        return Math.max(1, Math.min(cfg.minRadius(), 4));
    }

    private int boundedMaxRadius(final SpaceCaveFeatureConfig cfg) {
        return Math.max(boundedMinRadius(cfg), Math.min(cfg.maxRadius(), 7));
    }

    private int boundedVertical(final SpaceCaveFeatureConfig cfg) {
        return Math.max(6, Math.min(cfg.maxVertical(), 40));
    }

    private int boundedLength(final SpaceCaveFeatureConfig cfg, final int floor, final int cap) {
        return Mth.clamp(Math.max(cfg.minLength(), cfg.maxLength()), floor, cap);
    }

    private BlockState safeFluid(final SpaceCaveFeatureConfig cfg) {
        return cfg.fluid().isAir() ? Blocks.WATER.defaultBlockState() : cfg.fluid();
    }

    private Vec3 vec(final BlockPos pos) {
        return Vec3.atCenterOf(pos);
    }

    private int safeSurfaceY(final WorldGenLevel level, final LocalArea area, final int x, final int z) {
        final int safeX = Mth.clamp(x, area.minX, area.maxX);
        final int safeZ = Mth.clamp(z, area.minZ, area.maxZ);
        return level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, safeX, safeZ);
    }

    private boolean canWrite(final WorldGenLevel level, final LocalArea area, final BlockPos pos) {
        if (pos.getY() < area.minY || pos.getY() > area.maxY) {
            return false;
        }
        if (pos.getX() < area.minX || pos.getX() > area.maxX || pos.getZ() < area.minZ || pos.getZ() > area.maxZ) {
            return false;
        }
        return level.ensureCanWrite(pos);
    }

    private record Node(int x, int y, int z) {
    }

    private record Connection(Node a, Node b) {
    }

    private static final class LocalArea {
        private final int minX;
        private final int maxX;
        private final int minZ;
        private final int maxZ;
        private final int minY;
        private final int maxY;

        private LocalArea(
                final int minX,
                final int maxX,
                final int minZ,
                final int maxZ,
                final int minY,
                final int maxY
        ) {
            this.minX = minX;
            this.maxX = maxX;
            this.minZ = minZ;
            this.maxZ = maxZ;
            this.minY = minY;
            this.maxY = maxY;
        }

        public static LocalArea from(final WorldGenLevel level, final BlockPos origin) {
            final ChunkPos chunkPos = new ChunkPos(origin);
            final int minX = chunkPos.getMinBlockX();
            final int maxX = chunkPos.getMaxBlockX();
            final int minZ = chunkPos.getMinBlockZ();
            final int maxZ = chunkPos.getMaxBlockZ();
            final int minY = level.getMinBuildHeight() + 1;
            final int maxY = level.getMaxBuildHeight() - 2;
            return new LocalArea(minX, maxX, minZ, maxZ, minY, maxY);
        }

        public int capHorizontalRadius(final int radius) {
            return Math.max(1, Math.min(radius, 7));
        }

        public int capVerticalRadius(final int radius) {
            return Math.max(1, Math.min(radius, 64));
        }

        public int clampY(final int y, final int radiusY) {
            final int ry = capVerticalRadius(radiusY);
            return Mth.clamp(y, minY + ry, maxY - ry);
        }

        public boolean nearHorizontalEdge(final Vec3 vec, final int rx, final int rz) {
            final int crx = capHorizontalRadius(rx);
            final int crz = capHorizontalRadius(rz);

            final double minSafeX = minX + crx + 1.5D;
            final double maxSafeX = maxX - crx - 1.5D;
            final double minSafeZ = minZ + crz + 1.5D;
            final double maxSafeZ = maxZ - crz - 1.5D;

            return vec.x <= minSafeX || vec.x >= maxSafeX || vec.z <= minSafeZ || vec.z >= maxSafeZ;
        }

        public BlockPos clampCenter(final BlockPos pos, final int rx, final int ry, final int rz) {
            final int crx = capHorizontalRadius(rx);
            final int cry = capVerticalRadius(ry);
            final int crz = capHorizontalRadius(rz);

            final int x = Mth.clamp(pos.getX(), minX + crx, maxX - crx);
            final int y = Mth.clamp(pos.getY(), minY + cry, maxY - cry);
            final int z = Mth.clamp(pos.getZ(), minZ + crz, maxZ - crz);
            return new BlockPos(x, y, z);
        }

        public Vec3 clampCenter(final Vec3 vec, final int rx, final int ry, final int rz) {
            final int crx = capHorizontalRadius(rx);
            final int cry = capVerticalRadius(ry);
            final int crz = capHorizontalRadius(rz);

            final double x = Mth.clamp(vec.x, minX + crx + 0.5D, maxX - crx + 0.5D);
            final double y = Mth.clamp(vec.y, minY + cry + 0.5D, maxY - cry + 0.5D);
            final double z = Mth.clamp(vec.z, minZ + crz + 0.5D, maxZ - crz + 0.5D);
            return new Vec3(x, y, z);
        }

        public BlockPos sampleAround(
                final RandomSource random,
                final BlockPos origin,
                final int horizontal,
                final int vertical,
                final int rx,
                final int ry,
                final int rz
        ) {
            final int crx = capHorizontalRadius(rx);
            final int cry = capVerticalRadius(ry);
            final int crz = capHorizontalRadius(rz);

            final int minAllowedX = Math.max(minX + crx, origin.getX() - horizontal);
            final int maxAllowedX = Math.min(maxX - crx, origin.getX() + horizontal);

            final int minAllowedY = Math.max(minY + cry, origin.getY() - Math.max(1, vertical / 2));
            final int maxAllowedY = Math.min(maxY - cry, origin.getY() + Math.max(1, vertical / 2));

            final int minAllowedZ = Math.max(minZ + crz, origin.getZ() - horizontal);
            final int maxAllowedZ = Math.min(maxZ - crz, origin.getZ() + horizontal);

            final int x = minAllowedX >= maxAllowedX ? minAllowedX : Mth.nextInt(random, minAllowedX, maxAllowedX);
            final int y = minAllowedY >= maxAllowedY ? minAllowedY : Mth.nextInt(random, minAllowedY, maxAllowedY);
            final int z = minAllowedZ >= maxAllowedZ ? minAllowedZ : Mth.nextInt(random, minAllowedZ, maxAllowedZ);

            return new BlockPos(x, y, z);
        }

        public Vec3 pushInside(
                final Vec3 current,
                final double dx,
                final double dy,
                final double dz,
                final int rx,
                final int ry,
                final int rz
        ) {
            final int crx = capHorizontalRadius(rx);
            final int cry = capVerticalRadius(ry);
            final int crz = capHorizontalRadius(rz);

            double nx = current.x + dx;
            double ny = current.y + dy;
            double nz = current.z + dz;

            final double minSafeX = minX + crx + 0.5D;
            final double maxSafeX = maxX - crx + 0.5D;
            final double minSafeY = minY + cry + 0.5D;
            final double maxSafeY = maxY - cry + 0.5D;
            final double minSafeZ = minZ + crz + 0.5D;
            final double maxSafeZ = maxZ - crz + 0.5D;

            if (nx < minSafeX) {
                nx = minSafeX + (minSafeX - nx) * 0.35D;
            } else if (nx > maxSafeX) {
                nx = maxSafeX - (nx - maxSafeX) * 0.35D;
            }

            if (nz < minSafeZ) {
                nz = minSafeZ + (minSafeZ - nz) * 0.35D;
            } else if (nz > maxSafeZ) {
                nz = maxSafeZ - (nz - maxSafeZ) * 0.35D;
            }

            ny = Mth.clamp(ny, minSafeY, maxSafeY);

            return new Vec3(nx, ny, nz);
        }
    }


}