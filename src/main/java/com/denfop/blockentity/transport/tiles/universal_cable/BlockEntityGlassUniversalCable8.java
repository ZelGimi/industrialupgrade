package com.denfop.blockentity.transport.tiles.universal_cable;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityUniversalCable;
import com.denfop.blockentity.transport.types.UniversalType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockUniversalCableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGlassUniversalCable8 extends BlockEntityUniversalCable {
    public BlockEntityGlassUniversalCable8(BlockPos pos, BlockState state) {
        super(UniversalType.glass8, BlockUniversalCableEntity.universal8, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockUniversalCableEntity.universal8;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.universal_cable.getBlock(getTeBlock().getId());
    }
}
