package com.denfop.blockentity.mechanism.generator.energy.coal;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGenerator extends BlockEntityAdvGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityGenerator(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("generator_efficiency", 1.0D), ModConfig.mechanismInt("generator_energy_storage", 4000), 1, BlockBaseMachine3Entity.generator_iu, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("charging_in_the_generator_soil_pollution_amount", 0.35D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("charging_in_the_generator_air_pollution_amount", 0.75D)));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.generator_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
