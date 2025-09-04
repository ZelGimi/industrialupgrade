package com.denfop.blockentity.mechanism.multimechanism.dual;

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


public class BlockEntityDoubleRecycler extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityDoubleRecycler(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.DOUBLE_RECYCLER.usagePerTick,
                EnumMultiMachine.DOUBLE_RECYCLER.lenghtOperation, BlockMoreMachine1Entity.double_recycler, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine1Entity.double_recycler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1.getBlock(getTeBlock().getId());
    }

    public void initiate(int soundEvent) {
        if (this.getWorld().getGameTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_RECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRecycler.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
