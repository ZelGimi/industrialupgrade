package com.denfop.componets;

import ic2.core.block.TileEntityBlock;

public class RedstoneEmitter extends BasicRedstoneComponent {

    public RedstoneEmitter(TileEntityBlock parent) {
        super(parent);
    }

    public void onChange() {
        this.parent.getWorld().notifyNeighborsOfStateChange(this.parent.getPos(), this.parent.getBlockType(), false);
    }

}
