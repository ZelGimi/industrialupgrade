package com.denfop.api.otherenergies.transport;

import net.minecraft.core.Direction;

public class InfoCable {

    private final ITransportConductor conductor;
    private final Direction facing;
    private final InfoCable cable;

    public InfoCable(ITransportConductor conductor, Direction facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public Direction getFacing() {
        return facing;
    }

    public ITransportConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
