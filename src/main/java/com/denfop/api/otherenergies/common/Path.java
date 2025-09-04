package com.denfop.api.otherenergies.common;

import com.denfop.api.energy.networking.ConductorInfo;
import net.minecraft.core.Direction;

import java.util.ArrayList;

public class Path {


    final ISink target;
    final Direction targetDirection;
    public ArrayList<ConductorInfo> conductorList = new ArrayList<>();

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
