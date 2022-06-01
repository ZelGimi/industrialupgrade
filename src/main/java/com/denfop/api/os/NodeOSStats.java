package com.denfop.api.os;

public class NodeOSStats {

    protected double SEIn;
    protected double SE;

    public NodeOSStats(double SEIn, double SE) {
        this.SEIn = SEIn;
        this.SE = SE;
    }

    public double getSEIn() {
        return this.SEIn;
    }

    public double getSE() {
        return this.SE;
    }

    protected void set(double SEIn, double SE) {
        this.SEIn = SEIn;
        this.SE = SE;
    }

}
