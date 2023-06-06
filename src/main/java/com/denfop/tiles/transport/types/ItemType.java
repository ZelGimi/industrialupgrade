package com.denfop.tiles.transport.types;

import com.denfop.blocks.IIdProvider;

import java.util.HashMap;
import java.util.Map;

public enum ItemType implements IIdProvider {
    itemcable(0.25F, true, true),
    itemcable1(0.25F, false, true),
    itemcable2(0.25F, true, false),
    itemcable3(0.25F, false, false);

    public static final ItemType[] values = values();
    private static final Map<String, ItemType> nameMap = new HashMap<>();

    static {

        for (ItemType type : values) {
            nameMap.put(type.getName(), type);
        }

    }


    public final float thickness;
    public final boolean isOutput;
    private final boolean isItem;

    ItemType(float thickness, boolean isOutput, boolean isItem) {

        this.thickness = thickness;
        this.isOutput = isOutput;
        this.isItem = isItem;
    }

    public static ItemType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_pipes";
    }

    public boolean isItem() {
        return isItem;
    }

    public boolean isOutput() {
        return isOutput;
    }

    public int getId() {
        return this.ordinal();
    }
}
