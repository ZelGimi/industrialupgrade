package com.denfop.screen.space;

import com.denfop.api.space.*;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpaceMapScene3D {

    private final IStar star;
    private final List<SpaceBodyNode> nodes = new ArrayList<>();

    public SpaceMapScene3D(IStar star) {
        this.star = star;
    }

    public IStar getStar() {
        return star;
    }

    public List<SpaceBodyNode> getNodes() {
        return nodes;
    }

    public void rebuild(long gameTime) {
        this.nodes.clear();

        SpaceBodyNode starNode = new SpaceBodyNode(
                star,
                SpaceBodyNode.Type.STAR,
                null,
                new Vec3(0, 0, 0),
                (float) (star.getSize() * 32),
                star.getLocation(),
                false,
                false,
                (float) 0,
                0.0f,
                0xFFFFFFFF
        );
        nodes.add(starNode);

        for (IPlanet planet : star.getPlanetList()) {
            Vec3 planetPos = computePlanetPosition(planet, gameTime);
            int orbitColor = resolveOrbitColor(planet.getPressure(), planet.hasOxygen());
            boolean hasRing = planet.getRing() != null;
            boolean horizontal = planet.getRing() == EnumRing.HORIZONTAL;

            SpaceBodyNode planetNode = new SpaceBodyNode(
                    planet,
                    SpaceBodyNode.Type.PLANET,
                    starNode,
                    planetPos,
                    (float) (planet.getSize() * 32f),
                    planet.getLocation(),
                    hasRing,
                    horizontal,
                    (float) planet.getRotation(gameTime),
                    (float) planet.getDistance(),
                    orbitColor
            );
            nodes.add(planetNode);

            for (ISatellite satellite : planet.getSatelliteList()) {
                Vec3 satellitePos = computeSatellitePosition(planet, satellite, gameTime);
                int satelliteOrbitColor = resolveOrbitColor(satellite.getPressure(), satellite.hasOxygen());

                SpaceBodyNode satelliteNode = new SpaceBodyNode(
                        satellite,
                        SpaceBodyNode.Type.SATELLITE,
                        planetNode,
                        satellitePos,
                        (float) satellite.getSize() * 0.05f,
                        satellite.getLocation(),
                        false,
                        false,
                        (float) satellite.getRotation(gameTime),
                        (float) (satellite.getDistance() * 0.8f),
                        satelliteOrbitColor
                );
                nodes.add(satelliteNode);
            }
        }

        for (IAsteroid asteroid : star.getAsteroidList()) {
            Vec3 asteroidPos = computeAsteroidCenter(asteroid, gameTime);
            SpaceBodyNode asteroidNode = new SpaceBodyNode(
                    asteroid,
                    SpaceBodyNode.Type.ASTEROID,
                    starNode,
                    asteroidPos,
                    Math.max(0.28f, (float) asteroid.getSize() * 0.08f),
                    asteroid.getLocation(),
                    false,
                    false,
                    (float) asteroid.getRotation(gameTime),
                    (float) ((asteroid.getMinDistance() + asteroid.getMaxDistance()) * 0.5),
                    0xFF9C9C9C
            );
            nodes.add(asteroidNode);
        }

        nodes.sort(Comparator.comparingDouble(n -> -n.getWorldPos().z));
    }

    private Vec3 computePlanetPosition(IPlanet planet, long gameTime) {
        double angle = 2.0 * Math.PI * (gameTime * planet.getOrbitPeriod()) / 400.0D;
        double distance = planet.getDistance() * 128;

        double x = distance * Math.cos(angle);
        double z = distance * Math.sin(angle);
        double y = 0.0D;

        return new Vec3(x, y, z);
    }

    private Vec3 computeSatellitePosition(IPlanet planet, ISatellite satellite, long gameTime) {
        Vec3 parent = computePlanetPosition(planet, gameTime);

        double angle = 2.0 * Math.PI * (gameTime * satellite.getOrbitPeriod()) / 400.0D;
        double dist = (satellite.getDistance() * 0.8) * 128;

        double x = dist * Math.cos(angle);
        double z = dist * Math.sin(angle);
        double y = 0.0D;

        return new Vec3(parent.x + x, parent.y + y, parent.z + z);
    }

    private Vec3 computeAsteroidCenter(IAsteroid asteroid, long gameTime) {
        if (asteroid.getMiniAsteroid().isEmpty()) {
            double mid = (asteroid.getMinDistance() + asteroid.getMaxDistance()) * 0.5 * 32;
            double angle = 2.0 * Math.PI * (gameTime * asteroid.getOrbitPeriod()) / 400.0D;
            return new Vec3(mid * Math.cos(angle), 0, mid * Math.sin(angle));
        }

        MiniAsteroid sample = asteroid.getMiniAsteroid().get(0);
        float currentAngle = sample.getY() + sample.getRotationSpeed() * gameTime * 0.01f;
        float x = sample.getX() * (float) Math.cos(currentAngle);
        float z = sample.getX() * (float) Math.sin(currentAngle);
        return new Vec3(x, Math.sin(currentAngle * 0.7f) * 0.75f, z);
    }

    private float inclination(IBody body) {
        int hash = Math.abs(body.getName().hashCode());
        return (hash % 25) * 0.08f + 0.15f;
    }

    private int resolveOrbitColor(boolean pressure, boolean oxygen) {
        if (pressure) {
            return 0xFFFF5050;
        }
        if (oxygen) {
            return 0xFF62FF62;
        }
        return 0xFF5EA2FF;
    }
}