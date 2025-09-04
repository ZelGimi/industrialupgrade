package com.denfop.blockentity.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarryEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEarthTransport extends BlockEntityMultiBlockElement implements ITransport {


    public BlockEntityEarthTransport(BlockPos pos, BlockState state) {
        super(BlockEarthQuarryEntity.earth_transport, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockEarthQuarryEntity.earth_transport;
    }

}
