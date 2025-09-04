package com.denfop.blockentity.transport.tiles.coolpipe;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityCoolPipes;
import com.denfop.blockentity.transport.types.CoolType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCoolPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCoolPipe extends BlockEntityCoolPipes {
    public BlockEntityCoolPipe(BlockPos pos, BlockState state) {
        super(CoolType.cool, BlockCoolPipeEntity.cool, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCoolPipeEntity.cool;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.coolpipes.getBlock(getTeBlock().getId());
    }
}
