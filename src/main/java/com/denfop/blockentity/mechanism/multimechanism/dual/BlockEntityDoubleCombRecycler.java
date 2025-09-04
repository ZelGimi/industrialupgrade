package com.denfop.blockentity.mechanism.multimechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.sound.EnumSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityDoubleCombRecycler extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityDoubleCombRecycler(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.DOUBLE_COMB_RECYCLER.usagePerTick,
                EnumMultiMachine.DOUBLE_COMB_RECYCLER.lenghtOperation, BlockMoreMachine1Entity.double_comb_recycler, pos, state
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine1Entity.double_comb_recycler;
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
        return EnumMultiMachine.DOUBLE_COMB_RECYCLER;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.RecyclerOp.getSoundEvent();
    }


}
