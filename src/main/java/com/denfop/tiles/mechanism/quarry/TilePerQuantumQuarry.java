package com.denfop.tiles.mechanism.quarry;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TilePerQuantumQuarry extends TileBaseQuantumQuarry {

    public TilePerQuantumQuarry(BlockPos pos, BlockState blockState) {
        super(1, BlockBaseMachine.per_quantum_quarry, pos, blockState);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.per_quantum_quarry;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
}
