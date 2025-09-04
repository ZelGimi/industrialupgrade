package com.denfop.blockentity.crop;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Collections;
import java.util.List;

public class BlockEntityQuadMultiCrop extends BlockEntityMultiCrop {

    public BlockEntityQuadMultiCrop(BlockPos pos, BlockState state) {
        super(4, BlockBaseMachine3Entity.quad_multi_crop, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.quad_multi_crop;
    }

    public List<AABB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AABB(0.0D, 0.0D, 0.0D, 1.0D, 2D, 1.0D));

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
