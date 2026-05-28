package com.denfop.blockentity.mechanism.multimechanism.simple;


import com.denfop.config.ModConfig;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCompressor extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityCompressor(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.COMPRESSER.usagePerTick,
                EnumMultiMachine.COMPRESSER.lenghtOperation, BlockSimpleMachineEntity.compressor_iu, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("primitive_compressor_soil_pollution_amount", 0.1D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("primitive_compressor_air_pollution_amount", 0.15D)));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockSimpleMachineEntity.compressor_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.simplemachine.getBlock(getTeBlock().getId());
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMPRESSER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCompressor.name");
    }

    public String getStartSoundFile() {
        return "Machines/CompressorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
