package com.denfop.api.otherenergies.cool;

import com.denfop.api.energy.networking.ConductorInfo;
import net.minecraft.core.Direction;

import java.util.LinkedList;
import java.util.List;

public class Path {

    public final List<ConductorInfo> conductors;
    public final ICoolSink target;
    public final Direction targetDirection;
    public double min = Double.MAX_VALUE;

    public Path(ICoolSink sink, Direction facing) {
        this.target = sink;
        this.conductors = new LinkedList<>();
        this.targetDirection = facing;
    }

    public double getMin() {
        return min;
    }

    public void setMin(final double min) {
        this.min = min;
    }


    public List<ConductorInfo> getConductors() {
        return conductors;
    }

}
