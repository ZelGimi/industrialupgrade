package com.denfop.items.space.teleport;

import com.denfop.api.space.*;

public final class SpaceTeleportEnergyLogic {

    public static final double DEFAULT_MAX_CHARGE = 2_000_000D;
    public static final double DEFAULT_CRITICAL_CHARGE = 150_000D;

    private static final double MOON_MAX_STAY_SECONDS = 2.5D * 60D * 60D;
    private static final double MARS_MAX_STAY_SECONDS = 2.0D * 60D * 60D;
    private static final double FAR_SOLAR_MAX_STAY_SECONDS = 15D * 60D;
    private static final double DEEP_SYSTEM_MIN_STAY_SECONDS = 5D * 60D;

    private static final double FALLBACK_EARTH_DISTANCE = 4.1D;
    private static final double FALLBACK_MARS_DISTANCE = 3.3D;
    private static final double FALLBACK_MOON_DISTANCE = 1.1D;

    private static final double MAX_SOLAR_TELEPORT_COST = 170_000D;
    private static final double MAX_DEEP_SYSTEM_TELEPORT_COST = 320_000D;

    private SpaceTeleportEnergyLogic() {
    }

    public static double getDistanceUnits(final IBody body) {
        return getLocalDistanceUnits(body) + getSystemDistanceUnits(body);
    }

    public static double getLocalDistanceUnits(final IBody body) {
        if (body == null) {
            return 0.0D;
        }

        final double earthOrbit = resolveEarthOrbit();
        final double marsDelta = resolveMarsDelta();
        final double moonDelta = resolveMoonDistance();

        if (body instanceof IPlanet planet) {
            return Math.abs(planet.getDistance() - earthOrbit) / marsDelta;
        }

        if (body instanceof ISatellite satellite) {
            double planetUnits = Math.abs(satellite.getPlanet().getDistance() - earthOrbit) / marsDelta;
            double satelliteUnits = Math.abs(satellite.getDistanceFromPlanet()) / moonDelta * 0.45D;
            return planetUnits + satelliteUnits;
        }

        if (body instanceof IAsteroid asteroid) {
            double avgDistance = ((asteroid.getMaxDistance() - asteroid.getMinDistance()) * 0.5D) + asteroid.getMinDistance();
            return Math.abs(avgDistance - earthOrbit) / marsDelta;
        }

        return 0.0D;
    }

    public static double getSystemDistanceUnits(final IBody body) {
        if (body == null || body.getSystem() == null || isSolarBody(body)) {
            return 0.0D;
        }

        double solarOffset = Math.max(0.0D, body.getSystem().getDistanceFromSolar());
        return Math.log1p(solarOffset) * 0.55D;
    }

    public static double getDesiredStaySeconds(final IBody body) {
        if (body == null) {
            return MOON_MAX_STAY_SECONDS;
        }

        final double localUnits = getLocalDistanceUnits(body);
        final double systemUnits = getSystemDistanceUnits(body);

        final double moonUnits = getMoonUnits();
        final double marsUnits = getMarsUnits();
        final double farSolarUnits = getFurthestSolarUnits();

        final double solarStaySeconds;

        if (localUnits <= moonUnits) {
            solarStaySeconds = MOON_MAX_STAY_SECONDS;
        } else if (localUnits <= marsUnits) {
            double t = normalize(localUnits, moonUnits, marsUnits);
            solarStaySeconds = lerp(MOON_MAX_STAY_SECONDS, MARS_MAX_STAY_SECONDS, t);
        } else if (localUnits <= farSolarUnits) {
            double t = normalize(localUnits, marsUnits, farSolarUnits);
            solarStaySeconds = lerp(MARS_MAX_STAY_SECONDS, FAR_SOLAR_MAX_STAY_SECONDS, Math.pow(t, 1.25D));
        } else {
            solarStaySeconds = FAR_SOLAR_MAX_STAY_SECONDS;
        }

        if (systemUnits <= 0.0D) {
            return solarStaySeconds;
        }

        double externalPenalty = 1.0D + systemUnits * 0.85D;
        return clamp(solarStaySeconds / externalPenalty, DEEP_SYSTEM_MIN_STAY_SECONDS, MOON_MAX_STAY_SECONDS);
    }

    public static double getActiveDrainPerSecond(
            final IBody body,
            final double maxCharge,
            final double criticalCharge
    ) {
        double usableEnergy = Math.max(1.0D, maxCharge - criticalCharge);
        double staySeconds = Math.max(1.0D, getDesiredStaySeconds(body));
        return Math.ceil(usableEnergy / staySeconds);
    }

    public static long estimateRemainingTicks(
            final double currentCharge,
            final IBody body,
            final double maxCharge,
            final double criticalCharge
    ) {
        if (currentCharge <= criticalCharge) {
            return 0L;
        }

        double drainPerSecond = getActiveDrainPerSecond(body, maxCharge, criticalCharge);
        if (drainPerSecond <= 0.0D) {
            return 0L;
        }

        double seconds = (currentCharge - criticalCharge) / drainPerSecond;
        return Math.max(0L, (long) Math.floor(seconds * 20.0D));
    }

    public static double getTeleportCost(final IBody body, final double baseCost) {
        double minCost = Math.max(1.0D, baseCost);
        double normalized = clamp(getLocalDistanceUnits(body) / Math.max(1.0D, getFurthestSolarUnits()), 0.0D, 1.0D);

        double solarCost = minCost + (MAX_SOLAR_TELEPORT_COST - minCost) * Math.pow(normalized, 1.15D);
        double extraSystemCost = getSystemDistanceUnits(body) * 18_000D;

        return Math.ceil(clamp(solarCost + extraSystemCost, minCost, MAX_DEEP_SYSTEM_TELEPORT_COST));
    }

    public static double getReturnCost(final IBody body, final double baseReturnCost) {
        return Math.ceil(getTeleportCost(body, Math.max(1.0D, baseReturnCost)) * 0.55D);
    }

    public static double getDistanceMultiplier(final IBody body) {
        double moonDrain = getActiveDrainPerSecond(SpaceInit.moon, DEFAULT_MAX_CHARGE, DEFAULT_CRITICAL_CHARGE);
        double currentDrain = getActiveDrainPerSecond(body, DEFAULT_MAX_CHARGE, DEFAULT_CRITICAL_CHARGE);
        return clamp(currentDrain / Math.max(1.0D, moonDrain), 1.0D, 25.0D);
    }

    private static boolean isSolarBody(final IBody body) {
        return body != null
                && body.getSystem() != null
                && body.getSystem().getName() != null
                && body.getSystem().getName().equalsIgnoreCase("solarsystem");
    }

    private static double getMoonUnits() {
        return Math.max(0.05D, getLocalDistanceUnits(SpaceInit.moon));
    }

    private static double getMarsUnits() {
        return Math.max(getMoonUnits() + 0.01D, getLocalDistanceUnits(SpaceInit.mars));
    }

    private static double getFurthestSolarUnits() {
        double max = getMarsUnits();

        max = Math.max(max, getLocalDistanceUnits(SpaceInit.mercury));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.venus));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.jupiter));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.saturn));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.uranus));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.neptune));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.pluto));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.eris));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.makemake));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.haumea));
        max = Math.max(max, getLocalDistanceUnits(SpaceInit.asteroids));

        return Math.max(max, getMarsUnits() + 0.01D);
    }

    private static double resolveEarthOrbit() {
        return SpaceInit.earth != null ? SpaceInit.earth.getDistance() : FALLBACK_EARTH_DISTANCE;
    }

    private static double resolveMarsDelta() {
        if (SpaceInit.earth != null && SpaceInit.mars != null) {
            return Math.max(0.0001D, Math.abs(SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance()));
        }
        return Math.max(0.0001D, Math.abs(FALLBACK_MARS_DISTANCE - FALLBACK_EARTH_DISTANCE));
    }

    private static double resolveMoonDistance() {
        if (SpaceInit.moon != null) {
            return Math.max(0.0001D, Math.abs(SpaceInit.moon.getDistanceFromPlanet()));
        }
        return Math.max(0.0001D, FALLBACK_MOON_DISTANCE);
    }

    private static double lerp(final double a, final double b, final double t) {
        return a + (b - a) * clamp(t, 0.0D, 1.0D);
    }

    private static double normalize(final double value, final double min, final double max) {
        if (max <= min) {
            return 0.0D;
        }
        return clamp((value - min) / (max - min), 0.0D, 1.0D);
    }

    private static double clamp(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }
}