package com.denfop.api.otherenergies.common.networking;

import com.denfop.api.energy.networking.ConductorInfo;
import com.denfop.api.otherenergies.common.interfaces.Sink;
import net.minecraft.core.Direction;

import java.util.ArrayList;

public class Path {


    final Sink target;
    final Direction targetDirection;
    public ArrayList<ConductorInfo> conductorList = new ArrayList<>();

    Path(Sink sink, Direction facing) {
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
