package com.denfop.blockentity.mechanism;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityScanner;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvScanner extends BlockEntityScanner {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityAdvScanner(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismInt("improved_scanner_operation_length", 2500), BlockBaseMachine3Entity.adv_scanner, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("improved_scanner_soil_pollution_amount", 0.075D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("improved_scanner_air_pollution_amount", 0.2D)));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.adv_scanner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
