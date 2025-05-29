package com.denfop.api.space;


import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IAsteroid extends IBody {

    ResourceLocation getResource();

    List<IBaseResource> getResources();

    List<MiniAsteroid> getMiniAsteroid();

    EnumLevels getLevels();

    IStar getStar();

    int getTemperature();

    double getDistanceFromStar();

    EnumType getType();

    boolean canHaveColonies();

    double getMinDistance();

    double getMaxDistance();

}
