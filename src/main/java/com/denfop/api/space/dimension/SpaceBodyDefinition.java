package com.denfop.api.space.dimension;

import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public final class SpaceBodyDefinition {

    private final Map<String, BlockState> layers = new HashMap<>();
    private final List<SpaceOreEntry> ores = new ArrayList<>();
    private final List<SpaceFluidPocket> fluids = new ArrayList<>();

    private SpaceBodyDefinition() {
    }

    public static SpaceBodyDefinition create() {
        return new SpaceBodyDefinition();
    }

    public SpaceBodyDefinition layer(final String key, final BlockState state) {
        if (key != null && state != null) {
            this.layers.put(key, state);
        }
        return this;
    }

    public BlockState getLayer(final String key) {
        return this.layers.get(key);
    }

    public BlockState getLayerOrDefault(final String key, final BlockState fallback) {
        final BlockState state = this.layers.get(key);
        return state != null ? state : fallback;
    }

    public Map<String, BlockState> getLayers() {
        return Collections.unmodifiableMap(this.layers);
    }

    public SpaceBodyDefinition ore(final SpaceOreEntry entry) {
        if (entry != null) {
            this.ores.add(entry);
        }
        return this;
    }

    public SpaceBodyDefinition fluid(final SpaceFluidPocket entry) {
        if (entry != null) {
            this.fluids.add(entry);
        }
        return this;
    }

    public List<SpaceOreEntry> getOres() {
        return List.copyOf(this.ores);
    }

    public List<SpaceFluidPocket> getFluids() {
        return List.copyOf(this.fluids);
    }
}