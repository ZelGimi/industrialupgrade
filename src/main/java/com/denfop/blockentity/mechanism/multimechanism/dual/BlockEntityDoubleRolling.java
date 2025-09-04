package com.denfop.blockentity.mechanism.multimechanism.dual;

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

public class BlockEntityDoubleRolling extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityDoubleRolling(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.Rolling.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, BlockMoreMachine2Entity.double_rolling, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine2Entity.double_rolling;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2.getBlock(getTeBlock().getId());
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Rolling;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRolling1.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/plate.ogg";
    }


}
