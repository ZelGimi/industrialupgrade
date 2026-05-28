package com.denfop.blockentity.mechanism.steam;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntitySteamMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySteamMacerator extends BlockEntitySteamMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntitySteamMacerator(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.MACERATOR.usagePerTick,
                EnumMultiMachine.MACERATOR.lenghtOperation,
                4, BlockBaseMachine3Entity.steam_macerator, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("steam_macerator_soil_pollution_amount", 0.125D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("steam_macerator_air_pollution_amount", 0.2D)));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.steam_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockMacerator.name");
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

}
