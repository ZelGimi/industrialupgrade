package com.denfop.blockentity.transport.tiles.exp;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityExpPipes;
import com.denfop.blockentity.transport.types.ExpType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockExpCableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityExpCable extends BlockEntityExpPipes {
    public BlockEntityExpCable(BlockPos pos, BlockState state) {
        super(ExpType.expcable, BlockExpCableEntity.experience, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockExpCableEntity.experience;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.expcable.getBlock(getTeBlock().getId());
    }
}
