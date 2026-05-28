package com.denfop.client.ability;

import com.denfop.Constants;
import com.denfop.IUCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = IUCore.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class AbilityClientModEvents {

    private AbilityClientModEvents() {
    }

    @SubscribeEvent
    public static void registerOverlays(final RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.tryBuild(Constants.MOD_ID, "ability_hud"), new AbilityHudOverlay());
    }
}
