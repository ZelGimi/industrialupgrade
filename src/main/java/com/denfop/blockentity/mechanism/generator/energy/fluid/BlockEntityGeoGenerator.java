package com.denfop.blockentity.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGeoGenerator extends com.denfop.blockentity.mechanism.generator.energy.BlockEntityGeoGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityGeoGenerator(BlockPos pos, BlockState state) {
        super(10, 1, 1, BlockBaseMachine3Entity.geogenerator_iu, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.35));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.85));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.geogenerator_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
