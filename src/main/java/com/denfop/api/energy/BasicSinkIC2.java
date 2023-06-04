package com.denfop.api.energy;

import ic2.api.energy.prefab.BasicSink;
import net.minecraft.util.EnumFacing;

public class BasicSinkIC2 extends BasicEnergyTile implements IAdvEnergySink {

    private final BasicSink basicSink;
    protected int tier;
    protected int sourceTier;
    private double perenergy;
    private double pastEnergy;
    private double tick;


    public BasicSinkIC2(BasicSink tile) {
        if (tile.getSinkTier() < 0) {
            throw new IllegalArgumentException("invalid tier: " + tile.getSinkTier());
        } else {
            this.tier = tile.getSinkTier();
        }

        this.tile = tile.getWorldObj().getTileEntity(tile.getPosition());
        this.pos = tile.getPosition();
        this.basicSink = tile;
    }


    @Override
    public void onLoad() {
        super.onLoad();
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
        return basicSink.getDemandedEnergy();
    }

    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        basicSink.injectEnergy(directionFrom, amount, voltage);
        return 0;
    }

    public int getSinkTier() {
        return this.tier;
    }

    public void setSinkTier(int tier) {
        if (tier < 0) {
            throw new IllegalArgumentException("invalid tier: " + tier);
        } else {
            this.tier = tier;
            this.basicSink.setSinkTier(tier);
        }
    }

    protected String getNbtTagName() {
        return "IC2BasicSink";
    }


}
