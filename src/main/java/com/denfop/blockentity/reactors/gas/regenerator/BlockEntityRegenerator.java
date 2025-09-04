package com.denfop.blockentity.reactors.gas.regenerator;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.gas.IRegenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityRegenerator extends BlockEntityMultiBlockElement implements IRegenerator {

    private final int level;
    private final int max;
    private int helium;

    public BlockEntityRegenerator(int level, int max, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.level = level;
        this.max = max;
    }

    @Override
    public int getBlockLevel() {
        return level;
    }

    @Override
    public int getMaxHelium() {
        return this.max;
    }

    @Override
    public int getHelium() {
        return this.helium;
    }

    @Override
    public void addHelium(final int helium) {
        this.helium += helium;
    }

}
