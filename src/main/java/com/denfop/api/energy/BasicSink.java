package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BasicSink extends BasicEnergyTile implements IAdvEnergySink {

    protected int tier;
    protected int sourceTier;
    private double perenergy;
    private double pastEnergy;
    private double tick;

    public BasicSink(TileEntity parent, double capacity, int tier) {
        super(parent, capacity);
        if (tier < 0) {
            throw new IllegalArgumentException("invalid tier: " + tier);
        } else {
            this.tier = tier;
        }
    }

    @Override
    public double getPerEnergy() {
        return this.perenergy;
    }

    @Override
    public double getPastEnergy() {
        return this.pastEnergy;
    }

    @Override
    public void setPastEnergy(final double pastEnergy) {
        this.pastEnergy = pastEnergy;
    }

    @Override
    public void addPerEnergy(final double setEnergy) {
        this.perenergy += setEnergy;
    }

    @Override
    public void addTick(final double tick) {
        this.tick = tick;
    }

    @Override
    public double getTick() {
        return this.tick;
    }

    @Override
    public boolean isSink() {
        return true;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
        return true;
    }

    public double getDemandedEnergy() {
        return Math.max(0.0D, this.getCapacity() - this.getEnergyStored());
    }

    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        this.setEnergyStored(this.getEnergyStored() + amount);
        return 0.0D;
    }

    public int getSinkTier() {
        return this.tier;
    }

    public void setSinkTier(int tier) {
        if (tier < 0) {
            throw new IllegalArgumentException("invalid tier: " + tier);
        } else {
            this.tier = tier;
        }
    }

    protected String getNbtTagName() {
        return "IC2BasicSink";
    }

    @Override
    public TileEntity getTileEntity() {
        return this.tile;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

}
