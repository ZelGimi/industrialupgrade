package com.denfop.api.pollution.analyzer;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PollutionAnalyzerWorldPreviewCache {

    public static final int RADIUS = 1;
    public static final int CHUNK_SIZE = 16;
    public static final int MIN_BODY_THICKNESS = 16;
    public static final int MAX_UPPER_BLOCKS = 16;
    public static final int REBUILD_INTERVAL_TICKS = 40;
    public static final float HEIGHT_DIVISOR = 3.2F;
    public static final int FACE_DOWN = 1;
    public static final int FACE_UP = 1 << 1;
    public static final int FACE_NORTH = 1 << 2;
    public static final int FACE_SOUTH = 1 << 3;
    public static final int FACE_WEST = 1 << 4;
    public static final int FACE_EAST = 1 << 5;
    public static final int FACE_ALL = FACE_DOWN | FACE_UP | FACE_NORTH | FACE_SOUTH | FACE_WEST | FACE_EAST;
    private long lastBuildTick = Long.MIN_VALUE;
    private long centerKey = Long.MIN_VALUE;
    private String dimensionKey = "";

    private int globalBaseY = 64;
    private int globalMaxVisibleY = 80;

    private List<PreviewChunk> chunks = Collections.emptyList();

    public void update(ClientLevel level, ChunkPos center) {
        if (level == null) {
            this.chunks = Collections.emptyList();
            this.globalBaseY = 64;
            this.globalMaxVisibleY = 80;
            this.lastBuildTick = Long.MIN_VALUE;
            this.centerKey = Long.MIN_VALUE;
            this.dimensionKey = "";
            return;
        }

        String dim = level.dimension().location().toString();
        long newCenterKey = center.toLong();

        boolean needRebuild =
                !dim.equals(this.dimensionKey)
                        || this.centerKey != newCenterKey
                        || this.lastBuildTick == Long.MIN_VALUE
                        || (level.getGameTime() - this.lastBuildTick) >= REBUILD_INTERVAL_TICKS;

        if (!needRebuild) {
            return;
        }

        rebuild(level, center);
        this.dimensionKey = dim;
        this.centerKey = newCenterKey;
        this.lastBuildTick = level.getGameTime();
    }

    public List<PreviewChunk> getChunks() {
        return this.chunks;
    }

    public int getGlobalBaseY() {
        return this.globalBaseY;
    }

    public int getGlobalMaxVisibleY() {
        return this.globalMaxVisibleY;
    }

    private void rebuild(ClientLevel level, ChunkPos center) {
        List<TempChunkData> tempChunks = new ArrayList<>();

        int minBase = Integer.MAX_VALUE;
        int maxVisible = Integer.MIN_VALUE;

        for (int dz = -RADIUS; dz <= RADIUS; dz++) {
            for (int dx = -RADIUS; dx <= RADIUS; dx++) {
                int chunkX = center.x + dx;
                int chunkZ = center.z + dz;

                if (!level.hasChunk(chunkX, chunkZ)) {
                    continue;
                }

                TempChunkData data = new TempChunkData(chunkX, chunkZ);

                for (int localZ = 0; localZ < CHUNK_SIZE; localZ++) {
                    for (int localX = 0; localX < CHUNK_SIZE; localX++) {
                        int worldX = (chunkX << 4) + localX;
                        int worldZ = (chunkZ << 4) + localZ;
                        int idx = localZ * CHUNK_SIZE + localX;

                        int terrainY = level.getHeight(Heightmap.Types.OCEAN_FLOOR, worldX, worldZ) - 1;
                        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ) - 1;

                        if (terrainY < level.getMinBuildHeight()) {
                            terrainY = surfaceY;
                        }
                        if (surfaceY < level.getMinBuildHeight()) {
                            surfaceY = terrainY;
                        }

                        data.groundY[idx] = terrainY;
                        data.topY[idx] = Math.max(surfaceY, terrainY);

                        minBase = Math.min(minBase, terrainY - (MIN_BODY_THICKNESS - 1));
                        maxVisible = Math.max(maxVisible, data.topY[idx]);
                    }
                }

                tempChunks.add(data);
            }
        }

        if (tempChunks.isEmpty()) {
            this.chunks = Collections.emptyList();
            this.globalBaseY = 64;
            this.globalMaxVisibleY = 80;
            return;
        }

        this.globalBaseY = minBase;
        this.globalMaxVisibleY = maxVisible;

        List<PreviewChunk> built = new ArrayList<>(tempChunks.size());

        for (TempChunkData data : tempChunks) {
            List<PreviewSolid> solids = new LinkedList<>();
            List<PreviewFluidCell> fluids = new LinkedList<>();

            int centerIndex = 8 + 8 * CHUNK_SIZE;
            int centerGroundY = data.groundY[centerIndex];
            int maxTopY = Integer.MIN_VALUE;

            for (int localZ = 0; localZ < CHUNK_SIZE; localZ++) {
                for (int localX = 0; localX < CHUNK_SIZE; localX++) {
                    int idx = localZ * CHUNK_SIZE + localX;

                    int groundY = data.groundY[idx];
                    int topY = data.topY[idx];

                    maxTopY = Math.max(maxTopY, topY);

                    int worldX = (data.chunkX << 4) + localX;
                    int worldZ = (data.chunkZ << 4) + localZ;

                    boolean perimeter = localX == 0 || localX == 15 || localZ == 0 || localZ == 15;

                    if (perimeter) {
                        int bodyBottom = groundY - (MIN_BODY_THICKNESS - 1);
                        for (int y = bodyBottom; y <= groundY; y++) {
                            addSolid(level, solids, worldX, y, worldZ, localX, localZ);
                        }
                    }

                    int upperBottom = Math.max(groundY - MAX_UPPER_BLOCKS + 1, this.globalBaseY);
                    for (int y = topY; y >= upperBottom; y--) {
                        addSolid(level, solids, worldX, y, worldZ, localX, localZ);
                    }

                    addVisibleFluids(
                            level,
                            fluids,
                            worldX,
                            worldZ,
                            localX,
                            localZ,
                            Math.max(groundY - MAX_UPPER_BLOCKS + 1, this.globalBaseY),
                            topY
                    );
                }
            }

            built.add(new PreviewChunk(
                    data.chunkX,
                    data.chunkZ,
                    centerGroundY,
                    maxTopY,
                    new ArrayList<>(solids),
                    new ArrayList<>(fluids)
            ));
        }

        this.chunks = built;
    }

    private void addSolid(
            ClientLevel level,
            List<PreviewSolid> solids,
            int worldX,
            int worldY,
            int worldZ,
            int localX,
            int localZ
    ) {
        if (worldY < level.getMinBuildHeight() || worldY >= level.getMaxBuildHeight()) {
            return;
        }

        BlockPos pos = new BlockPos(worldX, worldY, worldZ);
        BlockState state = level.getBlockState(pos);

        if (state.isAir()) {
            return;
        }
        if (!state.getFluidState().isEmpty()) {
            return;
        }
        if (state.getRenderShape() == RenderShape.INVISIBLE) {
            return;
        }

        int exposureMask = computeExposureMask(level, pos, state);
        if (exposureMask == 0) {
            return;
        }

        solids.add(new PreviewSolid(localX, localZ, worldY, state));
    }

    private int computeExposureMask(ClientLevel level, BlockPos pos, BlockState state) {
        if (!state.canOcclude() || !state.isSolidRender(level, pos)) {
            return FACE_ALL;
        }

        int mask = 0;

        for (Direction dir : Direction.values()) {
            BlockPos sidePos = pos.relative(dir);
            BlockState neighbor = level.getBlockState(sidePos);

            if (isFaceOpen(level, sidePos, neighbor)) {
                mask |= faceBit(dir);
            }
        }

        return mask;
    }

    private boolean isFaceOpen(ClientLevel level, BlockPos pos, BlockState state) {
        if (state.isAir()) {
            return true;
        }
        if (!state.getFluidState().isEmpty()) {
            return true;
        }
        if (state.getRenderShape() == RenderShape.INVISIBLE) {
            return true;
        }
        if (!state.canOcclude()) {
            return true;
        }
        return !state.isSolidRender(level, pos);
    }

    private int faceBit(Direction dir) {
        return switch (dir) {
            case DOWN -> FACE_DOWN;
            case UP -> FACE_UP;
            case NORTH -> FACE_NORTH;
            case SOUTH -> FACE_SOUTH;
            case WEST -> FACE_WEST;
            case EAST -> FACE_EAST;
        };
    }

    private boolean isPreviewExposed(ClientLevel level, BlockPos pos, BlockState state) {
        if (!state.canOcclude()) {
            return true;
        }

        for (Direction dir : Direction.values()) {
            BlockPos sidePos = pos.relative(dir);
            BlockState neighbor = level.getBlockState(sidePos);

            if (neighbor.isAir()) {
                return true;
            }
            if (!neighbor.getFluidState().isEmpty()) {
                return true;
            }
            if (neighbor.getRenderShape() == RenderShape.INVISIBLE) {
                return true;
            }
            if (!neighbor.canOcclude()) {
                return true;
            }
            if (!neighbor.isSolidRender(level, sidePos)) {
                return true;
            }
        }

        return false;
    }

    private void addVisibleFluids(
            ClientLevel level,
            List<PreviewFluidCell> fluids,
            int worldX,
            int worldZ,
            int localX,
            int localZ,
            int minY,
            int maxY
    ) {
        for (int y = maxY; y >= minY; y--) {
            if (y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) {
                continue;
            }

            BlockPos pos = new BlockPos(worldX, y, worldZ);
            FluidState fluidState = level.getBlockState(pos).getFluidState();
            if (fluidState.isEmpty()) {
                continue;
            }

            int tint = IClientFluidTypeExtensions.of(fluidState.getType()).getTintColor(fluidState, level, pos);
            float height = Math.max(0.10F, fluidState.getHeight(level, pos));

            fluids.add(new PreviewFluidCell(
                    localX,
                    localZ,
                    y,
                    fluidState,
                    tint,
                    height
            ));
        }
    }

    private static final class TempChunkData {
        private final int chunkX;
        private final int chunkZ;
        private final int[] groundY = new int[CHUNK_SIZE * CHUNK_SIZE];
        private final int[] topY = new int[CHUNK_SIZE * CHUNK_SIZE];

        private TempChunkData(int chunkX, int chunkZ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }
    }

    public static final class PreviewChunk {
        private final int chunkX;
        private final int chunkZ;
        private final int centerGroundY;
        private final int maxTopY;
        private final List<PreviewSolid> solids;
        private final List<PreviewFluidCell> fluids;

        public PreviewChunk(
                int chunkX,
                int chunkZ,
                int centerGroundY,
                int maxTopY,
                List<PreviewSolid> solids,
                List<PreviewFluidCell> fluids
        ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.centerGroundY = centerGroundY;
            this.maxTopY = maxTopY;
            this.solids = solids;
            this.fluids = fluids;
        }

        public int getChunkX() {
            return this.chunkX;
        }

        public int getChunkZ() {
            return this.chunkZ;
        }

        public int getCenterGroundY() {
            return this.centerGroundY;
        }

        public int getMaxTopY() {
            return this.maxTopY;
        }

        public List<PreviewSolid> getSolids() {
            return this.solids;
        }

        public List<PreviewFluidCell> getFluids() {
            return this.fluids;
        }

        public float getDisplayCenterHeight(int globalBaseY) {
            return 1.0F + Math.round((this.centerGroundY - globalBaseY) / HEIGHT_DIVISOR);
        }
    }

    public static final class PreviewSolid {
        private final int localX;
        private final int localZ;
        private final int worldY;
        private final BlockState state;

        public PreviewSolid(int localX, int localZ, int worldY, BlockState state) {
            this.localX = localX;
            this.localZ = localZ;
            this.worldY = worldY;
            this.state = state;
        }

        public float displayX() {
            return this.localX - 7.5F;
        }

        public float displayZ() {
            return this.localZ - 7.5F;
        }

        public float displayY(int globalBaseY) {
            return (this.worldY - globalBaseY) + 1.0F;
        }

        public int getWorldY() {
            return this.worldY;
        }

        public BlockState getState() {
            return this.state;
        }
    }

    public static final class PreviewFluidCell {
        private final int localX;
        private final int localZ;
        private final int worldY;
        private final FluidState fluidState;
        private final int tint;
        private final float fluidHeight;

        public PreviewFluidCell(
                int localX,
                int localZ,
                int worldY,
                FluidState fluidState,
                int tint,
                float fluidHeight
        ) {
            this.localX = localX;
            this.localZ = localZ;
            this.worldY = worldY;
            this.fluidState = fluidState;
            this.tint = tint;
            this.fluidHeight = Math.max(0.10F, fluidHeight);
        }

        public int getLocalX() {
            return this.localX;
        }

        public int getLocalZ() {
            return this.localZ;
        }

        public int getWorldY() {
            return this.worldY;
        }

        public FluidState getFluidState() {
            return this.fluidState;
        }

        public int getTint() {
            return this.tint;
        }

        public float fluidHeight() {
            return this.fluidHeight;
        }

        public float minX() {
            return this.localX - 8.0F;
        }

        public float minZ() {
            return this.localZ - 8.0F;
        }

        public float maxX() {
            return minX() + 1.0F;
        }

        public float maxZ() {
            return minZ() + 1.0F;
        }

        public float baseY(int globalBaseY) {
            return (this.worldY - globalBaseY) + 1.0F;
        }
    }
}