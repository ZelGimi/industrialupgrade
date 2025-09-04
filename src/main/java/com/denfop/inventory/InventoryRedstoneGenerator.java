package com.denfop.inventory;

import com.denfop.blockentity.mechanism.generator.energy.redstone.BlockEntityBaseRedstoneGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;


public class InventoryRedstoneGenerator extends Inventory {

    private final BlockEntityBaseRedstoneGenerator tile;

    public InventoryRedstoneGenerator(BlockEntityBaseRedstoneGenerator baseRedstoneGenerator) {
        super(baseRedstoneGenerator, TypeItemSlot.INPUT, 1);
        this.tile = baseRedstoneGenerator;
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        return stack.getItem() == Items.REDSTONE || stack.getItem() == Blocks.REDSTONE_BLOCK.asItem();
    }


}
