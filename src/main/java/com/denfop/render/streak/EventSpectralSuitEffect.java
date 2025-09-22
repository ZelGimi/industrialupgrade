package com.denfop.render.streak;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventSpectralSuitEffect {

    public static final ResourceLocation texture = new ResourceLocation(Constants.TEXTURES_ITEMS + "effect.png");

    private static final Map<String, ArrayList<EventSpectralSuitEffect.StreakLocation>> playerLoc = new HashMap();
    public final int[] red = {255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 240, 222, 186, 150, 124, 96, 67, 40, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 34, 56, 78, 102, 121, 145, 176, 201, 218, 230, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255};
    public final int[] green = {0, 24, 36, 54, 72, 96, 120, 145, 172, 192, 216, 234, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 234, 214, 195, 176, 153, 137, 112, 94, 86, 55, 31, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public final int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 32, 45, 68, 78, 103, 118, 138, 151, 178, 205, 221, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 240, 228, 208, 186, 165, 149, 132, 115, 102, 97, 76, 53, 32, 15, 0};

    public EventSpectralSuitEffect() {
    }

    public static ArrayList<EventSpectralSuitEffect.StreakLocation> getPlayerStreakLocationInfo(EntityPlayer player) {
        ArrayList<EventSpectralSuitEffect.StreakLocation> loc = playerLoc.computeIfAbsent(
                player.getName(),
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

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        this.render(event.getPartialTicks());
    }

    public void render(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) {
            return;
        }
        PlayerStreakInfo playerStreak1 = IUCore.mapStreakInfo.get(mc.player.getName());
        boolean needRender = true;
        if (playerStreak1 != null) {
            needRender = playerStreak1.isRenderPlayer();
        }
        if (needRender) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (this.isRenderStreak(player) && !player.getName().equals(mc.player.getName())) {
                    ArrayList<EventSpectralSuitEffect.StreakLocation> loc = getPlayerStreakLocationInfo(player);
                    GL11.glPushMatrix();
                    GL11.glDisable(2884);
                    GL11.glDisable(3008);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glShadeModel(7425);
                    float startGrad = 5.0F - partialTicks;
                    float endGrad = 20.0F - partialTicks;

                    for (int i = loc.size() - 2; i >= 0; --i) {
                        int start = i;
                        EventSpectralSuitEffect.StreakLocation infoStart = loc.get(i);
                        float startAlpha = (float) i < endGrad
                                ? MathHelper.clamp(0.8F * (float) i / endGrad, 0.0F, 0.8F)
                                : ((float) i > (float) (loc.size() - 2) - startGrad
                                        ? MathHelper.clamp(0.8F * (float) (loc.size() - 2 - i) / startGrad, 0.0F, 0.8F)
                                        : 0.8F);
                        if (player.world.getWorldTime() - infoStart.lastTick > 40L) {
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
                                    ? MathHelper.clamp(0.8F * (float) (i - 1) / endGrad, 0.0F, 0.8F)
                                    : ((float) i > (float) (loc.size() - 1) - startGrad
                                            ? MathHelper.clamp(0.8F * (float) (loc.size() - 1 - i) / startGrad, 0.0F, 0.8F)
                                            : 0.8F);
                            grad1 = infoStart.posX - mc.getRenderManager().renderPosX;
                            double posY = infoStart.posY - mc.getRenderManager().renderPosY;
                            double posZ = infoStart.posZ - mc.getRenderManager().renderPosZ;
                            double nextPosX = infoEnd.posX - mc.getRenderManager().renderPosX;
                            double nextPosY = infoEnd.posY - mc.getRenderManager().renderPosY;
                            double nextPosZ = infoEnd.posZ - mc.getRenderManager().renderPosZ;
                            Tessellator tessellator = Tessellator.getInstance();
                            GL11.glPushMatrix();
                            GL11.glTranslated(grad1, posY, posZ);
                            int ii = 15728880;
                            int j = ii % 65536;
                            int k = ii / 65536;
                            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
                            RenderHelper.disableStandardItemLighting();
                            GL11.glDisable(2896);
                            PlayerStreakInfo playerStreak = IUCore.mapStreakInfo.get(player.getName());
                            if (playerStreak == null) {
                                playerStreak = new PlayerStreakInfo(new RGB((short) 0, (short) 0, (short) 0), false, true, true);
                                IUCore.mapStreakInfo.put(player.getName(), playerStreak);
                            }
                            double red = playerStreak.getRgb().getRed();
                            double green = playerStreak.getRgb().getGreen();
                            double blue = playerStreak.getRgb().getBlue();
                            boolean rgb = playerStreak.isRainbow();


                            if (rgb) {
                                red = this.red[(int) (player.getEntityWorld().provider.getWorldTime() % this.red.length)];
                                green = this.green[(int) (player.getEntityWorld().provider.getWorldTime() % this.red.length)];
                                blue = this.blue[(int) (player.getEntityWorld().provider.getWorldTime() % this.red.length)];
                            }
                            Color color = new Color((float) (red / 255), (float) (green / 255), (float) (blue / 255), startAlpha);
                            mc.renderEngine.bindTexture(texture);
                            BufferBuilder buffer = tessellator.getBuffer();
                            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                            buffer.pos(0.0D, 0.0D, 0.0D).tex(infoStart.startU, 1.0D).color(
                                    color.getRed(),
                                    color.getGreen(),
                                    color.getBlue(),
                                    (int) (255.0F * startAlpha)
                            ).endVertex();
                            buffer.pos(0.0D, 0.0F + infoStart.height, 0.0D).tex(infoStart.startU, 0.0D).color(
                                    color.getRed(),
                                    color.getGreen(),
                                    color.getBlue(),
                                    (int) (255.0F * startAlpha)
                            ).endVertex();
                            double endTex = infoEnd.startU - (double) start + (double) i;
                            if (endTex > infoStart.startU) {
                                --endTex;
                            }

                            double distX = infoStart.posX - infoEnd.posX;
                            double distZ = infoStart.posZ - infoEnd.posZ;

                            for (double scales = Math.sqrt(distX * distX + distZ * distZ) / (double) infoStart.height; scales > 1.0D; --scales) {
                                ++endTex;
                            }

                            buffer
                                    .pos(nextPosX - grad1, nextPosY - posY + (double) infoEnd.height, nextPosZ - posZ)
                                    .tex(endTex, 0.0D)
                                    .color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255.0F * endAlpha))
                                    .endVertex();
                            buffer.pos(nextPosX - grad1, nextPosY - posY, nextPosZ - posZ).tex(endTex, 1.0D).color(
                                    color.getRed(),
                                    color.getGreen(),
                                    color.getBlue(),
                                    (int) (255.0F * endAlpha)
                            ).endVertex();
                            tessellator.draw();
                            GL11.glEnable(2896);
                            RenderHelper.enableStandardItemLighting();
                            GL11.glPopMatrix();
                        }
                    }

                    GL11.glShadeModel(7424);
                    GL11.glDisable(3042);
                    GL11.glEnable(3008);
                    GL11.glEnable(2884);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(Post event) {
        final EntityPlayer player = event.getEntityPlayer();
        final float partialTicks = event.getPartialRenderTick();
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) {
            return;
        }
        PlayerStreakInfo playerStreak1 = IUCore.mapStreakInfo.get(mc.player.getName());
        boolean needRender = true;
        if (playerStreak1 != null) {
            needRender = playerStreak1.isRender();
        }
        if (needRender && this.isRenderStreak(player) && player.getName().equals(mc.player.getName())) {
            ArrayList<EventSpectralSuitEffect.StreakLocation> loc = getPlayerStreakLocationInfo(player);
            GL11.glPushMatrix();
            GL11.glDisable(2884);
            GL11.glDisable(3008);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            float startGrad = 5.0F - partialTicks;
            float endGrad = 20.0F - partialTicks;

            for (int i = loc.size() - 2; i >= 0; --i) {
                int start = i;
                EventSpectralSuitEffect.StreakLocation infoStart = loc.get(i);
                float startAlpha = (float) i < endGrad
                        ? MathHelper.clamp(0.8F * (float) i / endGrad, 0.0F, 0.8F)
                        : ((float) i > (float) (loc.size() - 2) - startGrad
                                ? MathHelper.clamp(0.8F * (float) (loc.size() - 2 - i) / startGrad, 0.0F, 0.8F)
                                : 0.8F);
                if (player.world.getWorldTime() - infoStart.lastTick > 40L) {
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
                            ? MathHelper.clamp(0.8F * (float) (i - 1) / endGrad, 0.0F, 0.8F)
                            : ((float) i > (float) (loc.size() - 1) - startGrad
                                    ? MathHelper.clamp(0.8F * (float) (loc.size() - 1 - i) / startGrad, 0.0F, 0.8F)
                                    : 0.8F);
                    grad1 = infoStart.posX - mc.getRenderManager().renderPosX;
                    double posY = infoStart.posY - mc.getRenderManager().renderPosY;
                    double posZ = infoStart.posZ - mc.getRenderManager().renderPosZ;
                    double nextPosX = infoEnd.posX - mc.getRenderManager().renderPosX;
                    double nextPosY = infoEnd.posY - mc.getRenderManager().renderPosY;
                    double nextPosZ = infoEnd.posZ - mc.getRenderManager().renderPosZ;
                    Tessellator tessellator = Tessellator.getInstance();
                    GL11.glPushMatrix();
                    GL11.glTranslated(grad1, posY, posZ);
                    int ii = 15728880;
                    int j = ii % 65536;
                    int k = ii / 65536;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
                    RenderHelper.disableStandardItemLighting();
                    GL11.glDisable(2896);
                    PlayerStreakInfo playerStreak = IUCore.mapStreakInfo.get(player.getName());
                    if (playerStreak == null) {
                        playerStreak = new PlayerStreakInfo(new RGB((short) 0, (short) 0, (short) 0), false, true, true);
                        IUCore.mapStreakInfo.put(player.getName(), playerStreak);
                    }
                    double red = playerStreak.getRgb().getRed();
                    double green = playerStreak.getRgb().getGreen();
                    double blue = playerStreak.getRgb().getBlue();
                    boolean rgb = playerStreak.isRainbow();


                    if (rgb) {
                        red = this.red[(int) (player.getEntityWorld().provider.getWorldTime() % this.red.length)];
                        green = this.green[(int) (player.getEntityWorld().provider.getWorldTime() % this.red.length)];
                        blue = this.blue[(int) (player.getEntityWorld().provider.getWorldTime() % this.red.length)];
                    }
                    Color color = new Color((float) (red / 255), (float) (green / 255), (float) (blue / 255), startAlpha);
                    mc.renderEngine.bindTexture(texture);
                    BufferBuilder buffer = tessellator.getBuffer();
                    buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    buffer.pos(0.0D, 0.0D, 0.0D).tex(infoStart.startU, 1.0D).color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            (int) (255.0F * startAlpha)
                    ).endVertex();
                    buffer.pos(0.0D, 0.0F + infoStart.height, 0.0D).tex(infoStart.startU, 0.0D).color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            (int) (255.0F * startAlpha)
                    ).endVertex();
                    double endTex = infoEnd.startU - (double) start + (double) i;
                    if (endTex > infoStart.startU) {
                        --endTex;
                    }

                    double distX = infoStart.posX - infoEnd.posX;
                    double distZ = infoStart.posZ - infoEnd.posZ;

                    for (double scales = Math.sqrt(distX * distX + distZ * distZ) / (double) infoStart.height; scales > 1.0D; --scales) {
                        ++endTex;
                    }

                    buffer
                            .pos(nextPosX - grad1, nextPosY - posY + (double) infoEnd.height, nextPosZ - posZ)
                            .tex(endTex, 0.0D)
                            .color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255.0F * endAlpha))
                            .endVertex();
                    buffer.pos(nextPosX - grad1, nextPosY - posY, nextPosZ - posZ).tex(endTex, 1.0D).color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            (int) (255.0F * endAlpha)
                    ).endVertex();
                    tessellator.draw();
                    GL11.glEnable(2896);
                    RenderHelper.enableStandardItemLighting();
                    GL11.glPopMatrix();
                }
            }

            GL11.glShadeModel(7424);
            GL11.glDisable(3042);
            GL11.glEnable(3008);
            GL11.glEnable(2884);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.side == Side.CLIENT && event.phase == Phase.END) {
            EntityPlayer player = event.player;
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

    private boolean isRenderStreak(EntityPlayer player) {
        NonNullList<ItemStack> armors = player.inventory.armorInventory;

        return armors.get(2).getItem() == IUItem.spectral_chestplate;
    }

    public static class StreakLocation {

        public double posX;
        public double posY;
        public double posZ;
        public float renderYawOffset;
        public float rotationYawHead;
        public float rotationPitch;
        public float limbSwing;
        public float limbSwingAmount;
        public boolean isSprinting;
        public long lastTick;
        public float height;
        public double startU;

        public StreakLocation(EntityPlayer player) {
            this.update(player);
        }

        public void update(EntityPlayer player) {
            this.posX = player.posX;
            this.posY = player.getEntityBoundingBox().minY;
            this.posZ = player.posZ;
            this.renderYawOffset = player.renderYawOffset;
            this.rotationYawHead = player.rotationYawHead;
            this.rotationPitch = player.rotationPitch;
            this.limbSwing = player.limbSwing;
            this.limbSwingAmount = player.limbSwingAmount;
            this.isSprinting = player.isSprinting();
            this.lastTick = player.world.getWorldTime();
            this.height = player.height;
        }

        public boolean hasSameCoords(EventSpectralSuitEffect.StreakLocation loc) {
            return loc.posX == this.posX && loc.posY == this.posY && loc.posZ == this.posZ && loc.height == this.height;
        }

    }

}
