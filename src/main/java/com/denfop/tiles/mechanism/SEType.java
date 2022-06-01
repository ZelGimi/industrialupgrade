package com.denfop.tiles.mechanism;

import ic2.core.block.state.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum SEType implements IIdProvider {
    scable(0, 0, 0.25F, 0.02D, Integer.MAX_VALUE);

    public static final SEType[] values = values();
    private static final Map<String, SEType> nameMap = new HashMap<>();

    static {

        for (SEType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;

    SEType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
    }

    public static SEType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_scable";
    }


    public int getId() {
        return this.ordinal();
    }
}
