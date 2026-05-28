package com.denfop.client.intro;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = IntroConstants.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class IntroClientEvents {

    public static final KeyMapping OPEN_INTRO = new KeyMapping(
            "key.industrialupgrade.open_intro",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "key.categories.industrialupgrade"
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_INTRO);
    }

    @EventBusSubscriber(modid = IntroConstants.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    public static class GameEvents {

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null || minecraft.level == null) {
                return;
            }

            while (OPEN_INTRO.consumeClick()) {
                if (minecraft.screen == null) {
                    minecraft.setScreen(new IntroDashboardScreen(IntroDashboardData.createDefault()));
                }
            }
        }
    }
}
