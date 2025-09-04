package com.denfop.blockentity.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.sound.EnumSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityDoubleCompressor extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityDoubleCompressor(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.DOUBLE_COMPRESSER.usagePerTick,
                EnumMultiMachine.DOUBLE_COMPRESSER.lenghtOperation, BlockMoreMachineEntity.double_commpressor, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachineEntity.double_commpressor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base.getBlock(getTeBlock().getId());
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_COMPRESSER;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.compressor.getSoundEvent();
    }


}
