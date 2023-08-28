package com.denfop.api.gui;

public interface ITypeSlot {

    default EnumTypeSlot getTypeSlot() {
        return null;
    }

    default EnumTypeSlot getTypeSlot(int slotid) {
        return getTypeSlot();
    }

}
