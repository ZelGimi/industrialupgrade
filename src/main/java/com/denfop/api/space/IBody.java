package com.denfop.api.space;


import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IBody {

    String getName();

    ISystem getSystem();

    ResourceLocation getLocation();

    double getDistance();

    int getRotationAngle();

    double getRotationTimeX(double time);

    double getOrbitPeriod();

    double getRotationTimeZ(double time);

    double getSize();

    double getRotation(double time);

    int getTemperature();

    List<IBaseResource> getResources();
}
