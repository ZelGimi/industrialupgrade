package com.denfop.api.energy.networking;

public class NodeStats {

    protected double energyIn;
    protected double energyOut;

    public NodeStats(double energyIn, double energyOut) {
        this.energyIn = energyIn;
        this.energyOut = energyOut;
    }

    public double getEnergyIn() {
        return this.energyIn;
    }

    public double getEnergyOut() {
        return this.energyOut;
    }

}
