package com.denfop.tiles.transport.types;

import com.denfop.blocks.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum QEType implements IIdProvider {
    qcable(0, 0, 0.25F, 0.02D, Integer.MAX_VALUE);

    public static final QEType[] values = values();
    private static final Map<String, QEType> nameMap = new HashMap<>();

    static {

        for (QEType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;

    QEType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
    }

    public static QEType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_qcable";
    }


    public int getId() {
        return this.ordinal();
    }
}
