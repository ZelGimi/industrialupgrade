package com.denfop.api.gui;

public enum EnumTypeComponent {
    ENERGY(0, 0, 12, 1, 12, 16, EnumTypeRender.HEIGHT, 0, 1, true),
    ENERGY_WEIGHT(30, 2, 69, 2, 39, 12, EnumTypeRender.WEIGHT, 0, 0, true),
    ADVANCED(220, 0, 220, 0, 18, 18, EnumTypeRender.WEIGHT, 0, 0, false),
    IMPROVED(202, 0, 202, 0, 18, 18, EnumTypeRender.WEIGHT, 0, 0, false),
    PERFECT(184, 0, 184, 0, 18, 18, EnumTypeRender.WEIGHT, 0, 0, false),
    DEFAULT(238, 0, 238, 0, 18, 18, EnumTypeRender.WEIGHT, 0, 0, false),
    MULTI_PROCESS(223, 50, 23, 50, 16, 24, EnumTypeRender.WEIGHT, 0, 0, true),
    COLD(163, 3, 167, 3, 4, 15, EnumTypeRender.HEIGHT, 0, 0, true),
    HEAT(30, 36, 69, 36, 39, 12, EnumTypeRender.WEIGHT, 0, 0, true),

    EXP(163, 18, 167, 18, 4, 15, EnumTypeRender.HEIGHT, 0, 0, true),
    FLUID_PART(85, 46, 85, 46, 22, 16, EnumTypeRender.WEIGHT, 0, 0, false),
    WATER(170, 3, 174, 3, 4, 15, EnumTypeRender.HEIGHT, 0, 0, true),
    SOLARIUM_ENERGY_WEIGHT(30, 86, 69, 86, 39, 12, EnumTypeRender.WEIGHT, 0, 0, true),
    QUANTUM_ENERGY_WEIGHT(30, 98, 69, 98, 39, 12, EnumTypeRender.WEIGHT, 0, 0, true),
    SOUND_BUTTON(22, 243, 34, 243, 13, 13, EnumTypeRender.WEIGHT, 0, 0, true),
    PROCESS(3, 55, 3, 72, 22, 16, EnumTypeRender.WEIGHT, 0, 0, true),
    PROCESS1(223, 98, 239, 98, 16, 22, EnumTypeRender.HEIGHT, 0, 0, true),
    LEFT(85, 146, 239, 98, 9, 18, EnumTypeRender.WEIGHT, 0, 0, true),
    RIGHT(96, 146, 239, 98, 9, 18, EnumTypeRender.WEIGHT, 0, 0, true),
    RAD(178, 244, 217, 244, 39, 12, EnumTypeRender.WEIGHT, 0, 0, true),
    RAD_1(160, 145, 160, 160, 60, 15, EnumTypeRender.WEIGHT, 0, 0, true),
    ENERGY_WEIGHT_1(143,178, 143, 197, 183-143, 191-178, EnumTypeRender.WEIGHT, 0, 0, true),
    ;

    private final int x;
    private final int y;
    private final int x1;
    private final int y1;
    private final int weight;
    private final int height;
    private final EnumTypeRender render;
    private final int endX;
    private final int endY;
    private final boolean hasDescription;

    EnumTypeComponent(int x, int y, int x1, int y1, int weight, int height, EnumTypeRender render, boolean hasDescription) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.weight = weight;
        this.height = height;
        this.render = render;
        this.endX = 0;
        this.endY = 0;
        this.hasDescription = hasDescription;
    }

    EnumTypeComponent(
            int x,
            int y,
            int x1,
            int y1,
            int weight,
            int height,
            EnumTypeRender render,
            int endX,
            int endY,
            boolean hasDescription
    ) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.weight = weight;
        this.height = height;
        this.render = render;
        this.endX = endX;
        this.endY = endY;
        this.hasDescription = hasDescription;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public boolean isHasDescription() {
        return hasDescription;
    }

    public EnumTypeRender getRender() {
        return render;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getX() {
        return x;
    }

    public int getX1() {
        return x1;
    }

    public int getY() {
        return y;
    }

    public int getY1() {
        return y1;
    }
}
