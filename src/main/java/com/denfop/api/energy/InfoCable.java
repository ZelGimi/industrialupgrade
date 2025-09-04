package com.denfop.api.energy;

import com.denfop.api.energy.interfaces.EnergyConductor;
import net.minecraft.core.Direction;

public class InfoCable {

    private final EnergyConductor conductor;
    private final Direction facing;
    private final InfoCable cable;

    public InfoCable(EnergyConductor conductor, Direction facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public Direction getFacing() {
        return facing;
    }

    public EnergyConductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
