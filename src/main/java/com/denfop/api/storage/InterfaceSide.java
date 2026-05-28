package com.denfop.api.storage;

import net.minecraft.core.Direction;

import java.util.Arrays;
import java.util.List;

public enum InterfaceSide {
    ANY(Direction.values()),
    UP(Direction.UP),
    DOWN(Direction.DOWN),
    NORTH(Direction.NORTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST),
    SOUTH(Direction.SOUTH),
    ;
    private final List<Direction> directions;

    InterfaceSide(Direction... directions) {
        this.directions = Arrays.asList(directions);
    }

    public List<Direction> getDirections() {
        return directions;
    }
}
