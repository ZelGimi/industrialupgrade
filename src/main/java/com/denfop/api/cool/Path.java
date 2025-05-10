package com.denfop.api.cool;

import net.minecraft.core.Direction;

import java.util.LinkedList;
import java.util.List;

public class Path {

    public final List<ICoolConductor> conductors;
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


    public List<ICoolConductor> getConductors() {
        return conductors;
    }

}
