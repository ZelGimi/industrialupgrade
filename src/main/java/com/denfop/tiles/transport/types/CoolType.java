package com.denfop.tiles.transport.types;

import ic2.core.block.state.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum CoolType implements IIdProvider {
    cool(0, 0, 0.25F, 0.02D, 4),
    cool1(0, 0, 0.25F, 0.02D, 8),
    cool2(0, 0, 0.25F, 0.2D, 16),
    cool3(0, 0, 0.25F, 0.2D, 32),
    cool4(0, 0, 0.25F, 0.2D, 64),

    ;

    public static final CoolType[] values = values();
    private static final Map<String, CoolType> nameMap = new HashMap<>();

    static {

        for (CoolType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;

    CoolType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
    }

    public static CoolType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_pipes";
    }


    public int getId() {
        return this.ordinal();
    }
}
