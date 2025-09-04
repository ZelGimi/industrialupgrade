package com.denfop.api.otherenergies.common;

import net.minecraft.core.Direction;

public class InfoCable {

    private final IConductor conductor;
    private final Direction facing;
    private final InfoCable cable;

    public InfoCable(IConductor conductor, Direction facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public Direction getFacing() {
        return facing;
    }

    public IConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
