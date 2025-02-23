package com.denfop.render.fluidcell;

public class ImageSize {

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public ImageSize(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return String.format("%d/%d %dx%d", this.x, this.y, this.width, this.height);
    }

}
