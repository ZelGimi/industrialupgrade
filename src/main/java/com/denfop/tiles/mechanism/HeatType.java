package com.denfop.tiles.mechanism;

import ic2.core.block.state.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum HeatType implements IIdProvider {
    pipes(0, 0, 0.25F, 0.02D, 4),
    pipes1(0, 0, 0.25F, 0.02D, 8),
    pipes2(0, 0, 0.25F, 0.2D, 16),
    pipes3(0, 0, 0.25F, 0.2D, 32),
    pipes4(0, 0, 0.25F, 0.2D, 64),

    ;

    public static final HeatType[] values = values();
    private static final Map<String, HeatType> nameMap = new HashMap();

    static {
        HeatType[] var0 = values;

        for (HeatType type : var0) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;

    HeatType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
    }

    public static HeatType get(String name) {
        return nameMap.get(name);
    }

    public String getName(int insulation) {
        return this.getName() + "_pipes";
    }

    public String getName() {
        return this.name();
    }

    public int getId() {
        return this.ordinal();
    }
}
