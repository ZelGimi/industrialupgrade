package com.denfop.api.energy;

import com.denfop.tiles.base.TileEntityTransformer;

public interface ITransformer {

    TileEntityTransformer.Mode getMode();

    boolean isStepUp();

    double getinputflow();

    double getoutputflow();

}
