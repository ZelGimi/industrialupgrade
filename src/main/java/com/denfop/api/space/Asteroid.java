package com.denfop.api.space;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Asteroid implements IAsteroid {

    private final String name;
    private final ISystem system;
    private final ResourceLocation textureLocation;
    private final EnumLevels levels;
    private final IStar star;
    private final double distance;
    private final int temperature;
    private final EnumType type;
    private final boolean colonies;
    List<IBaseResource> baseResourceList;

    public Asteroid(
            String name, ISystem system, ResourceLocation textureLocation, EnumLevels levels, IStar star, int temperature
            , double distance, EnumType type, boolean colonies
    ) {
        this.name = name;
        this.system = system;
        this.textureLocation = textureLocation;
        this.levels = levels;
        this.star = star;
        this.baseResourceList = new ArrayList<>();
        this.distance = distance;
        this.temperature = temperature;
        this.type = type;
        this.colonies = colonies;
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
    public IStar getStar() {
        return this.star;
    }

    @Override
    public int getTemperature() {
        return this.temperature;
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
    public boolean canHaveColonies() {
        return this.colonies;
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Asteroid asteroid = (Asteroid) o;
        return Objects.equals(name, asteroid.name);
    }


}
