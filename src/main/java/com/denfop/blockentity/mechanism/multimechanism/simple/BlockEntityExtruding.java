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

public class BlockEntityExtruding extends BlockEntityMultiMachine {

    private final AirPollutionComponent pollutionAir;
    private final SoilPollutionComponent pollutionSoil;

    public BlockEntityExtruding(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.Extruding.usagePerTick, EnumMultiMachine.Extruding.lenghtOperation, BlockMoreMachine2Entity.extruder, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine2Entity.extruder;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2.getBlock(getTeBlock().getId());
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
