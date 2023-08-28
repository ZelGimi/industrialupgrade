package com.denfop.api.space;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Planet implements IPlanet {

    private final String name;
    private final ISystem system;
    private final ResourceLocation textureLocation;
    private final EnumLevels levels;
    private final IStar star;
    private final double distance;
    private final boolean pressure;
    private final int temperature;
    private final EnumType type;
    private final boolean oxygen;
    private final boolean colonies;
    List<IBaseResource> baseResourceList;
    List<ISatellite> satelliteList;

    public Planet(
            String name, ISystem system, ResourceLocation textureLocation, EnumLevels levels, IStar star, int temperature
            , boolean pressure, double distance, EnumType type, boolean oxygen, boolean colonies
    ) {
        this.name = name;
        this.system = system;
        this.textureLocation = textureLocation;
        this.levels = levels;
        this.star = star;
        this.baseResourceList = new ArrayList<>();
        this.satelliteList = new ArrayList<>();
        this.distance = distance;
        this.pressure = pressure;
        this.temperature = temperature;
        this.type = type;
        this.oxygen = oxygen;
        this.colonies = colonies;
        SpaceNet.instance.addPlanet(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Planet asteroid = (Planet) o;
        return Objects.equals(name, asteroid.name);
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
        return this.textureLocation;
    }

    @Override
    public List<IBaseResource> getResources() {
        return this.baseResourceList;
    }

    @Override
    public EnumLevels getLevels() {
        return this.levels;
    }

    @Override
    public List<ISatellite> getSatelliteList() {
        return this.satelliteList;
    }

    @Override
    public IStar getStar() {
        return this.star;
    }

    @Override
    public int getTemperature() {
        return this.temperature;
    }

    @Override
    public boolean getPressure() {
        return this.pressure;
    }

    @Override
    public double getDistanceFromStar() {
        return this.distance;
    }

    @Override
    public EnumType getType() {
        return this.type;
    }

    @Override
    public boolean hasOxygen() {
        return this.oxygen;
    }

    @Override
    public boolean canHaveColonies() {
        return this.colonies;
    }

}
