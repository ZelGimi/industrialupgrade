package com.denfop.events.client;

import com.denfop.mixin.access.LevelRendererAccessor;
import com.denfop.screen.ScreenMain;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class GlobalRenderManager {

    private static final Map<ResourceKey<Level>, Map<BlockPos, Function<RenderLevelStageEvent, Void>>> globalRenders =
            new ConcurrentHashMap<>();

    public static long tick = 0;

    public GlobalRenderManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void addRender(Level world, BlockPos pos, Function<RenderLevelStageEvent, Void> globalRender) {
        if (world == null || pos == null || globalRender == null) {
            return;
        }

        if (!world.isClientSide) {
            return;
        }

        globalRenders
                .computeIfAbsent(world.dimension(), k -> new ConcurrentHashMap<>())
                .putIfAbsent(pos.immutable(), globalRender);
    }

    public static void removeRender(Level world, BlockPos pos) {
        if (world == null || pos == null) {
            return;
        }

        ResourceKey<Level> dimension = world.dimension();

        globalRenders.computeIfPresent(dimension, (key, renders) -> {
            renders.remove(pos);
            return renders.isEmpty() ? null : renders;
        });
    }

    public static void clearAllRenders() {
        globalRenders.clear();
        tick = 0;
    }

    public static void clearDimensionRenders(Level level) {
        if (level == null) {
            return;
        }

        globalRenders.remove(level.dimension());

        if (globalRenders.isEmpty()) {
            tick = 0;
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if (!(event.getLevel() instanceof Level level)) {
            return;
        }

        if (!level.isClientSide) {
            return;
        }

        clearDimensionRenders(level);
    }

    @SubscribeEvent
    public void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        clearAllRenders();
    }

    @SubscribeEvent
    public void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        clearAllRenders();
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!event.player.level().isClientSide) {
            return;
        }

        long currentGameTime = event.player.level().getGameTime();

        if (tick == currentGameTime) {
            return;
        }

        tick = currentGameTime;

        Screen guiScreen = Minecraft.getInstance().screen;

        if (guiScreen instanceof ScreenMain<?> screenMain) {
            int ticks = 4;

            while (ticks > 0) {
                screenMain.updateTickInterface();
                ticks--;
            }

            screenMain.updateTick();
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null) {
            return;
        }

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Map<BlockPos, Function<RenderLevelStageEvent, Void>> renders = globalRenders.get(mc.level.dimension());

        if (renders == null || renders.isEmpty()) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        Vec3 camera = event.getCamera().getPosition();

        poseStack.pushPose();

        try {
            poseStack.translate(-camera.x, -camera.y, -camera.z);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            for (Function<RenderLevelStageEvent, Void> function : new ArrayList<>(renders.values())) {
                if (function != null) {
                    function.apply(event);
                }
            }

            ((LevelRendererAccessor) event.getLevelRenderer())
                    .getRenderBuffers()
                    .bufferSource()
                    .endBatch();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            poseStack.popPose();
        }
    }
}