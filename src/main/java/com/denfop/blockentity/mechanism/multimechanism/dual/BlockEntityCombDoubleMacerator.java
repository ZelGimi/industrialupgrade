package com.denfop.blockentity.mechanism.multimechanism.dual;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.sound.EnumSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCombDoubleMacerator extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityCombDoubleMacerator(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.COMB_DOUBLE_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_DOUBLE_MACERATOR.lenghtOperation, BlockMoreMachine1Entity.double_comb_macerator, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("comb_double_macerator_soil_pollution_amount", 0.075D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("comb_double_macerator_air_pollution_amount", 0.1D)));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine1Entity.double_comb_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_DOUBLE_MACERATOR;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.MaceratorOp.getSoundEvent();
    }

}
