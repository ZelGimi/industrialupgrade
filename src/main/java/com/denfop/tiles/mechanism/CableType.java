package com.denfop.tiles.mechanism;

import ic2.core.block.state.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum CableType implements IIdProvider {
    glass(0, 0, 0.25F, 0.2D, 32768),
    glass1(0, 0, 0.25F, 0.2D, 131072),
    glass2(0, 0, 0.25F, 0.2D, 524288),
    glass3(0, 0, 0.25F, 0.2D, 2097152),
    glass4(0, 0, 0.25F, 0.2D, 8388608),
    glass5(0, 0, 0.25F, 0.2D, 33554432),
    glass6(0, 0, 0.25F, 0.2D, 134217728),
    glass7(0, 0, 0.25F, 0.2D, 536870912),
    glass8(0, 0, 0.25F, 0.2D, 8589934590D),
    glass9(0, 0, 0.25F, 0.2D, 439804653000D),
    glass10(0, 0, 0.25F, 0.2D, 1759218610000D),

    ;

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    public static final CableType[] values = values();
    private static final Map<String, CableType> nameMap = new HashMap();

    CableType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
    }

    public String getName(int insulation) {
        return this.getName() + "_cable";
    }

    public String getName() {
        return this.name();
    }

    public int getId() {
        return this.ordinal();
    }

    public static CableType get(String name) {
        return nameMap.get(name);
    }

    static {
        CableType[] var0 = values;

        for (CableType type : var0) {
            nameMap.put(type.getName(), type);
        }

    }
}
