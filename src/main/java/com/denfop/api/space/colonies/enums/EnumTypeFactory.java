package com.denfop.api.space.colonies.enums;

public enum EnumTypeFactory {
    LOW(10, 15, 0),
    MEDIUM(20, 20, 8),
    HIGH(35, 35, 21);

    private final byte energy;
    private final byte people;
    private final byte level;

    EnumTypeFactory(int energy, int people, int level) {
        this.energy = (byte) energy;
        this.people = (byte) people;
        this.level = (byte) level;
    }

    public static EnumTypeFactory getID(int id) {
        return values()[id % values().length];
    }

    public int getEnergy() {
        return energy;
    }

    public int getPeople() {
        return people;
    }

    public int getLevel() {
        return level;
    }
}
