package com.denfop.blockentity.mechanism.multimechanism.quad;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityQuadCentrifuge extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityQuadCentrifuge(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.QUAD_Centrifuge.usagePerTick,
                EnumMultiMachine.QUAD_Centrifuge.lenghtOperation, BlockMoreMachine3Entity.quadcentrifuge, pos, state
        );

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("quad_centrifuge_soil_pollution_amount", 0.025D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("quad_centrifuge_air_pollution_amount", 0.05D)));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.cold.buffer.storage = 0;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine3Entity.quadcentrifuge;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Centrifuge;
    }


}
