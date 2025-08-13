package com.denfop.componets;

public class BufferEnergy {
    public int sourceTier;
    public int sinkTier;
    public double storage;
    public double capacity;
    public boolean allow;
    public boolean need;

    public BufferEnergy(double storage, double capacity, int sinkTier, int sourceTier){
        this.storage=storage;
        this.capacity = capacity;
        this.sourceTier=sourceTier;
        this.sinkTier=sinkTier;
    }

}
