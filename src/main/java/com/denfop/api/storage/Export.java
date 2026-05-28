package com.denfop.api.storage;

public interface Export extends TypeMechanismStorage {

    TypeStack getTypeStack();


    TypeRedstone getRedstone();

    int getRedstoneSignal();

    TypeComponent getComponent();

    int getIndexFromSlot(int slot);

    TypeSlot getSlotMode();
}
