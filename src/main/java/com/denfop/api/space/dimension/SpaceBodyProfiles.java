package com.denfop.api.space.dimension;

import com.denfop.IUCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class SpaceBodyProfiles {

    private static final Map<String, SpaceDimensionProfile> CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Optional<SpaceDimensionProfile>> DIMENSION_PATH_CACHE = new ConcurrentHashMap<>();

    private SpaceBodyProfiles() {
    }

    public static SpaceDimensionProfile byBody(final SpaceBodyRef body) {
        return CACHE.computeIfAbsent(body.name(), key -> create(body));
    }

    public static SpaceDimensionProfile byName(final String bodyName) {
        return byBody(SpaceBodyCatalog.byName(bodyName));
    }

    public static void invalidate() {
        CACHE.clear();
        DIMENSION_PATH_CACHE.clear();
    }

    private static SpaceDimensionProfile create(final SpaceBodyRef body) {
        SpaceGenerationMode mode = generationMode(body);
        SpaceTerrainPalette terrain = SpaceResourceIntrospector.resolveTerrain(body, mode);

        if (mode == SpaceGenerationMode.NONE) {
            return SpaceDimensionProfile.none(body, terrain, effectsId(body));
        }

        final boolean asteroidField = mode == SpaceGenerationMode.ASTEROID_FIELD || body.isAsteroid();
        float atmosphereDensity = asteroidField ? 0.0F : (body.pressure() ? 0.85F : (body.oxygen() ? 0.45F : 0.02F));
        boolean atmosphere = atmosphereDensity > 0.03F;
        Vec3 sky = switch (mode) {
            case VOLCANIC -> new Vec3(0.64D, 0.38D, 0.18D);
            case DUSTY_DESERT -> new Vec3(0.78D, 0.51D, 0.34D);
            case ICY_SHELL -> new Vec3(0.35D, 0.45D, 0.58D);
            case CRYO_CHEMICAL -> new Vec3(0.28D, 0.34D, 0.42D);
            case AIRLESS_BARREN, AIRLESS_CRATERED, ASTEROID_FIELD -> new Vec3(0.02D, 0.02D, 0.03D);
            default -> new Vec3(0.45D, 0.58D, 0.71D);
        };
        Vec3 fog = switch (mode) {
            case VOLCANIC -> new Vec3(0.55D, 0.29D, 0.18D);
            case DUSTY_DESERT -> new Vec3(0.70D, 0.46D, 0.28D);
            case ICY_SHELL -> new Vec3(0.65D, 0.72D, 0.80D);
            case CRYO_CHEMICAL -> new Vec3(0.42D, 0.51D, 0.56D);
            case AIRLESS_BARREN, AIRLESS_CRATERED, ASTEROID_FIELD -> new Vec3(0.01D, 0.01D, 0.02D);
            default -> new Vec3(0.55D, 0.62D, 0.70D);
        };

        float sunSize = asteroidField
                ? 0.28F
                : (float) Math.max(0.65D, Math.min(1.8D, 0.9D / Math.max(0.08D, body.distanceFromStar())));
        float starBrightness = atmosphere ? 0.35F : 1.0F;
        boolean drawSunrise = atmosphere && mode != SpaceGenerationMode.VOLCANIC && mode != SpaceGenerationMode.CRYO_CHEMICAL;

        return new SpaceDimensionProfile(
                body,
                mode,
                effectsId(body),
                net.minecraft.world.level.levelgen.NoiseGeneratorSettings.OVERWORLD,
                gravityFor(body, mode),
                fixedDayLength(body, mode),
                asteroidField ? 18000L : null,
                !asteroidField,
                true,
                false,
                false,
                false,
                false,
                true,
                false,
                -64,
                384,
                256,
                asteroidField ? -63 : seaLevel(mode),
                1.0D,
                asteroidField ? 0.0F : ambientLightFor(mode),
                asteroidField ? 0.0F : 128.0F,
                BlockTags.INFINIBURN_OVERWORLD,
                atmosphere,
                atmosphereDensity,
                sky,
                fog,
                4159204,
                329011,
                sunSize,
                starBrightness,
                true,
                drawSunrise,
                asteroidField ? 1.35F : fogFactor(mode),
                terrain,
                !asteroidField && mode != SpaceGenerationMode.NONE && mode != SpaceGenerationMode.VOLCANIC,
                !asteroidField && mode != SpaceGenerationMode.NONE,
                !asteroidField && !terrain.fluids().isEmpty(),
                !asteroidField && (mode == SpaceGenerationMode.VOLCANIC || mode == SpaceGenerationMode.CRYO_CHEMICAL || mode == SpaceGenerationMode.ICY_SHELL),
                !asteroidField && mode == SpaceGenerationMode.VOLCANIC,
                !asteroidField && mode != SpaceGenerationMode.NONE,
                !asteroidField && (mode == SpaceGenerationMode.AIRLESS_CRATERED || mode == SpaceGenerationMode.DUSTY_DESERT || mode == SpaceGenerationMode.VOLCANIC),
                !asteroidField && mode == SpaceGenerationMode.VOLCANIC,
                asteroidField,
                craterRarity(body, mode),
                volcanoRarity(body, mode),
                cavityRarity(mode),
                shaftRarity(mode),
                lavaTubeRarity(mode),
                null,
                null
        );
    }

    private static net.minecraft.resources.ResourceLocation effectsId(final SpaceBodyRef body) {
        return ResourceLocation.tryBuild(IUCore.MODID, body.name().toLowerCase(Locale.ROOT));
    }

    private static SpaceGenerationMode generationMode(final SpaceBodyRef body) {
        return switch (body.name()) {
            case "earth", "jupiter", "saturn", "uranus", "neptune" -> SpaceGenerationMode.NONE;
            case "venus", "io", "mercury" -> SpaceGenerationMode.VOLCANIC;
            case "mars" -> SpaceGenerationMode.DUSTY_DESERT;
            case "asteroid", "asteroids" -> SpaceGenerationMode.ASTEROID_FIELD;
            case "moon", "phobos", "deimos", "charon", "proteus", "ceres" -> SpaceGenerationMode.AIRLESS_CRATERED;
            case "europe", "europa", "ganymede", "callisto", "enceladus", "rhea", "tethys", "titania", "oberon", "ariel" ->
                    SpaceGenerationMode.ICY_SHELL;
            case "titan", "dione", "mimas", "umbriel", "miranda", "triton", "pluto", "eris", "makemake", "haumea" ->
                    SpaceGenerationMode.CRYO_CHEMICAL;
            default -> body.isAsteroid()
                    ? SpaceGenerationMode.ASTEROID_FIELD
                    : (body.isSatellite() ? SpaceGenerationMode.AIRLESS_BARREN : SpaceGenerationMode.ASTEROID_FIELD);
        };
    }

    private static float gravityFor(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        if (mode == SpaceGenerationMode.ASTEROID_FIELD || body.isAsteroid()) {
            return 0.035F;
        }
        return (float) Math.max(0.08D, Math.min(1.1D, body.size() * 2.8D));
    }

    private static long fixedDayLength(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        if (mode == SpaceGenerationMode.ASTEROID_FIELD || body.isAsteroid()) {
            return 24000L;
        }
        return (long) Math.max(18000.0D, 24000.0D * (0.65D + body.orbitPeriod()));
    }

    private static int seaLevel(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> -16;
            case ICY_SHELL, CRYO_CHEMICAL -> 16;
            case NONE -> -63;
            default -> 0;
        };
    }

    private static float ambientLightFor(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> 0.08F;
            case CRYO_CHEMICAL -> 0.02F;
            case AIRLESS_BARREN, AIRLESS_CRATERED, ASTEROID_FIELD -> 0.0F;
            default -> 0.04F;
        };
    }

    private static float fogFactor(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> 0.35F;
            case DUSTY_DESERT -> 0.42F;
            case ICY_SHELL, CRYO_CHEMICAL -> 0.62F;
            default -> 0.95F;
        };
    }

    private static int craterRarity(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        return switch (mode) {
            case AIRLESS_CRATERED -> 5;
            case AIRLESS_BARREN -> 8;
            case DUSTY_DESERT -> 10;
            case ICY_SHELL -> 14;
            case VOLCANIC -> 18;
            default -> Integer.MAX_VALUE;
        };
    }

    private static int volcanoRarity(final SpaceBodyRef body, final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> body.name().equals("io") ? 8 : body.name().equals("venus") ? 12 : 16;
            default -> Integer.MAX_VALUE;
        };
    }

    private static int cavityRarity(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> 6;
            case DUSTY_DESERT -> 8;
            case ICY_SHELL, CRYO_CHEMICAL -> 9;
            case AIRLESS_CRATERED, AIRLESS_BARREN, ASTEROID_FIELD -> 11;
            default -> Integer.MAX_VALUE;
        };
    }

    private static int shaftRarity(final SpaceGenerationMode mode) {
        return switch (mode) {
            case AIRLESS_CRATERED, DUSTY_DESERT -> 9;
            case VOLCANIC -> 11;
            default -> Integer.MAX_VALUE;
        };
    }

    private static int lavaTubeRarity(final SpaceGenerationMode mode) {
        return switch (mode) {
            case VOLCANIC -> 7;
            default -> Integer.MAX_VALUE;
        };
    }

    @Nullable
    public static SpaceDimensionProfile byDimensionPath(final String path) {
        if (path == null || path.isBlank()) {
            return null;
        }

        final String normalized = normalizeDimensionPath(path);

        return DIMENSION_PATH_CACHE.computeIfAbsent(normalized, key -> {
            try {
                return Optional.of(SpaceBodyProfiles.byBody(SpaceBodyCatalog.byName(key)));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }).orElse(null);
    }

    private static String normalizeDimensionPath(final String rawPath) {
        String path = rawPath.trim().toLowerCase(Locale.ROOT);

        int colon = path.indexOf(':');
        if (colon >= 0 && colon + 1 < path.length()) {
            path = path.substring(colon + 1);
        }

        path = path.replace('\\', '/');

        if (path.contains("/")) {
            path = path.substring(path.lastIndexOf('/') + 1);
        }

        if (path.endsWith(".json")) {
            path = path.substring(0, path.length() - 5);
        }

        return path;
    }
}
