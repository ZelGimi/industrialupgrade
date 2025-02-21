package com.denfop.tiles.reactors.gas.regenerator;

import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IRegenerator;

public class TileEntityRegenerator extends TileEntityMultiBlockElement implements IRegenerator {

    private final int level;
    private final int max;
    private int helium;

    public TileEntityRegenerator(int level, int max) {
        this.level = level;
        this.max = max;
    }

    @Override
    public int getLevel() {
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
