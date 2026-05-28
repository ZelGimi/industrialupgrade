package com.denfop.screen.space;

import com.denfop.api.space.IBody;

import java.util.Collection;

public final class SpacePickingHelper {

    private SpacePickingHelper() {
    }

    public static IBody pick(int mouseX, int mouseY, Collection<SpaceProjectedBody> projectedBodies) {
        IBody best = null;
        float bestScore = Float.MAX_VALUE;

        for (SpaceProjectedBody projected : projectedBodies) {
            if (!projected.isVisible()) {
                continue;
            }

            float dx = mouseX - projected.getScreenX();
            float dy = mouseY - projected.getScreenY();
            float distSq = dx * dx + dy * dy;
            float r = projected.getRadius();

            if (distSq <= r * r) {
                float score = distSq + projected.getDepth() * 0.015f;
                if (score < bestScore) {
                    bestScore = score;
                    best = projected.getBody();
                }
            }
        }

        return best;
    }
}