package com.denfop.tiles.base;

import ic2.core.block.invslot.InvSlot;

public class InfoInvSlots {

    private final InvSlot slot;
    private final int index;

    public InfoInvSlots(InvSlot slot, int index) {
        this.slot = slot;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public InvSlot getSlot() {
        return slot;
    }

}
