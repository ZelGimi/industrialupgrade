package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityItemTrash extends TileEntityInventory {

    private final InvSlot invSlot;

    public TileEntityItemTrash(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.item_trash, pos, state);
        this.invSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 96) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return true;
            }
        };
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.item_trash;
    }

}
