package com.denfop.api.multiblock.preview;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public record MultiblockPreviewEntry(BlockPos relativePos, ItemStack stack, Direction direction) {

    public MultiblockPreviewEntry {
        if (relativePos == null) {
            throw new IllegalArgumentException("relativePos cannot be null");
        }
        if (stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("stack cannot be null or empty");
        }
        if (direction == null) direction = Direction.NORTH;
        if (direction == Direction.WEST || direction == Direction.EAST) direction = direction.getOpposite();
    }

    public boolean isOrigin() {
        return this.relativePos.equals(BlockPos.ZERO);
    }
}