package com.denfop.api.space.dimension.worldgen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public record SpacePlanetTraits(
        ResourceLocation dimensionId,
        double gravity,
        double temperature,
        double atmosphere,
        double tectonics,
        double volatility,
        double mineralRichness,
        double anomaly
) {
    public static SpacePlanetTraits from(WorldGenLevel level) {
        return from(detectBodyDefinition(level), level.getLevel().dimension().location());
    }

    public static SpacePlanetTraits from(Level level) {
        return from(detectBodyDefinition(level), level.dimension().location());
    }

    public static SpacePlanetTraits from(Object candidate, ResourceLocation dimensionId) {
        if (candidate == null) {
            return heuristic(dimensionId);
        }

        double gravity = extractDouble(candidate, 1.0D, "gravity", "gravitation");
        double temperature = extractDouble(candidate, 0.0D, "temperature", "heat", "thermal");
        double atmosphere = extractDouble(candidate, 0.45D, "atmosphere", "pressure", "air");
        double tectonics = extractDouble(candidate, 0.35D, "tectonic", "seismic", "fracture", "activity");
        double volatility = extractDouble(candidate, 0.30D, "volatility", "volcanic", "instability", "activity");
        double minerals = extractDouble(candidate, 0.50D, "mineral", "ore", "richness");
        double anomaly = extractDouble(candidate, 0.20D, "anomaly", "strange", "artifact", "distortion");

        return new SpacePlanetTraits(dimensionId, gravity, temperature, atmosphere, tectonics, volatility, minerals, anomaly);
    }

    private static Object detectBodyDefinition(WorldGenLevel level) {
        ResourceLocation id = level.getLevel().dimension().location();

        try {
            Class<?> registryClass = Class.forName("dimension.SpaceBodyDefinitionRegistry");
            for (Method method : registryClass.getDeclaredMethods()) {
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                String name = method.getName().toLowerCase(Locale.ROOT);
                if (!(name.contains("get") || name.contains("find") || name.contains("resolve") || name.contains("lookup"))) {
                    continue;
                }

                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && params[0] == ResourceLocation.class) {
                    method.setAccessible(true);
                    Object result = method.invoke(null, id);
                    if (result != null) {
                        return result;
                    }
                }

                if (params.length == 1 && params[0] == String.class) {
                    method.setAccessible(true);
                    Object result = method.invoke(null, id.toString());
                    if (result != null) {
                        return result;
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        return null;
    }

    private static Object detectBodyDefinition(Level level) {
        ResourceLocation id = level.dimension().location();

        try {
            Class<?> registryClass = Class.forName("dimension.SpaceBodyDefinitionRegistry");
            for (Method method : registryClass.getDeclaredMethods()) {
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                String name = method.getName().toLowerCase(Locale.ROOT);
                if (!(name.contains("get") || name.contains("find") || name.contains("resolve") || name.contains("lookup"))) {
                    continue;
                }

                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && params[0] == ResourceLocation.class) {
                    method.setAccessible(true);
                    Object result = method.invoke(null, id);
                    if (result != null) {
                        return result;
                    }
                }

                if (params.length == 1 && params[0] == String.class) {
                    method.setAccessible(true);
                    Object result = method.invoke(null, id.toString());
                    if (result != null) {
                        return result;
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        return null;
    }

    private static double extractDouble(Object candidate, double fallback, String... nameFragments) {
        for (Method method : candidate.getClass().getMethods()) {
            if (method.getParameterCount() != 0) {
                continue;
            }

            Class<?> returnType = method.getReturnType();
            if (!(returnType == double.class || returnType == Double.class || returnType == float.class || returnType == Float.class
                    || returnType == int.class || returnType == Integer.class)) {
                continue;
            }

            String methodName = method.getName().toLowerCase(Locale.ROOT);
            for (String fragment : nameFragments) {
                if (methodName.contains(fragment.toLowerCase(Locale.ROOT))) {
                    try {
                        Object value = method.invoke(candidate);
                        return normalize(((Number) value).doubleValue(), fragment);
                    } catch (Throwable ignored) {
                    }
                }
            }
        }

        for (Field field : candidate.getClass().getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (!(fieldType == double.class || fieldType == Double.class || fieldType == float.class || fieldType == Float.class
                    || fieldType == int.class || fieldType == Integer.class)) {
                continue;
            }

            String fieldName = field.getName().toLowerCase(Locale.ROOT);
            for (String fragment : nameFragments) {
                if (fieldName.contains(fragment.toLowerCase(Locale.ROOT))) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(candidate);
                        return normalize(((Number) value).doubleValue(), fragment);
                    } catch (Throwable ignored) {
                    }
                }
            }
        }

        return fallback;
    }

    private static double normalize(double value, String fragment) {
        String key = fragment.toLowerCase(Locale.ROOT);
        if (key.contains("temperature") || key.contains("heat") || key.contains("thermal")) {
            if (value > 2.0D || value < -2.0D) {
                return Math.max(-1.0D, Math.min(1.0D, value / 300.0D));
            }
            return value;
        }

        if (key.contains("gravity")) {
            if (value > 10.0D) {
                return value / 9.81D;
            }
            return value;
        }

        if (value > 1.0D) {
            return Math.min(1.0D, value / 100.0D);
        }
        if (value < 0.0D) {
            return 0.0D;
        }
        return value;
    }

    private static SpacePlanetTraits heuristic(ResourceLocation dimensionId) {
        String path = dimensionId.toString().toLowerCase(Locale.ROOT);

        double gravity = 1.0D;
        double temperature = 0.0D;
        double atmosphere = 0.45D;
        double tectonics = 0.35D;
        double volatility = 0.30D;
        double mineralRichness = 0.50D;
        double anomaly = 0.20D;

        List<String> lowGravityHints = List.of("moon", "asteroid", "phobos", "deimos", "europa");
        List<String> cryoHints = List.of("ice", "frost", "frozen", "europa", "enceladus", "triton");
        List<String> hotHints = List.of("lava", "inferno", "io", "volcan", "sulfur", "mercury");
        List<String> thinHints = List.of("moon", "asteroid", "vacuum");
        List<String> anomalyHints = List.of("anomaly", "alien", "artifact", "warp", "void");

        for (String hint : lowGravityHints) {
            if (path.contains(hint)) {
                gravity = 0.55D;
                atmosphere = 0.15D;
            }
        }
        for (String hint : cryoHints) {
            if (path.contains(hint)) {
                temperature = -0.85D;
                atmosphere = Math.max(atmosphere, 0.35D);
            }
        }
        for (String hint : hotHints) {
            if (path.contains(hint)) {
                temperature = 0.95D;
                tectonics = 0.70D;
                volatility = 0.80D;
            }
        }
        for (String hint : thinHints) {
            if (path.contains(hint)) {
                atmosphere = 0.05D;
            }
        }
        for (String hint : anomalyHints) {
            if (path.contains(hint)) {
                anomaly = 0.85D;
            }
        }

        if (path.contains("gas")) {
            atmosphere = 1.0D;
            mineralRichness = 0.15D;
        }
        if (path.contains("metal") || path.contains("iron")) {
            mineralRichness = 0.9D;
            gravity = Math.max(gravity, 1.25D);
        }

        return new SpacePlanetTraits(dimensionId, gravity, temperature, atmosphere, tectonics, volatility, mineralRichness, anomaly);
    }

    public boolean lowGravity() {
        return this.gravity < 0.8D;
    }

    public boolean heavyGravity() {
        return this.gravity > 1.25D;
    }

    public boolean cryogenic() {
        return this.temperature < -0.2D;
    }

    public boolean volcanic() {
        return this.temperature > 0.75D || this.volatility > 0.65D;
    }

    public boolean thinAtmosphere() {
        return this.atmosphere < 0.35D;
    }

    public boolean tectonicallyActive() {
        return this.tectonics > 0.55D;
    }
}
