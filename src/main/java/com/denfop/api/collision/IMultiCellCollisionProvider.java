package com.denfop.api.collision;

public interface IMultiCellCollisionProvider {

    default boolean useMultiCellCollision() {
        return true;
    }

}