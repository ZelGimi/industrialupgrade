package com.denfop.blockentity.mechanism.generator.energy.fluid;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.generator.energy.BlockEntityGeoGenerator;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpGeoGenerator extends BlockEntityGeoGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityImpGeoGenerator(BlockPos pos, BlockState state) {
        super(20, 3.4, 3, BlockBaseMachine1Entity.imp_geo, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("advanced_geo_generator_soil_pollution_amount", 0.15D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("advanced_geo_generator_air_pollution_amount", 0.35D)));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine1Entity.imp_geo;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }
}
