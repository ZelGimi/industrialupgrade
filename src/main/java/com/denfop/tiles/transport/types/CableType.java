package com.denfop.tiles.transport.types;

import com.denfop.blocks.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum CableType implements IIdProvider {
    glass(0.25F, 0.06D, 32768),
    glass1(0.25F, 0.07D, 131072),
    glass2(0.25F, 0.08D, 524288),
    glass3(0.25F, 0.09D, 2097152),
    glass4(0.25F, 0.1D, 8388608),
    glass5(0.25F, 0.11D, 33554432),
    glass6(0.25F, 0.12D, 134217728),
    glass7(0.25F, 0.15D, 536870912),
    glass8(0.25F, 0.18D, 8589934590D),
    glass9(0.25F, 0.2D, 439804653000D),
    glass10(0.25F, 0.25D, 1759218610000D),
    copper(0.25F, 0.015D, 128),
    copper1(0.25F, 0.015D, 128, 1),
    glass_1(0.25F, 0.05D, 8192, 0),
    gold(0.25F, 0.02D, 512),
    gold1(0.25F, 0.02D, 512, 1),
    gold2(0.25F, 0.02D, 512, 2),
    iron(0.25F, 0.04D, 2048),
    iron1(0.25F, 0.04D, 2048, 1),
    iron2(0.25F, 0.04D, 2048, 2),
    iron3(0.25F, 0.04D, 2048, 3),
    tin(0.25F, 0.02D, 32),
    tin1(0.25F, 0.02D, 32, 1),
    ;

    public static final CableType[] values = values();
    private static final Map<String, CableType> nameMap = new HashMap<>();

    static {

        for (CableType type : values) {
            nameMap.put(type.getName(), type);
        }

    }


    public final float thickness;
    public final double loss;
    public final double capacity;
    public final int insulation;

    CableType(float thickness, double loss, double capacity) {

        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
        this.insulation = 0;
    }

    CableType(float thickness, double loss, double capacity, int insulation) {

        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
        this.insulation = insulation;
    }

    public static CableType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_cable";
    }


    public int getId() {
        return this.ordinal();
    }
}
