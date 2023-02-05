package com.denfop.invslot;

import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityBaseRedstoneGenerator;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotRedstoneGenerator extends InvSlot {

    private final TileEntityBaseRedstoneGenerator tile;

    public InvSlotRedstoneGenerator(TileEntityBaseRedstoneGenerator baseRedstoneGenerator) {
        super(baseRedstoneGenerator, "slot", Access.I, 1, InvSide.ANY);
        this.tile = baseRedstoneGenerator;
    }

    @Override
    public boolean accepts(final ItemStack stack) {
        return stack.getItem() == Items.REDSTONE || stack.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK);
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (content.isEmpty()) {
            tile.redstone_coef = 0;
        }
        if (content.getItem() == Items.REDSTONE) {
            tile.redstone_coef = 1;
        } else {
            tile.redstone_coef = 9;
        }

    }

}
