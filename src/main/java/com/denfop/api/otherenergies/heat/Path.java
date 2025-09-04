package com.denfop.api.otherenergies.heat;

import com.denfop.api.energy.networking.ConductorInfo;
import net.minecraft.core.Direction;

import java.util.LinkedList;
import java.util.List;

public class Path {

    public final List<ConductorInfo> conductors;
    public final IHeatSink target;
    public final Direction targetDirection;
    public double min = Double.MAX_VALUE;

    public Path(IHeatSink sink, Direction facing) {
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
