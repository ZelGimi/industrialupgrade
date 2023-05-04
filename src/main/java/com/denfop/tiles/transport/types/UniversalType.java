package com.denfop.tiles.transport.types;

import com.denfop.blocks.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum UniversalType implements IIdProvider {
    glass(0.25F, 0.19D, 32768),
    glass1(0.25F, 0.17D, 131072),
    glass2(0.25F, 0.15D, 524288),
    glass3(0.25F, 0.12D, 2097152),
    glass4(0.25F, 0.1D, 8388608),
    glass5(0.25F, 0.08D, 33554432),
    glass6(0.25F, 0.05D, 134217728),
    glass7(0.25F, 0.01D, 536870912),
    glass8(0.25F, 0.005D, 8589934590D),
    glass9(0.25F, 0.001D, 439804653000D),
    glass10(0.25F, 0.0001D, 1759218610000D),

    ;

    public static final UniversalType[] values = values();
    private static final Map<String, UniversalType> nameMap = new HashMap<>();

    static {

        for (UniversalType type : values) {
            nameMap.put(type.getName(), type);
        }

    }


    public final float thickness;
    public final double loss;
    public final double capacity;

    UniversalType(float thickness, double loss, double capacity) {

        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
    }

    public static UniversalType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_cable";
    }


    public int getId() {
        return this.ordinal();
    }
}
