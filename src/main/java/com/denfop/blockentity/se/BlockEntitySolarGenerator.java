package com.denfop.blockentity.se;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntitySolarGeneratorEnergy;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarEnergyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySolarGenerator extends BlockEntitySolarGeneratorEnergy {


    public BlockEntitySolarGenerator(BlockPos pos, BlockState state) {

        super(1, BlockSolarEnergyEntity.se_gen, pos, state);

    }

    public MultiBlockEntity getTeBlock() {
        return BlockSolarEnergyEntity.se_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockSE.getBlock();
    }


}
