package com.denfop.blockentity.mechanism.replicator;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.BlockEntityBaseReplicator;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityReplicator extends BlockEntityBaseReplicator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityReplicator(BlockPos pos, BlockState state) {
        super(ModConfig.mechanismDouble("replicator_efficiency", 1.0D), BlockBaseMachine3Entity.replicator_iu, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("replicator_soil_pollution_amount", 0.1D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("replicator_air_pollution_amount", 0.2D)));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.replicator_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
