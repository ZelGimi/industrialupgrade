package com.denfop.api.cool;

import net.minecraft.util.EnumFacing;

public class InfoCable {

    private final ICoolConductor conductor;
    private final EnumFacing facing;
    private final InfoCable cable;

    public InfoCable(ICoolConductor conductor, EnumFacing facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public ICoolConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
