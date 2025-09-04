package com.denfop.blockentity.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.generator.energy.BlockEntityGeoGenerator;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhoGeoGenerator extends BlockEntityGeoGenerator {


    public BlockEntityPhoGeoGenerator(BlockPos pos, BlockState state) {
        super(64, 6, 8, BlocksPhotonicMachine.photonic_geogenerator, pos, state);

    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_geogenerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
