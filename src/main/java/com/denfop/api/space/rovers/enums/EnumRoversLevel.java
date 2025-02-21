package com.denfop.api.space.rovers.enums;

import com.denfop.api.space.EnumLevels;

import java.util.Arrays;
import java.util.List;

public enum EnumRoversLevel {

    ONE(EnumLevels.FIRST,EnumLevels.SECOND),
    TWO(EnumLevels.FIRST,EnumLevels.SECOND,EnumLevels.THIRD,EnumLevels.FOURTH),
    THREE(EnumLevels.FIRST,EnumLevels.SECOND,EnumLevels.THIRD,EnumLevels.FOURTH,EnumLevels.FIVE),
    FOUR(EnumLevels.FIRST,EnumLevels.SECOND,EnumLevels.THIRD,EnumLevels.FOURTH,EnumLevels.FIVE,EnumLevels.SIX,EnumLevels.SEVEN);
    List<EnumLevels> levelsList;
    EnumRoversLevel(EnumLevels... levels){
       levelsList = Arrays.asList(levels);
    }

    public List<EnumLevels> getLevelsList() {
        return levelsList;
    }
}
