package com.denfop.blockentity.transport.tiles.radiation;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityRadPipes;
import com.denfop.blockentity.transport.types.RadTypes;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockRadPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityRadiationPipe extends BlockEntityRadPipes {
    public BlockEntityRadiationPipe(BlockPos pos, BlockState state) {
        super(RadTypes.rad_cable, BlockRadPipeEntity.radiation, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockRadPipeEntity.radiation;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.radcable_item.getBlock(getTeBlock().getId());
    }
}
