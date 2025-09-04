package com.denfop.blockentity.transport.tiles.coolpipe;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityCoolPipes;
import com.denfop.blockentity.transport.types.CoolType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCoolPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCool1Pipe extends BlockEntityCoolPipes {
    public BlockEntityCool1Pipe(BlockPos pos, BlockState state) {
        super(CoolType.cool1, BlockCoolPipeEntity.cool1, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCoolPipeEntity.cool1;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.coolpipes.getBlock(getTeBlock().getId());
    }
}
