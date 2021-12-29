package com.denfop.render;

public class Vertex {

    public float x;
    public float y;
    public float z;

    public Vertex(float x, float y) {
        this(x, y, 0.0F);
    }

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
