package com.denfop.api.space.dimension.worldgen.feature.asteroid;

import com.denfop.IUCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.Map;

@EventBusSubscriber(modid = IUCore.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class AsteroidOreReloadListener {

    private static final Gson GSON = new GsonBuilder().setLenient().create();

    private AsteroidOreReloadListener() {
    }

    @SubscribeEvent
    public static void onReloadListener(final AddReloadListenerEvent event) {
        event.addListener(new Loader());
    }

    private static final class Loader extends SimpleJsonResourceReloadListener {

        private Loader() {
            super(GSON, "iu_space/asteroid_ores");
        }

        @Override
        protected void apply(final Map<ResourceLocation, JsonElement> jsonMap, final ResourceManager manager, final ProfilerFiller profiler) {
            AsteroidOreRegistry.applyReload(jsonMap);
        }
    }
}
