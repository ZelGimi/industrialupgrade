package com.denfop.render.streak;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.denfop.IUCore.mapStreakInfo;

public class EventSpectralSuitEffect {

    public static final ResourceLocation texture = new ResourceLocation(Constants.TEXTURES_ITEMS + "effect.png");
    private static final Map<String, ArrayList<EventSpectralSuitEffect.StreakLocation>> playerLoc = new HashMap();
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

        if (loc.size() < 20) {
            for (int i = 0; i < 20 - loc.size(); ++i) {
                loc.add(0, new EventSpectralSuitEffect.StreakLocation(player));
            }
        } else if (loc.size() > 20) {
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
        this.render(true, event.getPartialTick(), event.getPoseStack());
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderLevelStageEvent event) {

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
            this.render(false, event.getPartialTick(), event.getPoseStack());
    }

    public void render(boolean ignore, float partialTicks, PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }

        PlayerStreakInfo playerStreak1 = mapStreakInfo.get(player.getName().getString());
        boolean needRender = true;
        if (playerStreak1 != null) {
            needRender = isRenderStreak(player);
        }

        if (needRender) {
            for (Player targetPlayer : mc.level.players()) {
                if (this.isRenderStreak(targetPlayer) && (!targetPlayer.getName().getString().equals(player.getName().getString())) || ignore) {
                    ArrayList<EventSpectralSuitEffect.StreakLocation> loc = getPlayerStreakLocationInfo(targetPlayer);

                    poseStack.pushPose();


                    float startGrad = 5.0F - partialTicks;
                    float endGrad = 20.0F - partialTicks;

                    for (int i = loc.size() - 2; i >= 0; --i) {
                        int start = i;
                        EventSpectralSuitEffect.StreakLocation infoStart = loc.get(i);
                        float startAlpha = (float) i < endGrad
                                ? clamp(0.8F * (float) i / endGrad, 0.0F, 0.8F)
                                : ((float) i > (float) (loc.size() - 2) - startGrad
                                ? clamp(0.8F * (float) (loc.size() - 2 - i) / startGrad, 0.0F, 0.8F)
                                : 0.8F);


                        if (mc.level.getGameTime() - infoStart.lastTick > 40L) {
                            break;
                        }

                        EventSpectralSuitEffect.StreakLocation infoEnd = null;
                        double grad = 500.0D;
                        --i;

                        double grad1;
                        label76:
                        while (true) {
                            while (true) {
                                if (i < 0) {
                                    break label76;
                                }

                                EventSpectralSuitEffect.StreakLocation infoPoint = loc.get(i);
                                if (infoStart.isSprinting && loc.size() - 2 - i < 6) {
                                    infoEnd = infoPoint;
                                    --start;
                                    --i;
                                    break label76;
                                }

                                if (infoPoint.hasSameCoords(infoStart)) {
                                    --start;
                                    --i;
                                } else {
                                    grad1 = infoPoint.posZ - infoStart.posZ / (infoPoint.posX - infoStart.posX);
                                    if (grad == grad1 && infoPoint.posY == infoStart.posY) {
                                        infoEnd = infoPoint;
                                        --start;
                                        --i;
                                    } else {
                                        if (grad != 500.0D) {
                                            break label76;
                                        }

                                        grad = grad1;
                                        infoEnd = infoPoint;
                                        --i;
                                    }
                                }
                            }
                        }

                        if (infoEnd != null) {
                            i += 2;
                            float endAlpha = (float) i < endGrad
                                    ? clamp(0.8F * (float) (i - 1) / endGrad, 0.0F, 0.8F)
                                    : ((float) i > (float) (loc.size() - 1) - startGrad
                                    ? clamp(0.8F * (float) (loc.size() - 1 - i) / startGrad, 0.0F, 0.8F)
                                    : 0.8F);

                            grad1 = infoStart.posX - mc.gameRenderer.getMainCamera().getPosition().x;
                            double posY = infoStart.posY - mc.gameRenderer.getMainCamera().getPosition().y;
                            double posZ = infoStart.posZ - mc.gameRenderer.getMainCamera().getPosition().z;
                            double nextPosX = infoEnd.posX - mc.gameRenderer.getMainCamera().getPosition().x;
                            double nextPosY = infoEnd.posY - mc.gameRenderer.getMainCamera().getPosition().y;
                            double nextPosZ = infoEnd.posZ - mc.gameRenderer.getMainCamera().getPosition().z;

                            float deltaX = (float) (nextPosX - grad1);
                            float deltaZ = (float) (nextPosZ - posZ);

                            poseStack.pushPose();
                            poseStack.translate(grad1, posY, posZ);
                            RenderSystem.disableBlend();
                            RenderSystem.defaultBlendFunc();
                            RenderSystem.disableCull();
                            RenderSystem.enableDepthTest();
                            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);


                            PlayerStreakInfo playerStreak = mapStreakInfo.get(targetPlayer.getName().getString());
                            if (playerStreak == null) {
                                playerStreak = new PlayerStreakInfo(new RGB((short) 0, (short) 0, (short) 0), false);
                                mapStreakInfo.put(targetPlayer.getName().getString(), playerStreak);
                            }

                            double red = playerStreak.getRgb().getRed() / 255d;
                            double green = playerStreak.getRgb().getGreen() / 255d;
                            double blue = playerStreak.getRgb().getBlue() / 255d;
                            boolean rgb = playerStreak.isRainbow();
                            if (rgb) {
                                long worldTime = mc.level.getGameTime();
                                red = this.red[(int) (worldTime % this.red.length)] / 255f;
                                green = this.green[(int) (worldTime % this.green.length)] / 255f;
                                blue = this.blue[(int) (worldTime % this.red.length)] / 255f;
                            }

                            Color color = new Color((float) red, (float) green, (float) blue, startAlpha);
                            ScreenIndustrialUpgrade.bindTexture(texture);
                            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
                            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

                            Matrix4f matrix4f = poseStack.last().pose();
                            RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);

                            buffer.vertex(matrix4f, 0.0f, 0.0f, 0.0f).uv((float) infoStart.startU, 1.0f).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
                            buffer.vertex(matrix4f, 0.0f, 0.0F + infoStart.height, 0.0f).uv((float) infoStart.startU, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();

                            double endTex = infoEnd.startU - (double) start + (double) i;
                            if (endTex > infoStart.startU) {
                                --endTex;
                            }

                            double distX = infoStart.posX - infoEnd.posX;
                            double distZ = infoStart.posZ - infoEnd.posZ;
                            float correctedDeltaX = deltaX > 0 ? deltaX : 0;
                            float correctedDeltaZ = deltaZ > 0 ? deltaZ : 0;
                            for (double scales = Math.sqrt(distX * distX + distZ * distZ) / (double) infoStart.height; scales > 1.0D; --scales) {
                                ++endTex;
                            }

                            buffer.vertex(matrix4f, (float) Math.abs(nextPosX - grad1), (float) (nextPosY - posY + (double) infoEnd.height), (float) Math.abs(nextPosZ - posZ)).uv((float) endTex, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
                            buffer.vertex(matrix4f, (float) Math.abs(nextPosX - grad1), (float) (nextPosY - posY), (float) Math.abs(nextPosZ - posZ)).uv((float) endTex, 1.0f).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();


                            BufferUploader.drawWithShader(buffer.end());
                            RenderSystem.enableBlend();
                            RenderSystem.enableCull();
                            RenderSystem.disableDepthTest();
                            poseStack.popPose();
                            RenderSystem.setShaderColor(1, 1, 1, 1);

                        }
                    }

                    poseStack.popPose();

                }
            }
        }
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
            this.lastTick = player.level().getGameTime();
            this.height = (float) ((float) player.getBbHeight() * 2);
        }

        public boolean hasSameCoords(StreakLocation loc) {
            return loc.posX == this.posX && loc.posY == this.posY && loc.posZ == this.posZ && loc.height == this.height;
        }
    }

}
