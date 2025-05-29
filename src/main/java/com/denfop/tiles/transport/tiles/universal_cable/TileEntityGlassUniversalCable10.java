package com.denfop.tiles.transport.tiles.universal_cable;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockUniversalCable;
import com.denfop.tiles.transport.tiles.TileEntityUniversalCable;
import com.denfop.tiles.transport.types.UniversalType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityGlassUniversalCable10 extends TileEntityUniversalCable {
    public TileEntityGlassUniversalCable10(BlockPos pos, BlockState state) {
        super(UniversalType.glass10, BlockUniversalCable.universal10, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockUniversalCable.universal10;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.universal_cable.getBlock(getTeBlock().getId());
    }
}
