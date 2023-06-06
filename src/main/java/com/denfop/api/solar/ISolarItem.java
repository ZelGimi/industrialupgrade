package com.denfop.api.solar;


public interface ISolarItem {

    EnumSolarType getType();

    double getGenerationValue(int damage);

}
