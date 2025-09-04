package com.denfop.blockentity.mechanism.multimechanism.quad;

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

public class BlockEntityQuadCutting extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityQuadCutting(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.QUAD_Cutting.usagePerTick, EnumMultiMachine.QUAD_Cutting.lenghtOperation, BlockMoreMachine2Entity.quad_cutting,
                pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.025));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine2Entity.quad_cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base2.getBlock(getTeBlock().getId());
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting3.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }

}
