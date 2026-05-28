package com.denfop.blockentity.mechanism.generator.things.matter;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMatter;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImprovedMatter extends BlockEntityMultiMatter {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityImprovedMatter(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("advanced_matter_fabricator_energy_storage", 800000.0D), ModConfig.mechanismInt("advanced_matter_fabricator_tank_capacity", 14), ModConfig.mechanismDouble("advanced_matter_fabricator_energy_storage", 64000000.0D), BlockBaseMachineEntity.imp_matter, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("improved_matter_soil_pollution_amount", 0.015D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("improved_matter_air_pollution_amount", 0.065D)));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.imp_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
}
