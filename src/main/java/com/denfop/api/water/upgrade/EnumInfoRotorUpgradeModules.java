package com.denfop.api.water.upgrade;

import com.denfop.IUItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EnumInfoRotorUpgradeModules {
    STRENGTH_I(1, 4, 0.05), // 0
    STRENGTH_II(2, 4, 0.1), // 1
    STRENGTH_III(3, 4, 0.15), // 2
    EFFICIENCY_I(1, 2, 0.05),// 3
    EFFICIENCY_II(2, 2, 0.1), // 4
    EFFICIENCY_III(3, 2, 0.15),//5
    POWER_I(1, 4, 0.1),//6
    POWER_II(2, 4, 0.15),//7
    POWER_III(3, 4, 0.2),//8
    AUTO(1, 1, 1),//9
    WIND_I(1, 1, 1, true, 11, 12),//10
    WIND_II(2, 1, 2, true, 10, 12),//11
    WIND_III(3, 1, 3, true, 11, 10),//12
    REPAIR_I(1, 1, 10, true, 14, 15),//13
    REPAIR_II(2, 1, 8, true, 13, 15),//14
    REPAIR_III(3, 1, 6, true, 14, 13),//15
    OCEAN(1,1,1)//16
    ;

    public final String name;
    public final List<Integer> list;
    public final int max;
    private final int level;
    private final double coef;
    private final boolean limitation_type;
    private final List<Integer> ids;

    EnumInfoRotorUpgradeModules(int level, int max, double coef) {
        this.name = this.name().toLowerCase();
        this.list = new ArrayList<>();
        this.level = level;
        this.max = max;
        this.coef = coef;
        this.limitation_type = false;
        this.ids = null;
        IUItem.list1.add(this.name);
    }

    EnumInfoRotorUpgradeModules(int level, int max, double coef, boolean limitation_type, int... ids) {
        this.name = this.name().toLowerCase();
        this.list = new ArrayList<>();
        this.level = level;
        this.max = max;
        this.coef = coef;
        this.ids = Arrays.stream(ids).boxed().collect(Collectors.toList());
        this.limitation_type = limitation_type;
        IUItem.list1.add(this.name);
    }

    public static EnumInfoRotorUpgradeModules getFromID(final int ID) {
        return values()[ID % values().length];
    }

    public List<Integer> getIds() {
        return ids;
    }

    public double getCoef() {
        return coef;
    }

    public int getMax() {
        return max;
    }

    public int getLevel() {
        return level;
    }

    public boolean getLimition() {
        return this.limitation_type;
    }
}
