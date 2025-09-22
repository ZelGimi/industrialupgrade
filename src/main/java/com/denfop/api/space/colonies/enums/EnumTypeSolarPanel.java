package com.denfop.api.space.colonies.enums;

public enum EnumTypeSolarPanel {
    LOW(100, 2, 0),
    MEDIUM(200, 5, 9),
    HIGH(375, 12, 17);

    private final int generation;
    private final int people;
    private final byte level;

    EnumTypeSolarPanel(int generation, int people, int level) {
        this.generation = generation;
        this.people = people;
        this.level = (byte) level;
    }

    public static EnumTypeSolarPanel getID(int id) {
        return values()[id % values().length];
    }

    public int getLevel() {
        return level;
    }

    public int getGeneration() {
        return generation;
    }

    public int getPeople() {
        return people;
    }
}
