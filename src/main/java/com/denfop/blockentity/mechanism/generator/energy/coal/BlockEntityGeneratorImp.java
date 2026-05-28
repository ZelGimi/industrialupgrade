package com.denfop.blockentity.mechanism.generator.energy.coal;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGeneratorImp extends BlockEntityAdvGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityGeneratorImp(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("advanced_generator_efficiency", 3.4D), ModConfig.mechanismInt("advanced_generator_energy_storage", 16000), 3, BlockBaseMachineEntity.imp_gen, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("generator_advanced_soil_pollution_amount", 0.15D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("generator_advanced_air_pollution_amount", 0.35D)));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.imp_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
}
