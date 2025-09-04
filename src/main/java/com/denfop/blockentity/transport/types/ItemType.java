package com.denfop.blockentity.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum ItemType implements ISubEnum, ICableItem {
    itemcable(0.375F, true, false, true, 16384),
    itemcable1(0.375F, false, true, true, 16384),
    itemcable2(0.375F, true, false, false, 5000),
    itemcable3(0.375F, false, true, false, 5000),
    itemcable4(0.375F, false, false, true, 16384),
    itemcable5(0.375F, false, false, false, 5000),
    itemcable6(0.375F, true, false, true, 1),
    itemcable7(0.375F, false, true, true, 1),
    itemcable8(0.375F, false, false, true, 1),
    itemcable9(0.375F, true, false, true, 2),
    itemcable10(0.375F, false, true, true, 2),
    itemcable11(0.375F, false, false, true, 2),
    itemcable12(0.375F, true, false, true, 8),
    itemcable13(0.375F, false, true, true, 8),
    itemcable14(0.375F, false, false, true, 8),
    itemcable15(0.375F, true, false, true, 16),
    itemcable16(0.375F, false, true, true, 16),
    itemcable17(0.375F, false, false, true, 16),

    itemcable18(0.375F, true, false, false, 10),
    itemcable19(0.375F, false, true, false, 10),
    itemcable20(0.375F, false, false, false, 10),
    itemcable21(0.375F, true, false, false, 25),
    itemcable22(0.375F, false, true, false, 25),
    itemcable23(0.375F, false, false, false, 25),
    itemcable24(0.375F, true, false, false, 75),
    itemcable25(0.375F, false, true, false, 75),
    itemcable26(0.375F, false, false, false, 75),
    itemcable27(0.375F, true, false, false, 200),
    itemcable28(0.375F, false, true, false, 200),
    itemcable29(0.375F, false, false, false, 200),
    ;

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
    private final boolean isInput;
    private final int max;

    ItemType(float thickness, boolean isOutput, boolean isInput, boolean isItem, int max) {

        this.thickness = thickness;
        this.isOutput = isOutput;
        this.isItem = isItem;
        this.texture = new ResourceLocation(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
        this.isInput = isInput;
        this.max = max;
    }

    public static ItemType get(String name) {
        return nameMap.get(name);
    }

    public int getMax() {
        return max;
    }

    public String getName() {
        return this.name() + "_pipes";
    }

    public boolean isItem() {
        return isItem;
    }

    public boolean isInput() {
        return isInput;
    }

    public boolean isOutput() {
        return isOutput;
    }

    public int getId() {
        return this.ordinal();
    }

    @Override
    public float getThickness() {
        return thickness;
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
