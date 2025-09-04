package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.inventory.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityItemTrash extends BlockEntityInventory {

    private final Inventory invSlot;

    public BlockEntityItemTrash(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.item_trash, pos, state);
        this.invSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 96) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                return content;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return true;
            }
        };
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.item_trash;
    }

}
