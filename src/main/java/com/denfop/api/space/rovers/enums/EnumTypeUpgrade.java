package com.denfop.api.space.rovers.enums;

import com.denfop.IUItem;

public enum EnumTypeUpgrade {
    SOLAR(3),
    HEATER(4),
    COOLER(4),
    PRESSURE(1),
    PROTECTION(4),
    ENGINE(4),
    DRILL(4);

    private final int max;
    private final String upgrade;

    EnumTypeUpgrade(int max) {
        this.max = max;
        this.upgrade = this.name().toLowerCase();
        IUItem.list_space_upgrades.add(this.upgrade);
    }

    public static EnumTypeUpgrade getFromID(final int ID) {
        return values()[ID % values().length];
    }

    public int getMax() {
        return max;
    }

    public String getUpgrade() {
        return upgrade;
    }

}
