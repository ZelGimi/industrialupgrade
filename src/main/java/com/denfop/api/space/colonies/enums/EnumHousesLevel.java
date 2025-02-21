package com.denfop.api.space.colonies.enums;

public enum EnumHousesLevel {
    LOW(0),
    MEDIUM(4),
    HIGH(14);


    private final byte level;

    EnumHousesLevel(int level){
        this.level = (byte) level;
    }
    public int getLevel() {
        return level;
    }
}
