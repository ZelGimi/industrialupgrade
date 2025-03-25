package com.denfop.api.space.colonies.enums;

public enum EnumMiningFactory {
    LOW(10, 15,15,5,1,10),
    MEDIUM(20,  20,25,15,2,18),
    HIGH(40, 30,40,30,5,29);
    private final byte energy;
    private final byte needPeople;
    private final byte chance;
    private final byte maxValue;
    private final byte maxItemValue;
    private final byte level;

    EnumMiningFactory(int energy, int needPeople, int chance, int maxValue, int maxValueItem, int level) {
        this.energy = (byte) energy;
        this.needPeople = (byte) needPeople;
        this.chance = (byte) chance;
        this.maxValue= (byte) maxValue;
        this.maxItemValue= (byte) maxValueItem;
        this.level = (byte) level;
    }

    public byte getLevel() {
        return level;
    }

    public static EnumMiningFactory getID(int id) {
        return values()[id % values().length];
    }

    public int getEnergy() {
        return energy;
    }

    public byte getChance() {
        return chance;
    }

    public byte getMaxValue() {
        return maxValue;
    }

    public byte getMaxItemValue() {
        return maxItemValue;
    }

    public int getNeedPeople() {
        return needPeople;
    }

}
