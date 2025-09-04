package com.denfop.blockentity.mechanism.generator.energy.fluid;

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

public class BlockEntityPerGeoGenerator extends BlockEntityGeoGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityPerGeoGenerator(BlockPos pos, BlockState state) {
        super(32, 4.6, 4, BlockBaseMachine1Entity.per_geo, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine1Entity.per_geo;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }
}
