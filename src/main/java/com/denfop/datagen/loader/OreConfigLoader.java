package com.denfop.datagen.loader;

import com.denfop.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class OreConfigLoader extends SimpleJsonResourceReloadListener {
    public static final Map<ResourceLocation, CustomOreConfig> ORES = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static Logger LOGGER = LogUtils.getLogger();

    public OreConfigLoader() {
        super(GSON, "orespawn");
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new OreConfigLoader());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ORES.clear();
        for (var entry : map.entrySet()) {
            try {
                CustomOreConfig config = CustomOreConfig.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .resultOrPartial(err -> {
                            LOGGER.warn("[OreConfigLoader] Error parsing " + entry.getKey() + ": " + err);
                        })
                        .orElse(null);

                if (config != null) {
                    ORES.put(entry.getKey(), config);
                }
            } catch (Exception e) {
                LOGGER.error("[OreConfigLoader] Failed to load " + entry.getKey() + ": " + e);
            }
        }
        LOGGER.warn("[OreConfigLoader] Loaded ores: " + ORES.size());
    }
}
