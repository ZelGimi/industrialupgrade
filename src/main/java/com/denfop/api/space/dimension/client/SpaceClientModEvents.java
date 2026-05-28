package com.denfop.api.space.dimension.client;

import com.denfop.IUCore;
import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceBodyProfiles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IUCore.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
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
