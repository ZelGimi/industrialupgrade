package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TilePhotonicDestiller extends TileEntityBaseSolarDestiller {

    public TilePhotonicDestiller(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.PHOTONIC, BlocksPhotonicMachine.photonic_destiller, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_destiller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
