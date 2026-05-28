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


public class BlockEntityAdvancedMatter extends BlockEntityMultiMatter {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityAdvancedMatter(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("improved_matter_fabricator_energy_storage", 900000.0D), ModConfig.mechanismInt("improved_matter_fabricator_tank_capacity", 12), ModConfig.mechanismDouble("improved_matter_fabricator_energy_storage", 8000000.0D), BlockBaseMachineEntity.adv_matter, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("advanced_matter_soil_pollution_amount", 0.02D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("advanced_matter_air_pollution_amount", 0.1D)));
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.adv_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }

}
