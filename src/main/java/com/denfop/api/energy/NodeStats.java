package com.denfop.api.energy;

public class NodeStats {

    protected double energyIn;
    protected double energyOut;
    protected double voltage;

    public NodeStats(double energyIn, double energyOut, double voltage) {
        this.energyIn = energyIn;
        this.energyOut = energyOut;
        this.voltage = voltage;
    }

    public double getEnergyIn() {
        return this.energyIn;
    }

    public double getEnergyOut() {
        return this.energyOut;
    }

    public double getVoltage() {
        return this.voltage;
    }

}
