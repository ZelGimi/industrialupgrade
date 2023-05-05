package com.denfop.render.streak;

import com.denfop.Constants;
import com.denfop.items.armour.ItemArmorImprovemedQuantum;
import com.denfop.utils.StreakLocationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Objects;

public class EntityRendererStreak extends Render<EntityStreak> {

    private static final ResourceLocation texture = new ResourceLocation(Constants.TEXTURES_ITEMS + "effect.png");

    public EntityRendererStreak(final RenderManager renderManager) {
        super(renderManager);
    }

    public static float clamp_float(float p_76131_0_, float p_76131_1_, float p_76131_2_) {
        return p_76131_0_ < p_76131_1_ ? p_76131_1_ : (Math.min(p_76131_0_, p_76131_2_));
    }

    public void doRender(@Nonnull EntityStreak entity, double x, double y, double z, float entityYaw, float partialTicks) {
        renderStreak(entity, partialTicks);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(@Nonnull EntityStreak p_110775_1_) {

        return texture;
    }

    private void renderStreak(EntityStreak entity, final float par6) {
        if (entity.parent instanceof AbstractClientPlayer && !entity.isInvisible()) {
            this.bindTexture(Objects.requireNonNull(this.getEntityTexture(entity)));
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
                GlStateManager.pushMatrix();
                GL11.glDisable(2884);
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GlStateManager.blendState.srcFactor = 770;
                GlStateManager.blendState.dstFactor = 771;
                GlStateManager.shadeModel(7425);
                float startGrad = 5.0F - par6;
                float endGrad = 20.0F - par6;
                for (int i = loc.size() - 2; i >= 0; i--) {
                    int start = i;

                    StreakLocationUtils infoStart = loc.get(i);
                    float startAlpha = (i < endGrad) ? clamp_float(0.8F * i / endGrad, 0.0F, 0.8F)
                            : ((i > (loc.size() - 2) - startGrad)
                                    ? clamp_float(0.8F * (loc.size() - 2 - i) / startGrad, 0.0F, 0.8F)
                                    : 0.8F);
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
                        float endAlpha = (i < endGrad) ? clamp_float(0.8F * (i - 1) / endGrad, 0.0F, 0.8F)
                                : ((i > (loc.size() - 1) - startGrad)
                                        ? clamp_float(0.8F * (loc.size() - 1 - i) / startGrad, 0.0F, 0.8F)
                                        : 0.8F);
                        double grad1 = infoStart.posX - this.renderManager.renderPosX;
                        double posY = infoStart.posY - this.renderManager.renderPosY;
                        double posZ = infoStart.posZ - this.renderManager.renderPosZ;
                        double nextPosX = infoEnd.posX - this.renderManager.renderPosX;
                        double nextPosY = infoEnd.posY - this.renderManager.renderPosY;
                        double nextPosZ = infoEnd.posZ - this.renderManager.renderPosZ;


                        Tessellator tessellator = Tessellator.getInstance();
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(grad1, posY, posZ);
                        int light = entity.world.getCombinedLight(new BlockPos(entity.posX, entity.posY, entity.posZ), 0);
                        int blockLight = light % 65536;
                        int skyLight = light / 65536;
                        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) blockLight, (float) skyLight);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GlStateManager.disableLighting();
                        GlStateManager.enableDepth();
                        BufferBuilder bufferbuilder = tessellator.getBuffer();
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                        GlStateManager.color(1, 1, 1, startAlpha);
                        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(infoStart.startU, 1.0D).endVertex();

                        bufferbuilder.pos(0.0D, (0.0F + infoStart.height), 0.0D).tex(infoStart.startU, 0.0D).endVertex();
                        GlStateManager.color(1, 1, 1, endAlpha);

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
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GlStateManager.enableLighting();
                        RenderHelper.enableStandardItemLighting();
                        GlStateManager.color(0.1F, 1, 0.1F, 1);
                        GlStateManager.popMatrix();
                    }
                }
                GlStateManager.shadeModel(7424);
                GL11.glDisable(3042);
                GL11.glEnable(3008);
                GL11.glEnable(2884);
                GlStateManager.popMatrix();
            }
        }
    }

}
