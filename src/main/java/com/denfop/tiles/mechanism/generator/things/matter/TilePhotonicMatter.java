package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileMultiMatter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TilePhotonicMatter extends TileMultiMatter {


    public TilePhotonicMatter(BlockPos pos, BlockState state) {
        super(600000F, 16, 512000000, BlocksPhotonicMachine.photonic_gen_matter, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_gen_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
