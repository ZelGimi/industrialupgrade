package com.denfop.api.heat;

import com.denfop.api.heat.IHeatConductor;
import com.denfop.api.heat.IHeatSink;
import net.minecraft.util.EnumFacing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
