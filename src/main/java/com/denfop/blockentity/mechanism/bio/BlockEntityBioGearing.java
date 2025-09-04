package com.denfop.blockentity.mechanism.bio;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBioMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.sound.EnumSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityBioGearing extends BlockEntityBioMultiMachine {


    public BlockEntityBioGearing(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.Gearing.usagePerTick,
                EnumMultiMachine.Gearing.lenghtOperation,
                4, BlockBaseMachine3Entity.bio_gearing, pos, state
        );
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.bio_gearing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Gearing;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }


}
