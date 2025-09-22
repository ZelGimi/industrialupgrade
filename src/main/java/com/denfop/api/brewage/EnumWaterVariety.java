package com.denfop.api.brewage;

import java.util.Arrays;
import java.util.List;

public enum EnumWaterVariety {
    WATERY(31, 32, 33, 34, 35, 36, 37, 38, 39, 50, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50
            , 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64),
    LITE(15),
    WITHOUT_NAME(10),
    STRONG(3, 4, 6, 7, 8, 9),
    THICK(5),
    STODGE(30),
    BLACK_STUFF(1, 2),
    ;
    static EnumWaterVariety[] varieties = values();
    private List<Integer> amount;

    EnumWaterVariety(int... col) {
        this.amount = Arrays.asList(Arrays.stream(col).boxed().toArray(Integer[]::new));
    }

    public static EnumWaterVariety getVarietyFromLevelWater(int water) {
        for (EnumWaterVariety entry : varieties) {
            if (entry.amount.contains(water)) {
                return entry;
            }
        }
        return EnumWaterVariety.BLACK_STUFF;
    }

    ;

    public EnumWaterVariety[] getVarieties() {
        return varieties;
    }

    public List<Integer> getAmount() {
        return amount;
    }
}
