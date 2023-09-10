package com.denfop.api.energy;

import com.denfop.tiles.base.TileTransformer;

public interface ITransformer {

    TileTransformer.Mode getMode();

    boolean isStepUp();

    double getinputflow();

    double getoutputflow();

}
