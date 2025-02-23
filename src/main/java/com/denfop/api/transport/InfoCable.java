package com.denfop.api.transport;

import net.minecraft.util.EnumFacing;

public class InfoCable {

    private final ITransportConductor conductor;
    private final EnumFacing facing;
    private final InfoCable cable;

    public InfoCable(ITransportConductor conductor, EnumFacing facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public ITransportConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
