package com.denfop.api.heat;

import net.minecraft.util.EnumFacing;

public class InfoCable {

    private final IHeatConductor conductor;
    private final EnumFacing facing;
    private final InfoCable cable;

    public InfoCable(IHeatConductor conductor, EnumFacing facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public IHeatConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
