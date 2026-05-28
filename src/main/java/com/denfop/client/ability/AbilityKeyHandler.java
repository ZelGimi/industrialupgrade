package com.denfop.client.ability;

import com.denfop.IUCore;
import com.denfop.network.packet.PacketActivateAbility;
import com.denfop.utils.KeyboardClient;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IUCore.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AbilityKeyHandler {

    private AbilityKeyHandler() {
    }

    @SubscribeEvent
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.screen != null) {
            return;
        }

        while (KeyboardClient.abilitymode.consumeClick()) {
            new PacketActivateAbility(mc.player);
        }
    }
}
