package com.denfop.api.widget;

public enum EnumTypeSlot {
    BUCKET(175, 175, 18, 18),
    LIST(175, 191, 18, 18),
    COLOR(122, 72, 18, 18),
    ROTOR(191 - 16, 223, 18, 18),
    ROTOR_UPGRADE(223, 239, 18, 17),
    BLOCKS(207, 174, 18, 18),
    ROD_PART(191, 175, 18, 18),
    ROD_PART1(191, 191, 18, 18),
    QUARRY(191, 207, 18, 18),
    QUARRY1(191, 223, 18, 18),
    WATER_ROD_PART(191, 239, 18, 17),
    WATER_ROTOR(207, 191, 18, 18),
    UPGRADE(207, 207, 18, 18),
    WIRELESS(207, 239, 18, 17),
    PRIVATE(207, 223, 18, 18),
    CATHODE(224, 175, 17, 17),
    ANODE(224, 191, 17, 18),
    MATTER(207 + 19, 207, 18, 18),
    SOLARIUM(207 + 19, 223, 18, 18),
    EXP_MODULE(239, 175, 16, 16),
    FISHING_ROD(239, 191, 16, 16),
    PLASM(239, 223, 16, 16),
    RECIPE_SCHEDULE(0, 0, 16, 16, true),
    FERTILIZER(16, 0, 16, 16, true),
    BATTERY(33, 0, 16, 16, true),
    CROP(48, 0, 16, 16, true),

    TUBE(48 + 16, 0, 16, 16, true),
    BIT(48 + 32, 0, 16, 16, true),
    EXCHANGE(48 + 48, 0, 16, 16, true),
    CAPACITOR(96 + 16, 0, 16, 16, true),
    ;
    final boolean next;
    private final int x;
    private final int y;
    private final int weight;
    private final int height;

    EnumTypeSlot(int x, int y, int weight, int height) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.height = height;
        this.next = false;
    }

    EnumTypeSlot(int x, int y, int weight, int height, boolean next) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.height = height;
        this.next = next;
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
