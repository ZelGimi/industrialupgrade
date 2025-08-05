package com.denfop.tiles.mechanism.bio;

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

public class TileBioCutting extends TileBioMultiMachine {


    public TileBioCutting(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.Cutting.usagePerTick,
                EnumMultiMachine.Cutting.lenghtOperation,
                4, BlockBaseMachine3.bio_cutting, pos, state
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.bio_cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Cutting;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.steam.getSoundEvent();
    }

}
