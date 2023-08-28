package com.denfop.api.space.colonies;

public enum EnumTypeSolarPanel {
    LOW(20, 0),
    MEDIUM(40, 5),
    HIGH(60, 12);

    private final int generation;
    private final int people;

    EnumTypeSolarPanel(int generation, int people) {
        this.generation = generation;
        this.people = people;
    }

    public static EnumTypeSolarPanel getID(int id) {
        return values()[id % values().length];
    }

    public int getGeneration() {
        return generation;
    }

    public int getPeople() {
        return people;
    }
}
