package com.denfop.api.pollution.utils;

public class Vec2f {

    public final int x;
    public final int y;

    public Vec2f(float xIn, float yIn) {
        this.x = (int) xIn;
        this.y = (int) yIn;
    }

    public Vec2f(int xIn, int yIn) {
        this.x = xIn;
        this.y = yIn;
    }

    public int getZ() {
        return y;
    }

    public int getX() {
        return x;
    }
}
