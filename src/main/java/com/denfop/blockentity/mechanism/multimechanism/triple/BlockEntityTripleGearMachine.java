package com.denfop.blockentity.mechanism.multimechanism.triple;

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

public class BlockEntityTripleGearMachine extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityTripleGearMachine(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.TRIPLE_Gearing.usagePerTick, EnumMultiMachine.TRIPLE_Gearing.lenghtOperation, BlockMoreMachine3Entity.triplegearing, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine3Entity.triplegearing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Gearing;
    }

}
