package com.denfop.api.energy;

import net.minecraft.core.Direction;

public class InfoCable {

    private final IEnergyConductor conductor;
    private final Direction facing;
    private final InfoCable cable;

    public InfoCable(IEnergyConductor conductor, Direction facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public Direction getFacing() {
        return facing;
    }

    public IEnergyConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
