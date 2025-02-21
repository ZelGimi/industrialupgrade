package com.denfop.tiles.reactors.heat.coolant;

import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.ICoolant;

public class TileEntityBaseCoolant extends TileEntityMultiBlockElement implements ICoolant {

    private final int level;
    private final int max;
    private int helium;

    public TileEntityBaseCoolant(int level, int max) {
        this.level = level;
        this.max = max;
    }

    @Override
    public int getLevel() {
        return level;
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
