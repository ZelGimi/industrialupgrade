package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.combpump.BlockEntityCombinedPump;
import com.denfop.blockentity.mechanism.combpump.EnumTypePump;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhoCombPump extends BlockEntityCombinedPump {

    public BlockEntityPhoCombPump(BlockPos pos, BlockState state) {
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
    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_comb_pump;
    }

}
