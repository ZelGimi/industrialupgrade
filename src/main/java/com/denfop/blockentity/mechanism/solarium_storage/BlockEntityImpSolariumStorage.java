package com.denfop.blockentity.mechanism.solarium_storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpSolariumStorage extends BlockEntitySolariumStorage {

    public BlockEntityImpSolariumStorage(BlockPos pos, BlockState state) {
        super(1600000, EnumTypeStyle.IMPROVED, BlockBaseMachine3Entity.imp_solarium_storage, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.imp_solarium_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
