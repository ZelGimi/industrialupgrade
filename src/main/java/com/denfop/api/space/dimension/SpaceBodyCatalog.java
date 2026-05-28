package com.denfop.api.space.dimension;

import com.denfop.api.space.Asteroid;
import com.denfop.api.space.Planet;
import com.denfop.api.space.Satellite;
import com.denfop.api.space.SpaceInit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SpaceBodyCatalog {

    private static volatile List<SpaceBodyRef> cache;

    private SpaceBodyCatalog() {
    }

    public static List<SpaceBodyRef> allBodies() {
        List<SpaceBodyRef> local = cache;
        if (local != null) {
            return local;
        }
        synchronized (SpaceBodyCatalog.class) {
            if (cache != null) {
                return cache;
            }
            SpaceBootstrap.ensureSpaceCatalogInitialized();
            List<SpaceBodyRef> result = new ArrayList<>();
            for (Field field : SpaceInit.class.getDeclaredFields()) {
                try {
                    Object value = field.get(null);

                    if (value instanceof Planet planet) {
                        result.add(new SpaceBodyRef(
                                planet.getName(),
                                SpaceBodyRef.Kind.PLANET,
                                planet.getSystem().getName(),
                                planet.getStar().getName(),
                                null,
                                planet.getLocation(),
                                planet.getTemperature(),
                                planet.getPressure(),
                                planet.hasOxygen(),
                                planet.canHaveColonies(),
                                planet.getSize(),
                                planet.getDistance(),
                                planet.getDistanceFromStar(),
                                planet.getRotationAngle(),
                                planet.getOrbitPeriod(),
                                planet.getType(),
                                planet.getRing()
                        ));
                    } else if (value instanceof Satellite satellite) {
                        result.add(new SpaceBodyRef(
                                satellite.getName(),
                                SpaceBodyRef.Kind.SATELLITE,
                                satellite.getSystem().getName(),
                                satellite.getPlanet().getStar().getName(),
                                satellite.getPlanet().getName(),
                                satellite.getLocation(),
                                satellite.getTemperature(),
                                satellite.getPressure(),
                                satellite.hasOxygen(),
                                satellite.canHaveColonies(),
                                satellite.getSize(),
                                satellite.getDistance(),
                                satellite.getPlanet().getDistanceFromStar(),
                                satellite.getRotationAngle(),
                                satellite.getOrbitPeriod(),
                                satellite.getType(),
                                null
                        ));
                    } else if (value instanceof Asteroid asteroid) {
                        final double minDistance = asteroid.getMinDistance();
                        final double maxDistance = asteroid.getMaxDistance();
                        final double orbitDistance = minDistance + (maxDistance - minDistance) * 0.5D;
                        final double distanceFromStar = asteroid.getDistanceFromStar() > 0.0D
                                ? asteroid.getDistanceFromStar()
                                : Math.max(0.2D, orbitDistance);

                        result.add(new SpaceBodyRef(
                                asteroid.getName(),
                                SpaceBodyRef.Kind.ASTEROID,
                                asteroid.getSystem().getName(),
                                asteroid.getStar().getName(),
                                null,
                                asteroid.getLocation(),
                                asteroid.getTemperature(),
                                false,
                                false,
                                asteroid.canHaveColonies(),
                                Math.max(0.08D, asteroid.getSize()),
                                orbitDistance,
                                distanceFromStar,
                                asteroid.getRotationAngle(),
                                Math.max(0.35D, asteroid.getOrbitPeriod()),
                                asteroid.getType(),
                                null
                        ));
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
            result.sort(Comparator.comparing(SpaceBodyRef::systemName).thenComparingDouble(SpaceBodyRef::distanceFromStar));
            cache = List.copyOf(result);
            return cache;
        }
    }

    public static SpaceBodyRef byName(final String name) {
        return allBodies().stream()
                .filter(body -> body.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown space body: " + name));
    }

    static void invalidate() {
        cache = null;
    }
}
