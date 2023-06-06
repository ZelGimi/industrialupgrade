package com.denfop.api.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class BasicSource extends BasicEnergyTile implements IAdvEnergySource {

    protected int tier;
    protected int sourceTier;
    private double perenergy;
    private double pastEnergy;
    private double tick;

    public BasicSource(TileEntity parent, double capacity, int tier) {
        super(parent, capacity);
        if (tier < 0) {
            throw new IllegalArgumentException("invalid tier: " + tier);
        } else {
            this.tier = tier;
            double power = EnergyNetGlobal.instance.getPowerFromTier(tier);
            if (this.getCapacity() < power) {
                this.setCapacity(power);
            }

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

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return true;
    }

    public double getOfferedEnergy() {
        return this.getEnergyStored();
    }

    public void drawEnergy(double amount) {
        this.setEnergyStored(this.getEnergyStored() - amount);
    }

    public int getSourceTier() {
        return this.tier;
    }

    public void setSourceTier(int tier) {
        if (tier < 0) {
            throw new IllegalArgumentException("invalid tier: " + tier);
        } else {
            double power = EnergyNetGlobal.instance.getPowerFromTier(tier);
            if (this.getCapacity() < power) {
                this.setCapacity(power);
            }

            this.tier = tier;
        }
    }

    protected String getNbtTagName() {
        return "IC2BasicSource";
    }


}
