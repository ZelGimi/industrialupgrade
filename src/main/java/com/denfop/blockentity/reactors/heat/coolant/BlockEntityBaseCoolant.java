package com.denfop.blockentity.reactors.heat.coolant;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.heat.ICoolant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityBaseCoolant extends BlockEntityMultiBlockElement implements ICoolant {

    private final int levelBlock;
    private final int max;
    private int helium;

    public BlockEntityBaseCoolant(int levelBlock, int max, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.max = max;
    }

    @Override
    public int getBlockLevel() {
        return levelBlock;
    }


    @Override
    public void addHeliumToRegenerate(final double col) {
        this.helium += col;
    }

    @Override
    public double getMaxRegenerate() {
        return max;
    }

    @Override
    public double getHeliumToRegenerate() {
        return helium;
    }

}
