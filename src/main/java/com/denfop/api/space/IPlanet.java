package com.denfop.api.space;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IPlanet extends IBody {

    ResourceLocation getResource();


    EnumLevels getLevels();

    List<ISatellite> getSatelliteList();

    IStar getStar();

    int getTemperature();

    boolean getPressure();

    double getDistanceFromStar();

    EnumType getType();

    boolean hasOxygen();

    boolean canHaveColonies();

    EnumRing getRing();

}
