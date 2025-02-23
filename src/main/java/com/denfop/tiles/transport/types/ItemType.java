package com.denfop.tiles.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum ItemType implements ISubEnum, ICableItem {
    itemcable(0.375F, true,false, true,Integer.MAX_VALUE),
    itemcable1(0.375F, false,true, true,Integer.MAX_VALUE),
    itemcable2(0.375F, true, false, false,Integer.MAX_VALUE),
    itemcable3(0.375F, false,true,  false,Integer.MAX_VALUE),
    itemcable4(0.375F, false,false, true,Integer.MAX_VALUE),
    itemcable5(0.375F, false,false, false,Integer.MAX_VALUE),
    itemcable6(0.375F, true,false, true,4),
    itemcable7(0.375F, false,true, true,4),
    itemcable8(0.375F, false,false, true,4),
    itemcable9(0.375F, true,false, true,8),
    itemcable10(0.375F, false,true, true,8),
    itemcable11(0.375F, false,false, true,8),
    itemcable12(0.375F, true,false, true,16),
    itemcable13(0.375F, false,true, true,16),
    itemcable14(0.375F, false,false, true,16),
    itemcable15(0.375F, true,false, true,32),
    itemcable16(0.375F, false,true, true,32),
    itemcable17(0.375F, false,false, true,32),

    itemcable18(0.375F, true, false, false,100),
    itemcable19(0.375F, false,true,  false,100),
    itemcable20(0.375F, false,false, false,100),
    itemcable21(0.375F, true, false, false,250),
    itemcable22(0.375F, false,true,  false,250),
    itemcable23(0.375F, false,false, false,250),
    itemcable24(0.375F, true, false, false,500),
    itemcable25(0.375F, false,true,  false,500),
    itemcable26(0.375F, false,false, false,500),
    itemcable27(0.375F, true, false, false,1000),
    itemcable28(0.375F, false,true,  false,1000),
    itemcable29(0.375F, false,false, false,1000),
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

    ItemType(float thickness, boolean isOutput,boolean isInput, boolean isItem, int max) {

        this.thickness = thickness;
        this.isOutput = isOutput;
        this.isItem = isItem;
        this.texture = new ResourceLocation(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
        this.isInput = isInput;
        this.max=max;
    }

    public int getMax() {
        return max;
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
