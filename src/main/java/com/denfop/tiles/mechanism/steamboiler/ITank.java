package com.denfop.tiles.mechanism.steamboiler;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;

public interface ITank extends IMultiElement {

    Fluids.InternalFluidTank getTank();

    void setSteam();

    ComponentSteamEnergy getSteam();
}
