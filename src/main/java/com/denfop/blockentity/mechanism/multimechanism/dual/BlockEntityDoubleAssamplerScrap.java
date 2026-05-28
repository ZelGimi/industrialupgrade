package com.denfop.blockentity.mechanism.multimechanism.dual;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.sound.EnumSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityDoubleAssamplerScrap extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityDoubleAssamplerScrap(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.DOUBLE_AssamplerScrap.usagePerTick,
                EnumMultiMachine.DOUBLE_AssamplerScrap.lenghtOperation, BlockMoreMachine3Entity.double_assamplerscrap, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("double_assampler_scrap_soil_pollution_amount", 0.075D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("double_assampler_scrap_air_pollution_amount", 0.1D)));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine3Entity.double_assamplerscrap;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_AssamplerScrap;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.AssamplerScrap.getSoundEvent();
    }


}
