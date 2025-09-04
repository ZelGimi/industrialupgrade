package com.denfop.blockentity.transport.tiles.coolpipe;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityCoolPipes;
import com.denfop.blockentity.transport.types.CoolType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCoolPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCool4Pipe extends BlockEntityCoolPipes {
    public BlockEntityCool4Pipe(BlockPos pos, BlockState state) {
        super(CoolType.cool4, BlockCoolPipeEntity.cool4, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCoolPipeEntity.cool4;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.coolpipes.getBlock(getTeBlock().getId());
    }
}
