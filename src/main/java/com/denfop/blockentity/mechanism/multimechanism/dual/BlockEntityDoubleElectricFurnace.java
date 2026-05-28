package com.denfop.blockentity.mechanism.multimechanism.dual;



import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityDoubleElectricFurnace extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityDoubleElectricFurnace(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.DOUBLE_ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.DOUBLE_ELECTRIC_FURNACE.lenghtOperation, BlockMoreMachineEntity.double_furnace, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("double_electric_furnace_soil_pollution_amount", 0.075D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("double_electric_furnace_air_pollution_amount", 0.1D)));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachineEntity.double_furnace;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_ELECTRIC_FURNACE;
    }


}
