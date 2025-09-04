package com.denfop.api.space.colonies.enums;

public enum EnumProblems {
    OXYGEN(98, 59, 22, 20),
    WORKERS(81, 61, 17, 20),
    ENERGY(65, 60, 16, 18),
    PROTECTION(140, 61, 159 - 140, 79 - 61),
    FOOD(121, 61, 22, 18);

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    EnumProblems(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getY() {
        return y;
    }
}
