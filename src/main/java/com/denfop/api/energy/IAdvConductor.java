package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergyConductor;

public interface IAdvConductor extends IEnergyConductor, IAdvEnergyTile {

    void update_render();

}
