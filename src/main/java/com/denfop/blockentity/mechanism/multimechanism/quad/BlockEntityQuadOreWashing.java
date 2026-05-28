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

public class BlockEntityQuadOreWashing extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityQuadOreWashing(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.QUAD_OreWashing.usagePerTick,
                EnumMultiMachine.QUAD_OreWashing.lenghtOperation,
                BlockMoreMachine3Entity.quadorewashing, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("quad_ore_washing_soil_pollution_amount", 0.025D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("quad_ore_washing_air_pollution_amount", 0.05D)));
    }

    public int getSize(int size) {
        return Math.min(size, this.tank.getFluidAmount() / 1000);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine3Entity.quadorewashing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_OreWashing;
    }


}
