package com.denfop.api.reactors;

public enum EnumReactors {
    FS(3, 3, 500, 750, 100000, ITypeRector.FLUID, "WaterReactorMultiBlock"),
    FA(4, 4, 950, 1400, 150000, ITypeRector.FLUID, "AdvWaterReactorMultiBlock"),
    FI(5, 5, 1500, 2300, 300000, ITypeRector.FLUID, "ImpWaterReactorMultiBlock"),
    FP(6, 6, 2300, 3500, 600000, ITypeRector.FLUID, "PerWaterReactorMultiBlock"),
    GS(3, 4, 500, 750, 100000, ITypeRector.GAS_COOLING_FAST, "GasReactorMultiBlock"),
    GA(4, 5, 950, 1400, 150000, ITypeRector.GAS_COOLING_FAST, "AdvGasReactorMultiBlock"),
    GI(5, 6, 1500, 2300, 300000, ITypeRector.GAS_COOLING_FAST, "ImpGasReactorMultiBlock"),
    GP(7, 6, 2500, 3500, 600000, ITypeRector.GAS_COOLING_FAST, "PerGasReactorMultiBlock"),
    GRS(4, 3, 500, 750, 100000, ITypeRector.GRAPHITE_FLUID, "GraphiteReactorMultiBlock"),
    GRA(6, 4, 950, 1400, 150000, ITypeRector.GRAPHITE_FLUID, "advGraphiteReactorMultiBlock"),
    GRI(7, 5, 1500, 2300, 300000, ITypeRector.GRAPHITE_FLUID, "impGraphiteReactorMultiBlock"),
    GRP(9, 5, 2500, 3500, 600000, ITypeRector.GRAPHITE_FLUID, "perGraphiteReactorMultiBlock"),
    HS(4, 4, 700, 1250, 100000, ITypeRector.HIGH_SOLID, "HeatReactorMultiBlock"),
    HA(5, 5, 1500, 1900, 150000, ITypeRector.HIGH_SOLID, "advHeatReactorMultiBlock"),
    HI(6, 6, 2100, 2450, 300000, ITypeRector.HIGH_SOLID, "impHeatReactorMultiBlock"),
    HP(7, 7, 2800, 3500, 600000, ITypeRector.HIGH_SOLID, "perHeatReactorMultiBlock");
    private final int width;
    private final int height;
    private final int maxStable;
    private final int MaxHeat;
    private final double rad;
    private final ITypeRector type;
    private final String nameReactor;

    EnumReactors(int width, int height, int maxStable, int MaxHeat, double rad, ITypeRector typeRector, String nameReactor) {
        this.width = width;
        this.height = height;
        this.maxStable = maxStable;
        this.MaxHeat = MaxHeat;
        this.rad = rad;
        this.type = typeRector;
        this.nameReactor = nameReactor.toLowerCase();
    }

    public String getNameReactor() {
        return nameReactor;
    }

    public ITypeRector getType() {
        return type;
    }

    public double getRad() {
        return rad;
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
