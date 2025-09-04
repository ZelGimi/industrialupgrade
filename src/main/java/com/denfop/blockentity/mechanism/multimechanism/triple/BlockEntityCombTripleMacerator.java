package com.denfop.blockentity.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class BlockEntityCombTripleMacerator extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityCombTripleMacerator(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.COMB_TRIPLE_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_TRIPLE_MACERATOR.lenghtOperation, BlockMoreMachine1Entity.triple_comb_macerator, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine1Entity.triple_comb_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_TRIPLE_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator2.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
