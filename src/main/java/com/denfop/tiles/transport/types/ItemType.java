package com.denfop.tiles.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum ItemType implements ISubEnum, ICableItem {
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
    private final ResourceLocation texture;

    ItemType(float thickness, boolean isOutput, boolean isItem) {

        this.thickness = thickness;
        this.isOutput = isOutput;
        this.isItem = isItem;
        this.texture = new ResourceLocation(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
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

    @Override
    public String getNameCable() {
        return this.name() + "_pipes";
    }

    @Override
    public String getMainPath() {
        return "item_pipes";
    }

    @Override
    public ResourceLocation getRecourse() {
        return texture;
    }
}
