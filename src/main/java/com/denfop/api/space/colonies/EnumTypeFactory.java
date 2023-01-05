package com.denfop.api.space.colonies;

public enum EnumTypeFactory {
    LOW(10, 10),
    MEDIUM(20, 15),
    HIGH(30, 20);

    private final int energy;
    private final int people;

    EnumTypeFactory(int energy, int people) {
        this.energy = energy;
        this.people = people;
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
}
