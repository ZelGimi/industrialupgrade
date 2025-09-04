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

public class BlockEntityBioCentrifuge extends BlockEntityBioMultiMachine {


    public BlockEntityBioCentrifuge(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.Centrifuge.usagePerTick,
                EnumMultiMachine.Centrifuge.lenghtOperation,
                4, BlockBaseMachine3Entity.bio_centrifuge, pos, state
        );
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.bio_centrifuge;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Centrifuge;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }


}
