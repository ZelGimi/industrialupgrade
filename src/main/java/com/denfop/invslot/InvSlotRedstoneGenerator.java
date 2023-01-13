package com.denfop.invslot;

import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityBaseRedstoneGenerator;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotRedstoneGenerator extends InvSlot {

    private final TileEntityBaseRedstoneGenerator tile;

    public InvSlotRedstoneGenerator(TileEntityBaseRedstoneGenerator baseRedstoneGenerator) {
        super(baseRedstoneGenerator,"slot",Access.I,1,InvSide.ANY);
        this.tile = baseRedstoneGenerator;
    }



}
