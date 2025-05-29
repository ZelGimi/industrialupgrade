package com.denfop.api.sytem;

import net.minecraft.core.Direction;

public class Path {


    final ISink target;
    final Direction targetDirection;

    Path(ISink sink, Direction facing) {
        this.target = sink;
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


}
