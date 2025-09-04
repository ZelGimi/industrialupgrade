package com.denfop.blocks;


import net.minecraft.util.StringRepresentable;

public interface SubEnum extends StringRepresentable {


    int getId();

    default boolean register() {
        return true;
    }


    @Override
    default String getSerializedName() {
        return getName();
    }

    String getName();

    default boolean registerOnlyBlock() {
        return false;
    }

    String getMainPath();

    default String getOtherPart() {
        return "";
    }

    ;

    default boolean canAddToTab() {
        return true;
    }

    ;
}
