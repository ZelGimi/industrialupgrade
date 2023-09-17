package com.denfop.api.sytem;

import net.minecraft.util.EnumFacing;

import java.util.HashSet;
import java.util.Set;

public class Path {

    final Set<IConductor> conductors;
    final ISink target;
    final EnumFacing targetDirection;
    double min = Double.MAX_VALUE;

    Path(ISink sink, EnumFacing facing) {
        this.target = sink;
        this.conductors = new HashSet<>();
        this.targetDirection = facing;
    }

    public void tick(int tick, double adding) {
        if (this.target.isSink()) {
            if (this.target.getTick() != tick) {
                this.target.addTick(tick);
                this.target.setPastEnergy(this.target.getPerEnergy());

            }
            this.target.addPerEnergy(adding);
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(final double min) {
        this.min = min;
    }

    public Set<IConductor> getConductors() {
        return conductors;
    }

}
