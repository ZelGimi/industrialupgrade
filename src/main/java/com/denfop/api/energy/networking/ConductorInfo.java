package com.denfop.api.energy.networking;

import com.denfop.api.energy.interfaces.EnergyConductor;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.otherenergies.common.IConductor;
import com.denfop.api.otherenergies.cool.ICoolConductor;
import com.denfop.api.otherenergies.heat.IHeatConductor;
import net.minecraft.core.BlockPos;

public class ConductorInfo {


    private final double breakdownEnergy;
    private final BlockPos pos;
    private byte tick;
    private double energy;

    public ConductorInfo(BlockPos pos, EnergyConductor energyConductor) {
        this.breakdownEnergy = energyConductor.getConductorBreakdownEnergy();
        this.pos = pos;
    }

    public ConductorInfo(BlockPos pos, IConductor energyConductor, EnergyType energyType) {
        this.breakdownEnergy = energyConductor.getConductorBreakdownEnergy(energyType);
        this.pos = pos;
    }

    public ConductorInfo(BlockPos pos, ICoolConductor energyConductor) {
        this.breakdownEnergy = energyConductor.getConductorBreakdownCold();
        this.pos = pos;
    }

    public ConductorInfo(BlockPos pos, IHeatConductor energyConductor) {
        this.breakdownEnergy = energyConductor.getConductorBreakdownHeat();
        this.pos = pos;
    }

    public void addEnergy(byte tick, double energy) {
        if (tick != this.tick) {
            this.tick = tick;
            this.energy = 0;
        }
        this.energy += energy;
    }

    public double getEnergy(int tick) {
        if (this.tick - 1 == tick
                || this.tick == tick
                || this.tick + 1 == tick
        ) {
            return energy;
        } else {
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
    
