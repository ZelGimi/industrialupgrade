package com.denfop.api.space;

public class MiniAsteroid {

    private final float size;
    private float x;
    private float y;
    private float z;
    private final float rotationSpeed;

    public MiniAsteroid(float size, float x, float y, float z, float rotationSpeed){

        this.size = size;
        this.x=x;
        this.y=y;
        this.z=z;
        this.rotationSpeed=rotationSpeed;
    }

    public void setY(final float y) {
        this.y = y;
    }

    public void setX(final float x) {
        this.x = x;
    }

    public void setZ(final float z) {
        this.z = z;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public float getZ() {
    return z;
}


    public float getSize() {
        return size;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
