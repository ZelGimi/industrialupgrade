package com.denfop.blockentity.transport.tiles.energycable;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityCable;
import com.denfop.blockentity.transport.types.CableType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityIronCable extends BlockEntityCable {
    public BlockEntityIronCable(BlockPos pos, BlockState state) {
        super(CableType.iron, BlockCableEntity.iron, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCableEntity.iron;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cable.getBlock(getTeBlock().getId());
    }
}
