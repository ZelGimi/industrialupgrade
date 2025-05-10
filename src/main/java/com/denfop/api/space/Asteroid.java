package com.denfop.api.space;


import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.denfop.IUCore.random;

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
    private final int angle;
    private final double time;
    private final double size;
    private final double rotation;
    private final double maxLocation;
    private final double minLocation;
    List<IBaseResource> baseResourceList;
    List<MiniAsteroid> miniAsteroids;

    public Asteroid(
            String name, ISystem system, ResourceLocation textureLocation, EnumLevels levels, IStar star, int temperature
            , double distance, EnumType type, boolean colonies, int angle, double time, double size, double rotation,
            double minLocation, double maxLocation, int amount
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
        this.angle = angle;
        this.time = time;
        this.size = size;
        this.rotation = rotation;
        this.minLocation = minLocation;
        this.maxLocation = maxLocation;
        this.miniAsteroids = new ArrayList<>();
        for (int i = 0; i < amount; i++) {

            float radius1 = (float) (minLocation + (maxLocation - minLocation) * random.nextFloat());

            float angle1 = (float) (2 * Math.PI * random.nextFloat());


            float asteroidSize1 = 0.005f + random.nextFloat() * 0.01f;


            float orbitalSpeed1 = 0.1f + random.nextFloat() * 0.4f;


            miniAsteroids.add(new MiniAsteroid(asteroidSize1, radius1, angle1, 0.5f, orbitalSpeed1));
        }
        SpaceNet.instance.addAsteroid(this);
    }

    @Override
    public ResourceLocation getLocation() {
        return textureLocation;
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
    public double getOrbitPeriod() {
        return time;
    }

    @Override
    public double getRotationTimeZ(final double time) {
        return 0;
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    public double getRotation(double time) {
        return time;
    }

    @Override
    public double getDistance() {
        return distance;
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
    public List<MiniAsteroid> getMiniAsteroid() {
        return miniAsteroids;
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
    public double getMinDistance() {
        return minLocation;
    }

    @Override
    public double getMaxDistance() {
        return maxLocation;
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
