package com.denfop.blockentity.reactors.water.controller;

public enum EnumFluidReactors {
    S(3, 3, 500, 750, 100000),
    A(4, 4, 950, 1400, 150000),
    I(5, 5, 1500, 2300, 300000),
    P(6, 6, 2300, 3500, 600000);
    private final int width;
    private final int height;
    private final int maxStable;
    private final int MaxHeat;
    private final double rad;

    EnumFluidReactors(int width, int height, int maxStable, int MaxHeat, double rad) {
        this.width = width;
        this.height = height;
        this.maxStable = maxStable;
        this.MaxHeat = MaxHeat;
        this.rad = rad;
    }

    public int getMaxHeat() {
        return MaxHeat;
    }

    public int getMaxStable() {
        return maxStable;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public double getRadiation() {
        return rad;
    }
}
