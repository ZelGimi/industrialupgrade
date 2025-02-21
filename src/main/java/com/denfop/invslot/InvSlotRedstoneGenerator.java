package com.denfop.invslot;

import com.denfop.tiles.mechanism.generator.energy.redstone.TileBaseRedstoneGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotRedstoneGenerator extends InvSlot {

    private final TileBaseRedstoneGenerator tile;

    public InvSlotRedstoneGenerator(TileBaseRedstoneGenerator baseRedstoneGenerator) {
        super(baseRedstoneGenerator, TypeItemSlot.INPUT, 1);
        this.tile = baseRedstoneGenerator;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() == Items.REDSTONE || stack.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK);
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);


    }

}
