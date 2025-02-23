package com.denfop.api.energy;

import net.minecraft.util.EnumFacing;

public class InfoCable {

    private final IEnergyConductor conductor;
    private final EnumFacing facing;
    private final InfoCable cable;

    public InfoCable(IEnergyConductor conductor, EnumFacing facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public IEnergyConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
