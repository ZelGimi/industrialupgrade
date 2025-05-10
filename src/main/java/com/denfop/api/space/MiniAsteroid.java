package com.denfop.api.space;

public class MiniAsteroid {

    private final float size;
    private final float rotationSpeed;
    private float x;
    private float y;
    private float z;

    public MiniAsteroid(float size, float x, float y, float z, float rotationSpeed) {

        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotationSpeed = rotationSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public float getZ() {
        return z;
    }

    public void setZ(final float z) {
        this.z = z;
    }

    public float getSize() {
        return size;
    }

    public float getX() {
        return x;
    }

    public void setX(final float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(final float y) {
        this.y = y;
    }

}
