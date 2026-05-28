package com.denfop.api.space.dimension.worldgen.feature.asteroid;

import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.denfop.api.space.dimension.SpaceOreEntry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public final class AsteroidOreRegistry {

    private static final Map<String, List<AsteroidOreDefinition>> DEFAULTS = new HashMap<>();
    private static final Map<String, List<AsteroidOreDefinition>> RELOADED = new HashMap<>();

    private AsteroidOreRegistry() {
    }

    public static List<AsteroidOreDefinition> getDefinitions(final String bodyName) {
        final String normalized = normalize(bodyName);
        final List<AsteroidOreDefinition> merged = new ArrayList<>(defaults(normalized));
        merged.addAll(RELOADED.getOrDefault(normalized, List.of()));
        merged.sort(Comparator.comparingInt(AsteroidOreDefinition::weight).reversed());
        return List.copyOf(merged);
    }

    public static void applyReload(final Map<ResourceLocation, JsonElement> jsonMap) {
        RELOADED.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                continue;
            }

            final JsonObject json = entry.getValue().getAsJsonObject();
            final String body = normalize(GsonHelper.getAsString(json, "body", "asteroid"));
            final String blockId = GsonHelper.getAsString(json, "block", "minecraft:air");
            final Optional<BlockState> state = resolveBlock(blockId);
            if (state.isEmpty() || state.get().isAir()) {
                continue;
            }

            final int weight = Math.max(1, GsonHelper.getAsInt(json, "weight", 20));
            final int minVein = Math.max(1, GsonHelper.getAsInt(json, "min_vein_size", 3));
            final int maxVein = Math.max(minVein, GsonHelper.getAsInt(json, "max_vein_size", Math.max(minVein, minVein + 4)));
            final int rarity = Math.max(1, GsonHelper.getAsInt(json, "rarity", 4));
            final float minDepth = clamp01(GsonHelper.getAsFloat(json, "min_depth", 0.18F));
            final float maxDepth = clamp01(GsonHelper.getAsFloat(json, "max_depth", 0.92F));
            final DepositShape shape = DepositShape.byName(GsonHelper.getAsString(json, "spawn_shape", "cluster"));
            final EnumSet<AsteroidMaterialType> allowedTypes = readTypes(json.getAsJsonArray("allowed_types"));

            RELOADED.computeIfAbsent(body, key -> new ArrayList<>()).add(new AsteroidOreDefinition(
                    entry.getKey(),
                    state.get(),
                    weight,
                    minVein,
                    maxVein,
                    rarity,
                    Math.min(minDepth, maxDepth),
                    Math.max(minDepth, maxDepth),
                    shape,
                    allowedTypes.isEmpty() ? EnumSet.of(AsteroidMaterialType.ROCKY, AsteroidMaterialType.MIXED, AsteroidMaterialType.ORE_RICH) : allowedTypes
            ));
        }
    }

    private static List<AsteroidOreDefinition> defaults(final String bodyName) {
        return DEFAULTS.computeIfAbsent(bodyName, AsteroidOreRegistry::buildDefaults);
    }

    private static List<AsteroidOreDefinition> buildDefaults(final String bodyName) {
        final SpaceDimensionProfile profile = SpaceBodyProfiles.byName(bodyName);
        final List<AsteroidOreDefinition> defaults = new ArrayList<>();
        int index = 0;
        for (SpaceOreEntry entry : profile.ores()) {
            final EnumSet<AsteroidMaterialType> allowedTypes = entry.weight() >= 55
                    ? EnumSet.of(AsteroidMaterialType.METALLIC, AsteroidMaterialType.MIXED, AsteroidMaterialType.ORE_RICH, AsteroidMaterialType.SPECIAL)
                    : EnumSet.of(AsteroidMaterialType.ROCKY, AsteroidMaterialType.MIXED, AsteroidMaterialType.ORE_RICH, AsteroidMaterialType.POROUS);
            defaults.add(new AsteroidOreDefinition(
                    ResourceLocation.tryBuild(bodyName, "default_" + (index++)),
                    entry.state(),
                    Math.max(1, entry.weight()),
                    Math.max(2, entry.veinSize() - 2),
                    Math.max(3, entry.veinSize() + 2),
                    Math.max(1, 7 - Math.min(6, entry.weight() / 18)),
                    entry.weight() >= 55 ? 0.08F : 0.18F,
                    entry.weight() >= 55 ? 0.70F : 0.96F,
                    entry.weight() >= 65 ? DepositShape.CORE : (entry.weight() <= 12 ? DepositShape.SCATTER : DepositShape.VEIN),
                    allowedTypes
            ));
        }
        return Collections.unmodifiableList(defaults);
    }

    private static Optional<BlockState> resolveBlock(final String blockId) {
        final ResourceLocation id = ResourceLocation.tryParse(blockId);
        if (id == null) {
            return Optional.empty();
        }
        return BuiltInRegistries.BLOCK.getOptional(id).map(Block::defaultBlockState);
    }

    private static EnumSet<AsteroidMaterialType> readTypes(final JsonArray array) {
        if (array == null || array.isEmpty()) {
            return EnumSet.noneOf(AsteroidMaterialType.class);
        }
        final EnumSet<AsteroidMaterialType> set = EnumSet.noneOf(AsteroidMaterialType.class);
        for (JsonElement element : array) {
            if (element.isJsonPrimitive()) {
                set.add(AsteroidMaterialType.byName(element.getAsString()));
            }
        }
        return set;
    }

    private static float clamp01(final float value) {
        return Math.max(0.0F, Math.min(1.0F, value));
    }

    private static String normalize(final String name) {
        return name == null ? "asteroid" : name.toLowerCase(Locale.ROOT);
    }

    public enum DepositShape {
        CORE,
        VEIN,
        CLUSTER,
        SHELL,
        SCATTER;

        public static DepositShape byName(final String name) {
            if (name == null || name.isBlank()) {
                return CLUSTER;
            }
            final String normalized = name.trim().toUpperCase(Locale.ROOT).replace('-', '_');
            for (DepositShape value : values()) {
                if (value.name().equals(normalized)) {
                    return value;
                }
            }
            return CLUSTER;
        }
    }

    public record AsteroidOreDefinition(
            ResourceLocation id,
            BlockState state,
            int weight,
            int minVeinSize,
            int maxVeinSize,
            int rarity,
            float minDepth,
            float maxDepth,
            DepositShape shape,
            EnumSet<AsteroidMaterialType> allowedTypes
    ) {
        public boolean supports(final AsteroidMaterialType type) {
            return this.allowedTypes.contains(type);
        }
    }
}
