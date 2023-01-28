package com.denfop.invslot;

import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityBaseRedstoneGenerator;
import ic2.core.block.invslot.InvSlot;

public class InvSlotRedstoneGenerator extends InvSlot {

    private final TileEntityBaseRedstoneGenerator tile;

    public InvSlotRedstoneGenerator(TileEntityBaseRedstoneGenerator baseRedstoneGenerator) {
        super(baseRedstoneGenerator, "slot", Access.I, 1, InvSide.ANY);
        this.tile = baseRedstoneGenerator;
    }


}
