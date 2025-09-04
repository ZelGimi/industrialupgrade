package com.denfop.blockentity.reactors.heat.chamber;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.heat.IChamber;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvChamber extends BlockEntityMultiBlockElement implements IChamber {

    public BlockEntityAdvChamber(BlockPos pos, BlockState state) {
        super(BlockHeatReactorEntity.heat_adv_chamber, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_adv_chamber;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 1;
    }

}
