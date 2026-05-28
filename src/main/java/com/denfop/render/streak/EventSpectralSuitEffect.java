package com.denfop.render.streak;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.denfop.IUCore.mapStreakInfo;

public class EventSpectralSuitEffect {

    public static final ResourceLocation texture = new ResourceLocation(Constants.TEXTURES_ITEMS + "effect.png");
    private static final Map<String, ArrayList<EventSpectralSuitEffect.StreakLocation>> playerLoc = new HashMap<>();

    public final int[] red = {255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 240, 222, 186, 150, 124, 96, 67, 40, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 34, 56, 78, 102, 121, 145, 176, 201, 218, 230, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255};
    public final int[] green = {0, 24, 36, 54, 72, 96, 120, 145, 172, 192, 216, 234, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 234, 214, 195, 176, 153, 137, 112, 94, 86, 55, 31, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public final int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 32, 45, 68, 78, 103, 118, 138, 151, 178, 205, 221, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 240, 228, 208, 186, 165, 149, 132, 115, 102, 97, 76, 53, 32, 15, 0};

    public EventSpectralSuitEffect() {
    }

    public static ArrayList<EventSpectralSuitEffect.StreakLocation> getPlayerStreakLocationInfo(Player player) {
        ArrayList<EventSpectralSuitEffect.StreakLocation> loc = playerLoc.computeIfAbsent(
                player.getName().getString(),
                k -> new ArrayList<>()
        );

        if (loc.size() < 40) {
            for (int i = 0; i < 40 - loc.size(); ++i) {
                loc.add(0, new EventSpectralSuitEffect.StreakLocation(player));
            }
        } else if (loc.size() > 40) {
            loc.remove(0);
        }

        return loc;
    }

    public static float clamp(float num, float min, float max) {
        if (num < min) {
            return min;
        } else {
            return Math.min(num, max);
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(Post event) {
        this.render(event.getEntity(), event.getPartialTick(), event.getPoseStack());
    }

    public void render(Player player, float partialTicks, PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();

        if (player == null || mc.level == null) {
            return;
        }

        PlayerStreakInfo playerStreak1 = mapStreakInfo.get(player.getName().getString());
        boolean needRender = true;
        if (playerStreak1 != null) {
            needRender = isRenderStreak(player);
        }

        if (!needRender || !this.isRenderStreak(player)) {
            return;
        }

        ArrayList<EventSpectralSuitEffect.StreakLocation> loc = getPlayerStreakLocationInfo(player);
        if (loc.size() < 2) {
            return;
        }

        poseStack.pushPose();

        float startGrad = 5.0F - partialTicks;
        float endGrad = 40.0F - partialTicks;

        PlayerStreakInfo playerStreak = mapStreakInfo.get(player.getName().getString());
        if (playerStreak == null) {
            playerStreak = new PlayerStreakInfo(new RGB((short) 0, (short) 0, (short) 0), false);
            mapStreakInfo.put(player.getName().getString(), playerStreak);
        }

        double red = playerStreak.getRgb().getRed() / 255d;
        double green = playerStreak.getRgb().getGreen() / 255d;
        double blue = playerStreak.getRgb().getBlue() / 255d;
        boolean rgb = playerStreak.isRainbow();
        if (rgb) {
            long worldTime = mc.level.getGameTime();
            red = this.red[(int) (worldTime % this.red.length)] / 255f;
            green = this.green[(int) (worldTime % this.green.length)] / 255f;
            blue = this.blue[(int) (worldTime % this.blue.length)] / 255f;
        }

        ScreenIndustrialUpgrade.bindTexture(texture);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        Matrix4f matrix4f = poseStack.last().pose();

        double camX = mc.gameRenderer.getMainCamera().getPosition().x;
        double camY = mc.gameRenderer.getMainCamera().getPosition().y;
        double camZ = mc.gameRenderer.getMainCamera().getPosition().z;

        for (int i = 0; i < loc.size() - 1; i++) {
            StreakLocation infoStart = loc.get(i);
            StreakLocation infoEnd = loc.get(i + 1);

            if (mc.level.getGameTime() - infoStart.lastTick > 40L) {
                continue;
            }

            float startAlpha = (float) i < endGrad
                    ? clamp(0.8F * (float) i / endGrad, 0.0F, 0.8F)
                    : ((float) i > (float) (loc.size() - 2) - startGrad
                    ? clamp(0.8F * (float) (loc.size() - 2 - i) / startGrad, 0.0F, 0.8F)
                    : 0.8F);

            float endAlphaIndex = i + 1;
            float endAlpha = endAlphaIndex < endGrad
                    ? clamp(0.8F * endAlphaIndex / endGrad, 0.0F, 0.8F)
                    : (endAlphaIndex > (float) (loc.size() - 1) - startGrad
                    ? clamp(0.8F * ((float) (loc.size() - 1) - endAlphaIndex) / startGrad, 0.0F, 0.8F)
                    : 0.8F);

            float x0 = (float) (infoStart.posX - camX);
            float y0 = (float) (infoStart.posY - camY);
            float z0 = (float) (infoStart.posZ - camZ);

            float x1 = (float) (infoEnd.posX - camX);
            float y1 = (float) (infoEnd.posY - camY);
            float z1 = (float) (infoEnd.posZ - camZ);

            Color colorStart = new Color((float) red, (float) green, (float) blue, startAlpha);
            Color colorEnd = new Color((float) red, (float) green, (float) blue, endAlpha);

            buffer.vertex(matrix4f, x0, y0, z0)
                    .uv((float) infoStart.startU, 1.0f)
                    .color(colorStart.getRed(), colorStart.getGreen(), colorStart.getBlue(), colorStart.getAlpha())
                    .endVertex();

            buffer.vertex(matrix4f, x0, y0 + infoStart.height, z0)
                    .uv((float) infoStart.startU, 0.0f)
                    .color(colorStart.getRed(), colorStart.getGreen(), colorStart.getBlue(), colorStart.getAlpha())
                    .endVertex();

            buffer.vertex(matrix4f, x1, y1 + infoEnd.height, z1)
                    .uv((float) infoEnd.startU, 0.0f)
                    .color(colorEnd.getRed(), colorEnd.getGreen(), colorEnd.getBlue(), colorEnd.getAlpha())
                    .endVertex();

            buffer.vertex(matrix4f, x1, y1, z1)
                    .uv((float) infoEnd.startU, 1.0f)
                    .color(colorEnd.getRed(), colorEnd.getGreen(), colorEnd.getBlue(), colorEnd.getAlpha())
                    .endVertex();
        }

        BufferUploader.drawWithShader(buffer.end());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        poseStack.popPose();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            if (this.isRenderStreak(player)) {
                ArrayList<EventSpectralSuitEffect.StreakLocation> loc = getPlayerStreakLocationInfo(player);
                EventSpectralSuitEffect.StreakLocation oldest = loc.get(0);
                loc.remove(0);
                loc.add(oldest);
                oldest.update(player);
                EventSpectralSuitEffect.StreakLocation newest = loc.get(loc.size() - 2);
                double distX = newest.posX - oldest.posX;
                double distZ = newest.posZ - oldest.posZ;

                for (newest.startU += Math.sqrt(distX * distX + distZ * distZ) / (double) newest.height; oldest.startU > 1.0D; --oldest.startU) {
                }
            }
        }
    }

    private boolean isRenderStreak(Player player) {
        NonNullList<ItemStack> armors = player.getInventory().armor;
        return Minecraft.getInstance().screen == null && armors.get(2).getItem() == IUItem.spectral_chestplate.getItem();
    }

    public static class StreakLocation {

        public double posX;
        public double posY;
        public double posZ;
        public float renderYawOffset;
        public float rotationYawHead;
        public float rotationPitch;
        public boolean isSprinting;
        public long lastTick;
        public float height;
        public double startU;

        public StreakLocation(Player player) {
            this.update(player);
        }

        public void update(Player player) {
            this.posX = player.getX();
            this.posY = player.getBoundingBox().minY;
            this.posZ = player.getZ();
            this.renderYawOffset = player.yBodyRot;
            this.rotationYawHead = player.getViewYRot(Minecraft.getInstance().getPartialTick());
            this.rotationPitch = player.getViewXRot(Minecraft.getInstance().getPartialTick());
            this.isSprinting = player.isSprinting();
            this.lastTick = player.getLevel().getGameTime();
            this.height = (float) (player.getBbHeight() * 2.0F);
        }

        public boolean hasSameCoords(StreakLocation loc) {
            return loc.posX == this.posX && loc.posY == this.posY && loc.posZ == this.posZ && loc.height == this.height;
        }
    }
}