package com.denfop.api.space.dimension.client;

import com.denfop.IUCore;
import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceBodyProfiles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

@EventBusSubscriber(modid = IUCore.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class SpaceClientModEvents {

    private SpaceClientModEvents() {
    }

    @SubscribeEvent
    public static void onRegisterSpecialEffects(final RegisterDimensionSpecialEffectsEvent event) {
        for (var body : SpaceBodyCatalog.allBodies()) {
            var profile = SpaceBodyProfiles.byBody(body);
            event.register(profile.effectsLocation(), new SpaceDimensionSpecialEffects(profile));
        }
    }
}
