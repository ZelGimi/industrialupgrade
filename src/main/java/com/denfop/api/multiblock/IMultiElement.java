package com.denfop.api.multiblock;

public interface IMultiElement {

    IMainMultiBlock getMain();

    void setMainMultiElement(IMainMultiBlock main);

    default boolean isMain() {
        return false;
    }

    default int getBlockLevel() {
        return 0;
    }

    ;

    default boolean canCreateSystem(IMainMultiBlock mainMultiBlock) {
        return true;
    }

    default boolean hasOwnInventory() {
        return false;
    }

}
