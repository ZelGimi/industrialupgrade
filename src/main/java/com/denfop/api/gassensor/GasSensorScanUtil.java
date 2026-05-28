package com.denfop.api.gassensor;


import com.denfop.blocks.FluidName;
import com.denfop.world.GenData;
import com.denfop.world.WorldGenGas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayList;
import java.util.List;

public final class GasSensorScanUtil {

    private static final int VERIFY_RADIUS = 16;

    private GasSensorScanUtil() {
    }

    public static GasSensorVeinEntry scanCandidate(ServerLevel level, ServerPlayer player, ChunkPos sourceChunk, GenData data) {
        if (data == null || data.getTypeGas() == null) {
            return null;
        }

        FluidName fluidName = WorldGenGas.gasFluidMap.get(data.getTypeGas());
        if (fluidName == null || fluidName.getInstance() == null || fluidName.getInstance().get() == null) {
            return null;
        }

        Fluid fluid = fluidName.getInstance().get().getSource();
        String translationKey = fluidName.getInstance().get().getFluidType().getDescriptionId();
        String fluidRegistry = BuiltInRegistries.FLUID.getKey(fluid).toString();

        int centerX = data.getX() != 0 ? data.getX() : sourceChunk.getMinBlockX() + 8;
        int centerY = data.getY();
        int centerZ = data.getZ() != 0 ? data.getZ() : sourceChunk.getMinBlockZ() + 8;

        if (!isAreaLoaded(level, centerX, centerZ, VERIFY_RADIUS)) {
            return null;
        }

        int minY = Math.max(level.getMinBuildHeight(), centerY - VERIFY_RADIUS);
        int maxY = Math.min(level.getMaxBuildHeight() - 1, centerY + VERIFY_RADIUS);

        int amountMb = 0;
        int minRelativeX = Integer.MAX_VALUE;
        int minRelativeY = Integer.MAX_VALUE;
        int minRelativeZ = Integer.MAX_VALUE;
        int maxRelativeX = Integer.MIN_VALUE;
        int maxRelativeY = Integer.MIN_VALUE;
        int maxRelativeZ = Integer.MIN_VALUE;

        List<GasSensorVeinEntry.PreviewCell> cells = new ArrayList<>();

        for (int y = minY; y <= maxY; y++) {
            for (int z = centerZ - VERIFY_RADIUS; z <= centerZ + VERIFY_RADIUS; z++) {
                for (int x = centerX - VERIFY_RADIUS; x <= centerX + VERIFY_RADIUS; x++) {
                    int dx = x - centerX;
                    int dy = y - centerY;
                    int dz = z - centerZ;

                    if ((dx * dx) + (dy * dy) + (dz * dz) > (VERIFY_RADIUS * VERIFY_RADIUS)) {
                        continue;
                    }

                    BlockPos pos = new BlockPos(x, y, z);
                    FluidState state = level.getFluidState(pos);
                    if (state.isEmpty() || state.getType() != fluid) {
                        continue;
                    }

                    int cellAmount = state.isSource()
                            ? 1000
                            : Math.max(125, Math.round(state.getHeight(level, pos) * 1000.0F));

                    amountMb += cellAmount;

                    int rx = x - centerX;
                    int ry = y - centerY;
                    int rz = z - centerZ;

                    minRelativeX = Math.min(minRelativeX, rx);
                    minRelativeY = Math.min(minRelativeY, ry);
                    minRelativeZ = Math.min(minRelativeZ, rz);

                    maxRelativeX = Math.max(maxRelativeX, rx);
                    maxRelativeY = Math.max(maxRelativeY, ry);
                    maxRelativeZ = Math.max(maxRelativeZ, rz);

                    cells.add(new GasSensorVeinEntry.PreviewCell(rx, ry, rz, cellAmount));
                }
            }
        }

        if (amountMb <= 0 || cells.isEmpty()) {
            return null;
        }

        double distance = Math.sqrt(player.distanceToSqr(centerX + 0.5D, centerY + 0.5D, centerZ + 0.5D));

        String id = sourceChunk.x + ":" + sourceChunk.z + ":" + data.getTypeGas().name();

        return new GasSensorVeinEntry(
                id,
                translationKey,
                data.getTypeGas().name(),
                fluidRegistry,
                centerX,
                centerY,
                centerZ,
                sourceChunk.x,
                sourceChunk.z,
                amountMb,
                distance,
                minRelativeX,
                minRelativeY,
                minRelativeZ,
                maxRelativeX,
                maxRelativeY,
                maxRelativeZ,
                cells
        );
    }

    public static boolean isAreaLoaded(ServerLevel level, int centerX, int centerZ, int radiusBlocks) {
        int minChunkX = (centerX - radiusBlocks) >> 4;
        int maxChunkX = (centerX + radiusBlocks) >> 4;
        int minChunkZ = (centerZ - radiusBlocks) >> 4;
        int maxChunkZ = (centerZ + radiusBlocks) >> 4;

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                if (!level.hasChunk(chunkX, chunkZ)) {
                    return false;
                }
            }
        }
        return true;
    }
}