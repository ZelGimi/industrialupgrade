package com.denfop.tiles.base;


import com.denfop.invslot.Inventory;

public class InfoInvSlots {

    private final Inventory slot;
    private final int index;

    public InfoInvSlots(Inventory slot, int index) {
        this.slot = slot;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Inventory getSlot() {
        return slot;
    }

}
