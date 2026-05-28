package com.denfop.screen.space;

import com.denfop.api.space.IBody;

public class SpaceProjectedBody {

    private final IBody body;
    private final float screenX;
    private final float screenY;
    private final float depth;
    private final float radius;
    private final boolean visible;

    public SpaceProjectedBody(IBody body, float screenX, float screenY, float depth, float radius, boolean visible) {
        this.body = body;
        this.screenX = screenX;
        this.screenY = screenY;
        this.depth = depth;
        this.radius = radius;
        this.visible = visible;
    }

    public IBody getBody() {
        return body;
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public float getDepth() {
        return depth;
    }

    public float getRadius() {
        return radius;
    }

    public boolean isVisible() {
        return visible;
    }
}