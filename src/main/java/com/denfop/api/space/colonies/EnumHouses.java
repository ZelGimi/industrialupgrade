package com.denfop.api.space.colonies;

public enum EnumHouses {
    LOW(20, 10, EnumHousesLevel.LOW, 10),
    MEDIUM(35, 18, EnumHousesLevel.MEDIUM, 15),
    HIGH(45, 25, EnumHousesLevel.HIGH, 20),
    ;
    private final int max;
    private final int energy;
    private final EnumHousesLevel level;
    private final int consumeOxygen;

    EnumHouses(int max, int energy, EnumHousesLevel level, int consumeOxygen) {
        this.max = max;
        this.energy = energy;
        this.level = level;
        this.consumeOxygen = consumeOxygen;
    }

    public static EnumHouses getID(int id) {
        return values()[id % values().length];
    }

    public int getMax() {
        return max;
    }

    public EnumHousesLevel getLevel() {
        return level;
    }

    public int getEnergy() {
        return energy;
    }

    public int getConsumeOxygen() {
        return consumeOxygen;
    }
}
