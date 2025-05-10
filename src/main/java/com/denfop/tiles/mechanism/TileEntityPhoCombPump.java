package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.combpump.EnumTypePump;
import com.denfop.tiles.mechanism.combpump.TileEntityCombinedPump;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPhoCombPump extends TileEntityCombinedPump {

    public TileEntityPhoCombPump(BlockPos pos, BlockState state) {
        super(320, 10, EnumTypePump.PH, BlocksPhotonicMachine.photonic_comb_pump, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_comb_pump;
    }

}
