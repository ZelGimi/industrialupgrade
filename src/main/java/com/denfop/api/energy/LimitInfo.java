package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergySink;
import net.minecraft.world.World;

public class LimitInfo {

    private final World world;
    private final IEnergySink sink;
    private final double amount;
    private boolean limit;

    public LimitInfo(IEnergySink sink, World world, boolean limit, double amount) {
        this.world = world;
        this.sink = sink;
        this.limit = limit;
        this.amount = amount;
    }

    public World getWorld() {
        return world;
    }

    public IEnergySink getSink() {
        return sink;
    }

    public boolean isLimit() {
        return limit;
    }

    public void setLimit(double amount) {
        this.limit = true;
    }

    public double getAmount() {
        return amount;
    }

    public void removeLimit() {
        this.limit = false;
    }

}
