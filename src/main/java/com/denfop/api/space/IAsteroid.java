package com.denfop.api.space;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IAsteroid extends IBody {

    ResourceLocation getResource();

    List<IBaseResource> getResources();

    EnumLevels getLevels();

    IStar getStar();

    int getTemperature();

    double getDistanceFromStar();

    EnumType getType();

    boolean canHaveColonies();

}
