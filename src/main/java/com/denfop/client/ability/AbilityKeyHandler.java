package com.denfop.client.ability;

import com.denfop.IUCore;
import com.denfop.network.packet.PacketActivateAbility;
import com.denfop.utils.KeyboardClient;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = IUCore.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class AbilityKeyHandler {

    private AbilityKeyHandler() {
    }

    @SubscribeEvent
    public static void onClientTick(final ClientTickEvent.Post event) {


        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.screen != null) {
            return;
        }

        while (KeyboardClient.abilitymode.consumeClick()) {
            new PacketActivateAbility(mc.player);
        }
    }
}
