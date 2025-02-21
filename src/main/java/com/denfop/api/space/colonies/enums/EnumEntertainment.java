package com.denfop.api.space.colonies.enums;

public enum EnumEntertainment {
    LOW(10, 0,40,7),
    MEDIUM(20,  5,100,13),
    HIGH(40, 10,200,22);
    private final byte energy;
    private final byte needPeople;
    private final short entertainment;
    private final byte level;

    EnumEntertainment(int energy, int needPeople, int entertainment, int level) {
        this.energy = (byte) energy;
        this.needPeople = (byte) needPeople;
        this.entertainment = (short) entertainment;
        this.level = (byte) level;
    }

    public byte getLevel() {
        return level;
    }

    public static EnumEntertainment getID(int id) {
        return values()[id % values().length];
    }

    public int getEnergy() {
        return energy;
    }

    public short getEntertainment() {
        return entertainment;
    }

    public int getNeedPeople() {
        return needPeople;
    }

}
