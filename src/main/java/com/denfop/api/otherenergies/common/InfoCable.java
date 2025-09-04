package com.denfop.api.otherenergies.common;

import com.denfop.api.otherenergies.common.interfaces.Conductor;
import net.minecraft.core.Direction;

public class InfoCable {

    private final Conductor conductor;
    private final Direction facing;
    private final InfoCable cable;

    public InfoCable(Conductor conductor, Direction facing, InfoCable cable) {
        this.conductor = conductor;
        this.facing = facing;
        this.cable = cable;
    }

    public Direction getFacing() {
        return facing;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public InfoCable getPrev() {
        return cable;
    }

}
