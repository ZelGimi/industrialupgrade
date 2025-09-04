package com.denfop.api.widget;

public interface ITypeSlot {

    default EnumTypeSlot getTypeSlot() {
        return null;
    }

    default EnumTypeSlot getTypeSlot(int slotid) {
        return getTypeSlot();
    }

}
