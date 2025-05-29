package com.denfop.api.cool;

import net.minecraft.core.Direction;

public class InfoCable {

    private final ICoolConductor conductor;
    private final Direction facing;
    private final InfoCable cable;

    public InfoCable(ICoolConductor conductor, Direction facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public Direction getFacing() {
        return facing;
    }

    public ICoolConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
