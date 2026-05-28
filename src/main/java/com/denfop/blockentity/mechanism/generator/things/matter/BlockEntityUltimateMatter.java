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

public class BlockEntityUltimateMatter extends BlockEntityMultiMatter {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityUltimateMatter(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("perfect_matter_fabricator_energy_storage", 700000.0D), ModConfig.mechanismInt("perfect_matter_fabricator_tank_capacity", 16), ModConfig.mechanismDouble("perfect_matter_fabricator_energy_storage", 256000000.0D), BlockBaseMachineEntity.per_matter, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("ultimate_matter_soil_pollution_amount", 0.011D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("ultimate_matter_air_pollution_amount", 0.045D)));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.per_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
}
