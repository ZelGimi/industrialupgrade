package com.denfop.screen;

public class FuelAllocation {
    public int remaining;
    public int rocketLevel;
    public int fuelLevel;
    public int fuelUsed;
    public int upgrades;

    public FuelAllocation(int rocketLevel, int fuelLevel, int fuelUsed, int upgrades, double remaining) {
        this.rocketLevel = rocketLevel;
        this.fuelLevel = fuelLevel;
        this.fuelUsed = fuelUsed;
        this.upgrades = upgrades;
        this.remaining = (int) remaining;
    }

    @Override
    public String toString() {
        return "RocketLevel " + rocketLevel + " (+" + upgrades + " upgrades)" +
                " -> FuelLevel " + fuelLevel + ": " + fuelUsed + " units" +
                " seconds: " + remaining;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public int getFuelUsed() {
        return fuelUsed;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getRocketLevel() {
        return rocketLevel;
    }

    public int getUpgrades() {
        return upgrades;
    }
}
