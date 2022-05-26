
package com.denfop.api.cooling;

public class NodeCoolStats {

    protected double coolIn;
    protected double cool;

    public NodeCoolStats(double coolIn, double cool) {
        this.coolIn = coolIn;
        this.cool = cool;
    }

    public double getHeatIn() {
        return this.coolIn;
    }

    public double getHeat() {
        return this.cool;
    }

    protected void set(double heatIn, double heat) {
        this.coolIn = heatIn;
        this.cool = heat;
    }

}
