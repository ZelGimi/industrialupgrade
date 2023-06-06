package com.denfop.componets;

import com.denfop.tiles.base.TileEntityInventory;

public class RedstoneEmitter extends BasicRedstoneComponent {

    public RedstoneEmitter(TileEntityInventory parent) {
        super(parent);
    }

    public void onChange() {
        this.parent.getWorld().notifyNeighborsOfStateChange(this.parent.getPos(), this.parent.getBlockType(), false);
    }

}
