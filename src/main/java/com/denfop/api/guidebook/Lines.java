package com.denfop.api.guidebook;

public enum Lines {
    GOLD(48, 8,18,13),
    GRAY(48, 2,12,13),
    DARK(48, 5,15,13);

    private final int hX;
    private final int hY;
    private final int vX;
    private final int Vy;

    Lines(int hX, int hY, int vX, int Vy) {
        this.hX = hX;
        this.hY = hY;
        this.vX = vX;
        this.Vy = Vy;
    }

    public int getHX() {
        return hX;
    }

    public int getHY() {
        return hY;
    }

    public int getVY() {
        return Vy;
    }

    public int getVX() {
        return vX;
    }
}
