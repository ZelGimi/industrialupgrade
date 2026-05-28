package com.denfop.screen.space;

import com.denfop.api.space.IBody;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SpaceBodyNode {

    private final IBody body;
    private final Type type;
    private final SpaceBodyNode parent;
    private final Vec3 worldPos;
    private final float visualRadius;
    private final ResourceLocation texture;
    private final boolean hasRing;
    private final boolean horizontalRing;
    private final float selfRotation;
    private final float orbitVisualRadius;
    private final int orbitColor;
    public SpaceBodyNode(IBody body,
                         Type type,
                         SpaceBodyNode parent,
                         Vec3 worldPos,
                         float visualRadius,
                         ResourceLocation texture,
                         boolean hasRing,
                         boolean horizontalRing,
                         float selfRotation,
                         float orbitVisualRadius,
                         int orbitColor) {
        this.body = body;
        this.type = type;
        this.parent = parent;
        this.worldPos = worldPos;
        this.visualRadius = visualRadius;
        this.texture = texture;
        this.hasRing = hasRing;
        this.horizontalRing = horizontalRing;
        this.selfRotation = selfRotation;
        this.orbitVisualRadius = orbitVisualRadius;
        this.orbitColor = orbitColor;
    }

    public IBody getBody() {
        return body;
    }

    public Type getType() {
        return type;
    }

    public SpaceBodyNode getParent() {
        return parent;
    }

    public Vec3 getWorldPos() {
        return worldPos;
    }

    public float getVisualRadius() {
        return visualRadius;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public boolean hasRing() {
        return hasRing;
    }

    public boolean isHorizontalRing() {
        return horizontalRing;
    }

    public float getSelfRotation() {
        return selfRotation;
    }

    public float getOrbitVisualRadius() {
        return orbitVisualRadius;
    }

    public int getOrbitColor() {
        return orbitColor;
    }

    public enum Type {
        STAR,
        PLANET,
        SATELLITE,
        ASTEROID
    }
}