package com.denfop.api.energy.networking;

import com.denfop.api.energy.interfaces.EnergySink;
import net.minecraft.core.Direction;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Path {

    public final EnergySink target;
    final Direction targetDirection;
    public Direction sourceDirection;
    double min = Double.MAX_VALUE;
    double loss = 0.0D;
    boolean hasController = false;
    boolean isLimit = false;
    double limit_amount = Double.MAX_VALUE;
    double adding = 0;
    List<ConductorInfo> conductorList = new LinkedList<>();

    Path(EnergySink sink, Direction facing) {
        this.target = sink;
        this.targetDirection = facing;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || (getClass() != o.getClass() && !(o instanceof EnergySink))) {
            return false;
        }
        if (o instanceof EnergySink) {
            EnergySink energySink = (EnergySink) o;
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

    public List<ConductorInfo> getConductorList() {
        return conductorList;
    }

}
