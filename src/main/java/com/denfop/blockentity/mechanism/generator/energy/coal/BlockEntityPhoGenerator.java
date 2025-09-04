package com.denfop.blockentity.mechanism.generator.energy.coal;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhoGenerator extends BlockEntityAdvGenerator {


    public BlockEntityPhoGenerator(BlockPos pos, BlockState state) {
        super(6, 70000, 8, BlocksPhotonicMachine.photonic_generator, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
