package com.denfop.api.energy;

import net.minecraft.core.BlockPos;

public class ConductorInfo {


    private final double breakdownEnergy;
    private final BlockPos pos;
    private  byte tick;
    private double energy;
    public ConductorInfo(BlockPos pos, IEnergyConductor energyConductor){
        this.breakdownEnergy = energyConductor.getConductorBreakdownEnergy();
        this.pos=pos;
    }


    public void addEnergy(byte tick, double energy) {
        if (tick != this.tick){
            this.tick = tick;
            this.energy = 0;
        }
        this.energy += energy;
    }

    public double getEnergy(int tick) {
        if (this.tick - 1 == tick
                || this.tick == tick
                || this.tick + 1 ==tick
        ) {
            return energy;
        }else {
            this.tick = (byte) tick;
            this.energy = 0;
        }
        return energy;
    }

    public BlockPos getPos() {
        return pos;
    }

    public double getBreakdownEnergy() {
        return breakdownEnergy;
    }

}
    
