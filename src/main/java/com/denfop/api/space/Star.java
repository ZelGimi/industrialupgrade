package com.denfop.api.space;


import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Star implements IStar {

    private final String name;
    private final ISystem system;
    private final ResourceLocation location;
    private final int angle;
    private final double size;
    List<IPlanet> planetList;
    List<IAsteroid> asteroidList;

    public Star(String name, ISystem system, ResourceLocation location, int angle, double size) {
        this.name = name;
        this.system = system;
        this.location = location;
        this.planetList = new ArrayList<>();
        this.asteroidList = new ArrayList<>();
        this.angle = angle;
        this.size = size;
        SpaceNet.instance.addStar(this);
    }

    @Override
    public List<IAsteroid> getAsteroidList() {
        return asteroidList;
    }

    @Override
    public double getOrbitPeriod() {
        return 0;
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    public double getRotation(final double time) {
        return time;
    }

    @Override
    public int getTemperature() {
        return 1000000;
    }

    @Override
    public List<IBaseResource> getResources() {
        return Collections.emptyList();
    }

    @Override
    public double getDistance() {
        return 0;
    }

    @Override
    public int getRotationAngle() {
        return angle;
    }

    @Override
    public double getRotationTimeX(final double time) {
        return 0;
    }

    @Override
    public double getRotationTimeZ(final double time) {
        return 0;
    }

    @Override
    public ResourceLocation getLocation() {
        return location;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ISystem getSystem() {
        return this.system;
    }


    @Override
    public ResourceLocation getResource() {
        return this.location;
    }

    @Override
    public List<IPlanet> getPlanetList() {
        return this.planetList;
    }

}
