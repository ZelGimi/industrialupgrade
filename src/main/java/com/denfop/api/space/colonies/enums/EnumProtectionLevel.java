package com.denfop.api.space.colonies.enums;

public enum EnumProtectionLevel {

    LOW(50, 0, 5, 0),
    MEDIUM(100, 5, 8, 7),
    HIGH(200, 12, 15, 15),
    VERY_HIGH(500, 20, 20, 25);

    private final byte energy;
    private final byte people;
    private final short protection;
    private final byte level;

    EnumProtectionLevel(int protection, int people, int energy, int level) {
        this.protection = (short) protection;
        this.people = (byte) people;
        this.energy = (byte) energy;
        this.level = (byte) level;
    }

    public byte getLevel() {
        return level;
    }

    public byte getEnergy() {
        return energy;
    }

    public short getProtection() {
        return protection;
    }

    public byte getPeople() {
        return people;
    }
}
