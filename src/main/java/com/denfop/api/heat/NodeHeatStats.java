
package com.denfop.api.heat;

public class NodeHeatStats {

    protected double heatIn;
    protected double heat;

    public NodeHeatStats(double heatIn, double heat) {
        this.heatIn = heatIn;
        this.heat = heat;
    }

    public double getHeatIn() {
        return this.heatIn;
    }

    public double getHeat() {
        return this.heat;
    }

    protected void set(double heatIn, double heat) {
        this.heatIn = heatIn;
        this.heat = heat;
    }

}
