package com.denfop.invslot;

import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import ic2.core.block.invslot.InvSlot;

public class InvSlotItemPipes extends InvSlot {

    public InvSlotItemPipes(TileEntityItemPipes tileEntityItemPipes) {
        super(tileEntityItemPipes, "slots", Access.I, 9 * 6);
    }

}
