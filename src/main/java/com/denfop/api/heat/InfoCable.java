package com.denfop.api.heat;

import net.minecraft.core.Direction;

public class InfoCable {

    private final IHeatConductor conductor;
    private final Direction facing;
    private final InfoCable cable;

    public InfoCable(IHeatConductor conductor, Direction facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public Direction getFacing() {
        return facing;
    }

    public IHeatConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
