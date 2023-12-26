package com.denfop.tiles.reactors.graphite.controller;

public enum EnumGraphiteReactors {
    S(4,2,500,750,100000),
    A(6,3,950,1400,150000),
    I(7,4,1500,2300,300000),
    P(9,4,2500,3500,600000);
    private final int width;
    private final int height;
    private final int maxStable;
    private final int MaxHeat;
    private final double rad;

    EnumGraphiteReactors(int width, int height, int maxStable, int MaxHeat, double rad){
        this.width=width;
        this.height=height;
        this.maxStable=maxStable;
        this.MaxHeat=MaxHeat;
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
