package com.denfop.api.multiblock;

public interface MultiBlockElement {

    MainMultiBlock getMain();

    void setMainMultiElement(MainMultiBlock main);

    default boolean isMain() {
        return false;
    }

    default int getBlockLevel() {
        return 0;
    }

    ;

    default boolean canCreateSystem(MainMultiBlock mainMultiBlock) {
        return true;
    }

    default boolean hasOwnInventory() {
        return false;
    }

}
