package com.denfop.api.collision;

import com.denfop.blockentity.base.BlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MultiCellCollisionShapeHelper {

    private static final double EPS = 1.0E-6D;
    private static final double MIN_PART_SIZE = 1.0E-4D;

    private MultiCellCollisionShapeHelper() {
    }

    public static VoxelShape buildClippedShapeForCell(BlockEntityBase master, BlockPos masterPos, BlockPos cellPos, boolean forCollision) {
        List<AABB> aabbs = master.getAabbs(forCollision);
        if (aabbs == null || aabbs.isEmpty()) {
            return Shapes.empty();
        }

        int dx = cellPos.getX() - masterPos.getX();
        int dy = cellPos.getY() - masterPos.getY();
        int dz = cellPos.getZ() - masterPos.getZ();

        VoxelShape result = Shapes.empty();

        for (AABB box : aabbs) {
            if (box == null) {
                continue;
            }

            double minX = Math.max(box.minX, dx);
            double minY = Math.max(box.minY, dy);
            double minZ = Math.max(box.minZ, dz);

            double maxX = Math.min(box.maxX, dx + 1.0D);
            double maxY = Math.min(box.maxY, dy + 1.0D);
            double maxZ = Math.min(box.maxZ, dz + 1.0D);

            if (maxX - minX <= MIN_PART_SIZE || maxY - minY <= MIN_PART_SIZE || maxZ - minZ <= MIN_PART_SIZE) {
                continue;
            }

            double localMinX = snap(minX - dx);
            double localMinY = snap(minY - dy);
            double localMinZ = snap(minZ - dz);
            double localMaxX = snap(maxX - dx);
            double localMaxY = snap(maxY - dy);
            double localMaxZ = snap(maxZ - dz);

            if (localMaxX - localMinX <= MIN_PART_SIZE || localMaxY - localMinY <= MIN_PART_SIZE || localMaxZ - localMinZ <= MIN_PART_SIZE) {
                continue;
            }

            result = Shapes.or(result, Shapes.box(
                    localMinX,
                    localMinY,
                    localMinZ,
                    localMaxX,
                    localMaxY,
                    localMaxZ
            ));
        }

        return result.optimize();
    }

    public static Set<BlockPos> collectCoveredCells(BlockEntityBase master, BlockPos masterPos, boolean forCollision) {
        List<AABB> aabbs = master.getAabbs(forCollision);
        Set<BlockPos> result = new HashSet<>();

        if (aabbs == null || aabbs.isEmpty()) {
            return result;
        }

        for (AABB box : aabbs) {
            if (box == null) {
                continue;
            }

            int minX = floorCell(box.minX);
            int minY = floorCell(box.minY);
            int minZ = floorCell(box.minZ);

            int maxX = ceilCell(box.maxX);
            int maxY = ceilCell(box.maxY);
            int maxZ = ceilCell(box.maxZ);

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        BlockPos cell = masterPos.offset(x, y, z);
                        VoxelShape clipped = buildClippedShapeForCell(master, masterPos, cell, forCollision);
                        if (!clipped.isEmpty()) {
                            result.add(cell);
                        }
                    }
                }
            }
        }

        result.remove(masterPos);
        return result;
    }

    private static double snap(double value) {
        if (Math.abs(value) < EPS) {
            return 0.0D;
        }
        if (Math.abs(value - 1.0D) < EPS) {
            return 1.0D;
        }
        return value;
    }

    private static int floorCell(double value) {
        return (int) Math.floor(value);
    }

    private static int ceilCell(double value) {
        return (int) Math.floor(value - EPS);
    }
}