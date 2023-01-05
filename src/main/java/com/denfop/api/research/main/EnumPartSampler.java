package com.denfop.api.research.main;

public enum EnumPartSampler {
    NW(59, 24, 113, 78),
    NE(127, 24, 181, 78),
    SW(59, 91, 113, 145),
    SE(127, 91, 181, 145);
    public final int startX;
    public final int startY;
    public final int endX;
    public final int endY;

    EnumPartSampler(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

}
