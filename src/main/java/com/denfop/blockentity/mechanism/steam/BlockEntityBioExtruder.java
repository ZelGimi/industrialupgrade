package com.denfop.blockentity.mechanism.steam;

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

public class BlockEntityBioExtruder extends BlockEntityBioMultiMachine {


    public BlockEntityBioExtruder(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.Extruding.usagePerTick,
                EnumMultiMachine.Extruding.lenghtOperation,
                4, BlockBaseMachine3Entity.bio_extruder, pos, state
        );
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.bio_extruder;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Extruding;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }


}
