package com.denfop.blockentity.reactors.heat.controller;

public enum EnumHeatReactors {
    S(4, 4, 700, 1250, 100000),
    A(5, 5, 1500, 1900, 150000),
    I(6, 6, 2100, 2450, 300000),
    P(7, 7, 2800, 3500, 600000);
    private final int width;
    private final int height;
    private final int maxStable;
    private final int MaxHeat;
    private final double rad;

    EnumHeatReactors(int width, int height, int maxStable, int MaxHeat, double rad) {
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
