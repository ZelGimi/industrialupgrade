package com.denfop.api.energy;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BasicSinkSource extends BasicEnergyTile implements IAdvDual {

    protected int sinkTier;
    protected int sourceTier;
    private double perenergy;
    private double pastEnergy;
    private double tick;
    private double perenergy1;
    private double pastEnergy1;
    private double tick1;

    public BasicSinkSource(TileEntity parent, double capacity, int sinkTier, int sourceTier) {
        super(parent, capacity);
        if (sinkTier < 0) {
            throw new IllegalArgumentException("invalid sink tier: " + sinkTier);
        } else if (sourceTier < 0) {
            throw new IllegalArgumentException("invalid source tier: " + sourceTier);
        } else {
            this.sinkTier = sinkTier;
            this.sourceTier = sourceTier;

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
    public boolean isSource() {
        return true;
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


    @Override
    public double getPerEnergy1() {
        return this.perenergy1;
    }

    @Override
    public double getPastEnergy1() {
        return this.pastEnergy1;
    }

    @Override
    public void setPastEnergy1(final double pastEnergy) {
        this.pastEnergy1 = pastEnergy;
    }

    @Override
    public void addPerEnergy1(final double setEnergy) {
        this.perenergy1 += setEnergy;
    }


    @Override
    public void addTick1(final double tick) {
        this.tick1 = tick;
    }

    @Override
    public double getTick1() {
        return this.tick1;
    }

    public double getDemandedEnergy() {
        return Math.max(0.0D, this.getCapacity() - this.getEnergyStored());
    }

    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        this.setEnergyStored(this.getEnergyStored() + amount);
        return 0.0D;
    }

    public int getSinkTier() {
        return this.sinkTier;
    }

    public void setSinkTier(int tier) {
        if (tier < 0) {
            throw new IllegalArgumentException("invalid tier: " + tier);
        } else {
            this.sinkTier = tier;
        }
    }

    public double getOfferedEnergy() {
        return this.getEnergyStored();
    }

    public void drawEnergy(double amount) {
        this.setEnergyStored(this.getEnergyStored() - amount);
    }

    public int getSourceTier() {
        return this.sourceTier;
    }

    public void setSourceTier(int tier) {
        if (tier < 0) {
            throw new IllegalArgumentException("invalid tier: " + tier);
        } else {
            double power = EnergyNet.instance.getPowerFromTier(tier);
            if (this.getCapacity() < power) {
                this.setCapacity(power);
            }

            this.sourceTier = tier;
        }
    }

    protected String getNbtTagName() {
        return "IC2BasicSinkSource";
    }



    @Override
    public boolean acceptsEnergyFrom(final IEnergyEmitter iEnergyEmitter, final EnumFacing enumFacing) {
        return false;
    }

    @Override
    public boolean emitsEnergyTo(final IEnergyAcceptor iEnergyAcceptor, final EnumFacing enumFacing) {
        return false;
    }



}
