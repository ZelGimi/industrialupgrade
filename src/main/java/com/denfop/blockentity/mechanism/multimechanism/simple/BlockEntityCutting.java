package com.denfop.blockentity.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine2Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCutting extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityCutting(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.Cutting.usagePerTick, EnumMultiMachine.Cutting.lenghtOperation, BlockMoreMachine2Entity.cutting, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine2Entity.cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2.getBlock(getTeBlock().getId());
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }


}
