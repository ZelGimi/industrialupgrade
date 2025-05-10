package com.denfop.api.space.colonies.enums;

public enum EnumHouses {
    LOW(20, 10, EnumHousesLevel.LOW, 1),
    MEDIUM(40, 18, EnumHousesLevel.MEDIUM, 1.5),
    HIGH(70, 25, EnumHousesLevel.HIGH, 2),
    ;
    private final byte max;
    private final byte energy;
    private final EnumHousesLevel level;
    private final double consumeOxygen;

    EnumHouses(int max, int energy, EnumHousesLevel level, double consumeOxygen) {
        this.max = (byte) max;
        this.energy = (byte) energy;
        this.level = level;
        this.consumeOxygen = consumeOxygen;
    }

    public static EnumHouses getID(int id) {
        return values()[id % values().length];
    }

    public byte getMax() {
        return max;
    }

    public EnumHousesLevel getLevel() {
        return level;
    }

    public byte getEnergy() {
        return energy;
    }

    public double getConsumeOxygen() {
        return consumeOxygen;
    }
}
