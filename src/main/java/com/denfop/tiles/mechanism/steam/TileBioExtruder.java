package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileBioMultiMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

public class TileBioExtruder extends TileBioMultiMachine {


    public TileBioExtruder(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.Extruding.usagePerTick,
                EnumMultiMachine.Extruding.lenghtOperation,
                4, BlockBaseMachine3.bio_extruder, pos, state
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.bio_extruder;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Extruding;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }


}
