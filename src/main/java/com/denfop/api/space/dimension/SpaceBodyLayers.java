package com.denfop.api.space.dimension;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public final class SpaceBodyLayers {

    public static final String STONE = "stone";
    public static final String COBBLE = "cobble";
    public static final String TOP = "top";
    public static final String SUBSURFACE = "subsurface";
    public static final String RIM = "rim";
    public static final String FLUID = "fluid";

    private final Map<String, BlockState> layers = new HashMap<>();

    public static SpaceBodyLayers create() {
        return new SpaceBodyLayers();
    }

    public static BlockState air() {
        return Blocks.AIR.defaultBlockState();
    }

    public SpaceBodyLayers put(final String key, final BlockState state) {
        if (state != null && !state.isAir()) {
            layers.put(key, state);
        }
        return this;
    }

    public BlockState get(final String key, final BlockState fallback) {
        return layers.getOrDefault(key, fallback);
    }

    public boolean has(final String key) {
        return layers.containsKey(key);
    }
}