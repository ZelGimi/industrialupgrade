package com.denfop.blockentity.mechanism.heat;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBaseHeatMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityFluidHeat extends BlockEntityBaseHeatMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityFluidHeat(BlockPos pos, BlockState state) {
        super(true, BlockBaseMachine3Entity.fluid_heat, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.24));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.4));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.fluid_heat;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
