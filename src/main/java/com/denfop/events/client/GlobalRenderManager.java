package com.denfop.events.client;


import com.denfop.mixin.access.LevelRendererAccessor;
import com.denfop.screen.ScreenMain;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


public class GlobalRenderManager {

    private static final Map<String, Map<BlockPos, Function<RenderLevelStageEvent, Void>>> globalRenders = new HashMap<>();
    public static long tick = 0;

    public GlobalRenderManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void addRender(Level world, BlockPos pos, Function<RenderLevelStageEvent, Void> globalRender) {
        String dimension = world.dimension().location().toString();
        globalRenders.computeIfAbsent(dimension, k -> new HashMap<>()).putIfAbsent(pos, globalRender);
    }

    public static void removeRender(Level world, BlockPos pos) {
        String dimension = world.dimension().location().toString();
        globalRenders.computeIfPresent(dimension, (k, v) -> {
            v.remove(pos);
            return v.isEmpty() ? null : v;
        });
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if ((event.getLevel()).isClientSide()) {
            return;
        }
        globalRenders.getOrDefault(((Level) event.getLevel()).dimension().toString(), new HashMap<>()).clear();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onWorldTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player.getLevel().isClientSide && tick != event.player
                .getLevel()
                .getGameTime()) {
            tick = event.player.getLevel().getGameTime();
            Screen guiScreen = Minecraft.getInstance().screen;
            if (guiScreen instanceof ScreenMain) {
                double ticks = 4;
                while (ticks > 0) {
                    ((ScreenMain<?>) guiScreen).updateTickInterface();
                    ticks--;
                }

            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        try {
            for (Map.Entry<String, Map<BlockPos, Function<RenderLevelStageEvent, Void>>> entry : globalRenders.entrySet()) {
                if (Objects.equals(entry.getKey(), mc.level.dimension().location().toString())) {
                    PoseStack poseStack = event.getPoseStack();
                    Vec3 camera = event.getCamera().getPosition();

                    double x = camera.x;
                    double y = camera.y;
                    double z = camera.z;
                    double dopY = 0;

                    poseStack.pushPose();
                    poseStack.translate(-x, -y, -z);


                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                    for (Function<RenderLevelStageEvent, Void> function : entry.getValue().values()) {
                        function.apply(event);
                    }
                    ((LevelRendererAccessor) event.getLevelRenderer()).getRenderBuffers().bufferSource().endBatch();
                    poseStack.popPose();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
