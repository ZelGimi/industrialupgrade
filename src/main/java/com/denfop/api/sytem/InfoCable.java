package com.denfop.api.sytem;

import net.minecraft.util.EnumFacing;

public class InfoCable {

    private final IConductor conductor;
    private final EnumFacing facing;
    private final InfoCable cable;

    public InfoCable(IConductor conductor, EnumFacing facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public IConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
