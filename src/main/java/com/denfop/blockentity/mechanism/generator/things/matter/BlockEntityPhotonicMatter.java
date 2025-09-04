package com.denfop.blockentity.mechanism.generator.things.matter;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMatter;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhotonicMatter extends BlockEntityMultiMatter {


    public BlockEntityPhotonicMatter(BlockPos pos, BlockState state) {
        super(600000F, 16, 512000000, BlocksPhotonicMachine.photonic_gen_matter, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_gen_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
