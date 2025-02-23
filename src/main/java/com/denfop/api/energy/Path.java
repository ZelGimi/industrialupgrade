package com.denfop.api.energy;

import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Path {

    final IEnergySink target;
    final EnumFacing targetDirection;
    double min = Double.MAX_VALUE;
    double loss = 0.0D;
    boolean hasController = false;
    boolean isLimit = false;
    double limit_amount = Double.MAX_VALUE;
    double adding = 0;
    List<IEnergyConductor> conductorList = new LinkedList<>();
    Path(IEnergySink sink, EnumFacing facing) {
        this.target = sink;
        this.targetDirection = facing;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || (getClass() != o.getClass() && !(o instanceof IEnergySink))) {
            return false;
        }
        if (o instanceof IEnergySink) {
            IEnergySink energySink = (IEnergySink) o;
            return energySink == target;
        }
        Path path = (Path) o;
        return target == path.target;
    }

    @Override
    public int hashCode() {
        return Objects.hash(target);
    }



    public void setHasController(final boolean hasController) {
        this.hasController = hasController;
    }

    public double getMin() {
        return min;
    }

    public void setMin(final double min) {
        this.min = min;
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

    public List<IEnergyConductor> getConductorList() {
        return conductorList;
    }

}
