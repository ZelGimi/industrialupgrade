package com.denfop.blockentity.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGeothermalPumpEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGeothermalRigDrill extends BlockEntityMultiBlockElement implements IRig {

    public BlockEntityGeothermalRigDrill(BlockPos pos, BlockState state) {
        super(BlockGeothermalPumpEntity.geothermal_rig, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGeothermalPumpEntity.geothermal_rig;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump.getBlock(getTeBlock());
    }

}
