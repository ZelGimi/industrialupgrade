package com.denfop.api.sytem;

public class NodeStats {

    protected double In;
    protected double Out;

    public NodeStats(double In, double Out) {
        this.In = In;
        this.Out = Out;
    }

    public double getIn() {
        return this.In;
    }

    public double getOut() {
        return this.Out;
    }

    protected void set(double In, double Out) {
        this.In = In;
        this.Out = Out;
    }

}
