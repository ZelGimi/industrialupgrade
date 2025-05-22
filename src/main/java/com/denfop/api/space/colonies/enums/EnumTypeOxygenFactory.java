package com.denfop.api.space.colonies.enums;

public enum EnumTypeOxygenFactory {
    LOW(10, 2, 0, 2),
    MEDIUM(20, 7, 9, 4),
    HIGH(35, 14, 21, 8);
    private final byte generation;
    private final byte energy;
    private final byte people;
    private final byte level;

    EnumTypeOxygenFactory(int energy, int people, int level, int generation) {
        this.energy = (byte) energy;
        this.people = (byte) people;
        this.level = (byte) level;
        this.generation = (byte) generation;
    }

    public static EnumTypeOxygenFactory getID(int id) {
        return values()[id % values().length];
    }

    public int getEnergy() {
        return energy;
    }

    public byte getGeneration() {
        return generation;
    }

    public int getPeople() {
        return people;
    }

    public int getLevel() {
        return level;
    }
}
