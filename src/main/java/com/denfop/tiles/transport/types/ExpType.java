package com.denfop.tiles.transport.types;

import ic2.core.block.state.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum ExpType implements IIdProvider {
    expcable(0, 0, 0.25F, 0.02D, Integer.MAX_VALUE);

    public static final ExpType[] values = values();
    private static final Map<String, ExpType> nameMap = new HashMap<>();

    static {

        for (ExpType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;

    ExpType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
    }

    public static ExpType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_expcable";
    }


    public int getId() {
        return this.ordinal();
    }
}
