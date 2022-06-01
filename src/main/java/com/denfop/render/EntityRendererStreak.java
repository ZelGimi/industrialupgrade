package com.denfop.render;

import com.denfop.Constants;
import com.denfop.items.armour.ItemArmorImprovemedQuantum;
import com.denfop.utils.StreakLocationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class EntityRendererStreak extends Render<EntityStreak> {

    private static final ResourceLocation texture = new ResourceLocation(Constants.TEXTURES_ITEMS + "effect.png");

    public EntityRendererStreak(final RenderManager renderManager) {
        super(renderManager);
    }

    public void doRender(@Nonnull EntityStreak entity, double par2, double par3, double par4, float par5, float par6) {
        renderStreak(entity);
    }

    protected ResourceLocation getEntityTexture(@Nonnull EntityStreak p_110775_1_) {

        return texture;
    }

    private void renderStreak(EntityStreak entity) {
        if (entity.parent instanceof AbstractClientPlayer && !entity.isInvisible()) {
            AbstractClientPlayer player = (AbstractClientPlayer) entity.parent;
            Minecraft mc = Minecraft.getMinecraft();
            if (!entity.isInvisible() && (player != mc.player || mc.gameSettings.thirdPersonView != 0)) {
                if (player.inventory.armorInventory.get(2).isEmpty()) {
                    return;
                }
                if (!(player.inventory.armorInventory.get(2).getItem() instanceof ItemArmorImprovemedQuantum)) {
                    return;
                }
                ArrayList<StreakLocationUtils> loc = EventStreakEffect
                        .getPlayerStreakLocationInfo(player);
                GL11.glPushMatrix();
                GL11.glDisable(2884);
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glShadeModel(7425);
                for (int i = loc.size() - 2; i >= 0; i--) {
                    int start = i;
                    StreakLocationUtils infoStart = loc.get(i);
                    if (player.getEntityWorld().getWorldTime() - infoStart.lastTick > 40L) {
                        break;
                    }
                    StreakLocationUtils infoEnd = null;
                    double grad = 500.0D;
                    i--;
                    while (i >= 0) {
                        StreakLocationUtils infoPoint = loc.get(i);
                        if (infoStart.isSprinting && loc.size() - 2 - i < 6) {
                            infoEnd = infoPoint;
                            start--;
                            i--;
                            break;
                        }
                        if (infoPoint.hasSameCoords(infoStart)) {
                            start--;
                            i--;
                            continue;
                        }
                        double grad1 = infoPoint.posZ - infoStart.posZ / (infoPoint.posX - infoStart.posX);
                        if (grad == grad1 && infoPoint.posY == infoStart.posY) {
                            infoEnd = infoPoint;
                            start--;
                            i--;
                            continue;
                        }
                        if (grad != 500.0D) {
                            break;
                        }
                        grad = grad1;
                        infoEnd = infoPoint;
                        i--;
                    }
                    if (infoEnd != null) {
                        i += 2;
                        double grad1 = infoStart.posX - mc.getRenderManager().renderPosX;
                        double posY = infoStart.posY - mc.getRenderManager().renderPosY;
                        double posZ = infoStart.posZ - mc.getRenderManager().renderPosZ;
                        double nextPosX = infoEnd.posX - mc.getRenderManager().renderPosX;
                        double nextPosY = infoEnd.posY - mc.getRenderManager().renderPosY;
                        double nextPosZ = infoEnd.posZ - mc.getRenderManager().renderPosZ;


                        Tessellator tessellator = Tessellator.getInstance();
                        GL11.glPushMatrix();
                        GL11.glTranslated(grad1, posY, posZ);
                        int ii = entity.getBrightnessForRender();
                        int j = ii % 65536;
                        int k = ii / 65536;
                        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
                        RenderHelper.disableStandardItemLighting();
                        GL11.glDisable(2896);
                        mc.renderEngine.bindTexture(texture);
                        BufferBuilder bufferbuilder = tessellator.getBuffer();
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(infoStart.startU, 1.0D).endVertex();
                        bufferbuilder.pos(0.0D, (0.0F + infoStart.height), 0.0D).tex(infoStart.startU, 0.0D).endVertex();

                        double endTex = infoEnd.startU - start + i;
                        if (endTex > infoStart.startU) {
                            endTex--;
                        }
                        double distX = infoStart.posX - infoEnd.posX;
                        double distZ = infoStart.posZ - infoEnd.posZ;

                        double scales = Math.sqrt(distX * distX + distZ * distZ) / infoStart.height;
                        while (scales > 1.0D) {
                            endTex++;
                            scales--;
                        }
                        double pos = nextPosX - grad1;
                        double pos1 = nextPosZ - posZ;

                        if (pos >= 6) {
                            pos = 6;
                        }
                        if (pos1 >= 6) {
                            pos1 = 6;
                        }
                        if (pos <= -6) {
                            pos = -6;
                        }
                        if (pos1 <= -6) {
                            pos1 = -6;
                        }
                        if (endTex >= 24) {
                            endTex = 24;
                        }
                        if (endTex <= -24) {
                            endTex = -24;
                        }

                        bufferbuilder.pos(pos, nextPosY - posY + infoEnd.height, pos1).tex(
                                endTex, 0.0D).endVertex();
                        bufferbuilder.pos(pos, nextPosY - posY, pos).tex(endTex, 1.0D).endVertex();
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
