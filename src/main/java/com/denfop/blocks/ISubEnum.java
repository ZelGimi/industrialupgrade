package com.denfop.blocks;


import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface ISubEnum extends StringRepresentable {


    int getId();

    default boolean register() {
        return true;
    }

    ;

    default boolean registerVariants() {
        return true;
    }

    ;

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

    default BlockBehaviour.Properties createProperties() {
        return (BlockBehaviour.Properties.of());
    }

    ;

    default boolean canAddToTab() {
        return true;
    }

    ;
}
