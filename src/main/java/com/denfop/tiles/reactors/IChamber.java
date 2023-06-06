package com.denfop.tiles.reactors;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;

public interface IChamber extends IReactorChamber {

    TileEntityBaseNuclearReactorElectric getReactor();

    void setReactor(IReactor reactor);

    void destoryChamber(boolean wrench);

}
