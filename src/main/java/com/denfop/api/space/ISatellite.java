package com.denfop.api.space;


import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ISatellite extends IBody {

    ResourceLocation getResource();

    List<IBaseResource> getResources();

    EnumLevels getLevels();

    IPlanet getPlanet();

    int getTemperature();

    boolean getPressure();

    double getDistanceFromPlanet();

    EnumType getType();

    boolean hasOxygen();

    boolean canHaveColonies();

}
