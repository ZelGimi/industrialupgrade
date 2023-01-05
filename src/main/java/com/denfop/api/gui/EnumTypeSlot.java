package com.denfop.api.gui;

public enum EnumTypeSlot {
    BUCKET(122, 38, 18, 18),
    LIST(122, 55, 18, 18),
    COLOR(122, 72, 18, 18),
    ROTOR(122, 90, 18, 18),
    ROTOR_UPGRADE(122, 107, 18, 18),
    BLOCKS(122, 125, 18, 18),
    ROD_PART(123, 143, 18, 18),
    ROD_PART1(123, 161, 18, 18),
    QUARRY(123, 181, 18, 18),
    QUARRY1(123, 199, 18, 18),
    WATER_ROD_PART(123, 215, 18, 17),
    WATER_ROTOR(123, 216, 18, 18),
    ;
    private final int x;
    private final int y;
    private final int weight;
    private final int height;

    EnumTypeSlot(int x, int y, int weight, int height) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.height = height;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }
}
