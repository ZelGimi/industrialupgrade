package com.denfop.screen.cable;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record CablePreviewHitResult(CablePreviewPart part, AABB box, Vec3 anchor, double depth) {

    public Direction direction() {
        return this.part.direction();
    }
}