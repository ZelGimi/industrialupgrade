package com.denfop.api.qe;

public class NodeQEStats {

    protected double QEIn;
    protected double QE;

    public NodeQEStats(double QEIn, double QE) {
        this.QEIn = QEIn;
        this.QE = QE;
    }

    public double getQEIn() {
        return this.QEIn;
    }

    public double getQE() {
        return this.QE;
    }

    protected void set(double QEIn, double QE) {
        this.QEIn = QEIn;
        this.QE = QE;
    }

}
