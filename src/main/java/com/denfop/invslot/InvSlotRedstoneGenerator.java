package com.denfop.invslot;

import com.denfop.tiles.mechanism.generator.energy.redstone.TileBaseRedstoneGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;


public class InvSlotRedstoneGenerator extends InvSlot {

    private final TileBaseRedstoneGenerator tile;

    public InvSlotRedstoneGenerator(TileBaseRedstoneGenerator baseRedstoneGenerator) {
        super(baseRedstoneGenerator, TypeItemSlot.INPUT, 1);
        this.tile = baseRedstoneGenerator;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() == Items.REDSTONE || stack.getItem() == Blocks.REDSTONE_BLOCK.asItem();
    }



}
