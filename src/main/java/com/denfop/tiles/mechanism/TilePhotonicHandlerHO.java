package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileBaseHandlerHeavyOre;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TilePhotonicHandlerHO extends TileBaseHandlerHeavyOre {


    public TilePhotonicHandlerHO(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.PHOTONIC, BlocksPhotonicMachine.photonic_handlerho, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_handlerho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
