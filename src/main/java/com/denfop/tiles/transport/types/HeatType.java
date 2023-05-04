package com.denfop.tiles.transport.types;

import com.denfop.blocks.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum HeatType implements IIdProvider {
    pipes(0, 0, 0.25F, 0.02D, 1000),
    pipes1(0, 0, 0.25F, 0.02D, 2000),
    pipes2(0, 0, 0.25F, 0.2D, 4000),
    pipes3(0, 0, 0.25F, 0.2D, 8000),
    pipes4(0, 0, 0.25F, 0.2D, 16000),

    ;

    public static final HeatType[] values = values();
    private static final Map<String, HeatType> nameMap = new HashMap<>();

    static {

        for (HeatType type : values) {
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

    public String getName() {
        return this.name() + "_pipes";
    }


    public int getId() {
        return this.ordinal();
    }
}
