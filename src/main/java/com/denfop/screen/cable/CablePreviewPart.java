package com.denfop.screen.cable;

import net.minecraft.core.Direction;

public enum CablePreviewPart {
    CENTER(null),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    EAST(Direction.EAST),
    WEST(Direction.WEST),
    UP(Direction.UP),
    DOWN(Direction.DOWN);

    private final Direction direction;

    CablePreviewPart(Direction direction) {
        this.direction = direction;
    }

    public static CablePreviewPart fromDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    public Direction direction() {
        return this.direction;
    }

    public boolean isToggleable() {
        return this.direction != null;
    }
}