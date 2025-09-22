package com.denfop.api.heat;

import net.minecraft.util.EnumFacing;

import java.util.LinkedList;
import java.util.List;

public class Path {

    public final List<IHeatConductor> conductors;
    public final IHeatSink target;
    public final EnumFacing targetDirection;
    public double min = Double.MAX_VALUE;

    public Path(IHeatSink sink, EnumFacing facing) {
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


    public List<IHeatConductor> getConductors() {
        return conductors;
    }

}
