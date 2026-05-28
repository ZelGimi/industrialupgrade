package com.denfop.api.space.dimension;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class SpaceLevelStemProvider implements DataProvider {

    private final DataGenerator generator;
    private final String modId;
    private final RegistryOps<JsonElement> registryOps;
    private final Map<ResourceLocation, LevelStem> entries;

    public SpaceLevelStemProvider(
            final DataGenerator generator,
            final String modId,
            final RegistryOps<JsonElement> registryOps,
            final Map<ResourceLocation, LevelStem> entries
    ) {
        this.generator = generator;
        this.modId = modId;
        this.registryOps = registryOps;
        this.entries = entries;
    }

    @Override
    public void run(final CachedOutput cache) {
        final Path dataRoot = this.generator.getOutputFolder()
                .resolve("data");

        final List<CompletableFuture<?>> futures = new ArrayList<>();

        for (final Map.Entry<ResourceLocation, LevelStem> entry : this.entries.entrySet()) {
            final DataResult<JsonElement> encoded = LevelStem.CODEC.encodeStart(this.registryOps, entry.getValue());
            final JsonElement json = encoded.getOrThrow(false, s -> {
            });
            if (!(json instanceof JsonObject object)) {
                throw new IllegalStateException("Expected JsonObject for dimension '" + entry.getKey() + "'");
            }

            final Path target = dataRoot
                    .resolve(entry.getKey().getNamespace())
                    .resolve("dimension")
                    .resolve(entry.getKey().getPath() + ".json");

            try {
                DataProvider.saveStable(cache, object, target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public String getName() {
        return "Industrial Upgrade Space Dimensions";
    }
}
