package com.denfop.componets;

import com.denfop.tiles.base.TileEntityInventory;

public class ComparatorEmitter extends BasicRedstoneComponent {

    public ComparatorEmitter(TileEntityInventory parent) {
        super(parent);
    }

    public void onChange() {
        this.parent.getWorld().updateComparatorOutputLevel(this.parent.getPos(), this.parent.getBlockType());
    }

}
