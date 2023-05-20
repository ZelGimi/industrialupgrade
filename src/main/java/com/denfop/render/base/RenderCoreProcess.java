package com.denfop.render.base;


import com.denfop.Constants;
import com.denfop.tiles.base.IIsMolecular;
import com.denfop.tiles.mechanism.worlcollector.TileEntityCrystallize;
import ic2.core.block.TileEntityBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.*;

public class RenderCoreProcess<T extends TileEntityBlock> extends TileEntitySpecialRenderer<T> {

    private static final ResourceLocation plazmaTextloc = new ResourceLocation(Constants.MOD_ID, "textures/models/plazma.png");

    private static final ResourceLocation particlesTextloc = new ResourceLocation(
            Constants.MOD_ID,
            "textures/models/particles.png"
    );

    private static final Map<List<Serializable>, Integer> textureSizeCache = new HashMap<>();
    private final Random random = new Random();
    public int ticker;
    float rotation;
    float prevRotation;

    public static int getTextureSize(String s, int dv) {
        Integer textureSize = textureSizeCache.get(Arrays.asList(s, dv));
        if (textureSize != null) {
            return textureSize;
        }
        try {
            InputStream inputstream = Minecraft
                    .getMinecraft()
                    .getResourceManager()
                    .getResource(new ResourceLocation(Constants.MOD_ID, s))
                    .getInputStream();
            BufferedImage bi = ImageIO.read(inputstream);
            int size = bi.getWidth() / dv;
            textureSizeCache.put(Arrays.asList(new Serializable[]{s, dv}), size);
            return size;
        } catch (Exception var5) {
            var5.printStackTrace();
            return 16;
        }
    }

    public void renderCore(T te, double x, double y, double z) {
        this.ticker++;
        if (this.ticker > 161) {
            this.ticker = 1;
        }
        int size1 = getTextureSize("textures/models/plazma.png", 64);
        int size2 = getTextureSize("textures/models/particles.png", 32);
        if (te instanceof IIsMolecular) {
            if (((IIsMolecular) te).getMode() != 0) {
                size2 = getTextureSize("textures/models/particles" + ((IIsMolecular) te).getMode() + ".png", 32);
            }
        }
        float f1 = ActiveRenderInfo.getRotationX();
        float f2 = ActiveRenderInfo.getRotationXZ();
        float f3 = ActiveRenderInfo.getRotationZ();
        float f4 = ActiveRenderInfo.getRotationYZ();
        float f5 = ActiveRenderInfo.getRotationXY();
        float scaleCore = 0.35F;
        float posX = (float) x + 0.5F;
        float posY = (float) y + 0.5F;
        float posZ = (float) z + 0.5F;
        Color color = new Color(12648447);
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(plazmaTextloc);
        int i = this.ticker % 16;
        float size4 = (size1 * 4);
        float float_sizeMinus0_01 = size1 - 0.01F;
        float x0 = ((i % 4 * size1) + 0.0F) / size4;
        float x1 = ((i % 4 * size1) + float_sizeMinus0_01) / size4;
        float x2 = ((i / 4F * size1) + 0.0F) / size4;
        float x3 = ((i / 4F * size1) + float_sizeMinus0_01) / size4;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer
                .pos((posX - f1 * scaleCore - f4 * scaleCore), (posY - f2 * scaleCore), (posZ - f3 * scaleCore - f5 * scaleCore))
                .tex(x1, x3)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        buffer
                .pos((posX - f1 * scaleCore + f4 * scaleCore), (posY + f2 * scaleCore), (posZ - f3 * scaleCore + f5 * scaleCore))
                .tex(x1, x2)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        buffer
                .pos((posX + f1 * scaleCore + f4 * scaleCore), (posY + f2 * scaleCore), (posZ + f3 * scaleCore + f5 * scaleCore))
                .tex(x0, x2)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        buffer
                .pos((posX + f1 * scaleCore - f4 * scaleCore), (posY - f2 * scaleCore), (posZ + f3 * scaleCore - f5 * scaleCore))
                .tex(x0, x3)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        if (te instanceof IIsMolecular) {
            if (((IIsMolecular) te).getMode() != 0) {
                (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(new ResourceLocation(
                        Constants.MOD_ID,
                        "textures/models/particles" + ((IIsMolecular) te).getMode() + ".png"
                ));
            } else {
                (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(particlesTextloc);
            }
        } else {
            (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(particlesTextloc);
        }

        int qq = this.ticker % 16;
        i = 24 + qq;
        float size8 = (size2 * 8);
        float_sizeMinus0_01 = size2 - 0.01F;
        x0 = ((i % 8 * size2) + 0.0F) / size8;
        x1 = ((i % 8 * size2) + float_sizeMinus0_01) / size8;
        x2 = ((i / 8F * size2) + 0.0F) / size8;
        x3 = ((i / 8F * size2) + float_sizeMinus0_01) / size8;
        float var11 = MathHelper.sin(this.ticker / 10.0F) * 0.1F;
        scaleCore = 0.4F + var11;
        buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer
                .pos((posX - f1 * scaleCore - f4 * scaleCore), (posY - f2 * scaleCore), (posZ - f3 * scaleCore - f5 * scaleCore))
                .tex(x1, x3)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        buffer
                .pos((posX - f1 * scaleCore + f4 * scaleCore), (posY + f2 * scaleCore), (posZ - f3 * scaleCore + f5 * scaleCore))
                .tex(x1, x2)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        buffer
                .pos((posX + f1 * scaleCore + f4 * scaleCore), (posY + f2 * scaleCore), (posZ + f3 * scaleCore + f5 * scaleCore))
                .tex(x0, x2)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        buffer
                .pos((posX + f1 * scaleCore - f4 * scaleCore), (posY - f2 * scaleCore), (posZ + f3 * scaleCore - f5 * scaleCore))
                .tex(x0, x3)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glBlendFunc(770, 0);
    }


    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        if (te.getActive()) {
            if (te instanceof IIsMolecular) {
                GL11.glPushMatrix();
                renderItem((IIsMolecular) te, x, y, z, partialTicks);
                GL11.glPopMatrix();
            }
            GL11.glPushMatrix();
            renderCore(te, x, y, z);
            GL11.glPopMatrix();

        }
    }

    public boolean shouldSpreadItems() {
        return true;
    }

    private int transformModelCount(
            IIsMolecular itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_,
            float p_177077_8_,
            IBakedModel p_177077_9_
    ) {
        ItemStack itemstack = itemIn.getItemStack();
        Item item = itemstack.getItem();

        if (item == Items.AIR) {
            return 0;
        } else {
            int i = this.getModelCount(itemstack);
            float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;

            if (!(itemIn instanceof TileEntityCrystallize)) {
                GlStateManager.translate((float) p_177077_2_ + 0.5, (float) p_177077_4_ + 0.25F * f2 + 0.2,
                        (float) p_177077_6_ + 0.5
                );
            } else {
                GlStateManager.translate((float) p_177077_2_ + 0.5, (float) p_177077_4_ + 0.25F * f2 + 0.37,
                        (float) p_177077_6_ + 0.5
                );
            }
            GlStateManager.scale(0.5F, 0.5f, 0.5f);
            GL11.glRotatef(rotation, 0F, 1F, 0F);
            rotation = prevRotation + (rotation - prevRotation) * p_177077_8_;
            prevRotation = rotation;
            rotation += 0.25;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    protected int getModelCount(ItemStack stack) {
        int i = 1;

        if (stack.getCount() > 48) {
            i = 5;
        } else if (stack.getCount() > 32) {
            i = 4;
        } else if (stack.getCount() > 16) {
            i = 3;
        } else if (stack.getCount() > 1) {
            i = 2;
        }

        return i;
    }

    private void renderItem(IIsMolecular te, double x, double y, double z, float partialTicks) {
        ItemStack itemstack = te.getItemStack();
        int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
        this.random.setSeed(i);
        boolean flag = false;

        if (this.bindEntityTexture(te.getEntityBlock())) {
            Minecraft
                    .getMinecraft()
                    .getRenderManager().renderEngine
                    .getTexture(this.getEntityTexture(te.getEntityBlock()))
                    .setBlurMipmap(false
                            , false);
            flag = true;
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                itemstack,
                te.getEntityBlock().getWorld(),
                null
        );
        int j = this.transformModelCount(te, x, y, z, partialTicks, ibakedmodel);
        boolean flag1 = ibakedmodel.isGui3d();

        if (!flag1) {
            float f3 = -0.0F * (float) (j - 1) * 0.5F;
            float f4 = -0.0F * (float) (j - 1) * 0.5F;
            float f5 = -0.09375F * (float) (j - 1) * 0.5F;
            GlStateManager.translate(f3, f4, f5);
        }


        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();
            if (flag1) {

                if (k > 0) {
                    float f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f9 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(shouldSpreadItems() ? f7 : 0, shouldSpreadItems() ? f9 : 0, f6);
                }

                IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                        ibakedmodel,
                        ItemCameraTransforms.TransformType.GROUND,
                        false
                );
                Minecraft.getMinecraft().getRenderItem().renderItem(itemstack, transformedModel);
                GlStateManager.popMatrix();
            } else {

                if (k > 0) {
                    float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    GlStateManager.translate(f8, f10, 0.0F);
                }

                IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                        ibakedmodel,
                        ItemCameraTransforms.TransformType.GROUND,
                        false
                );
                Minecraft.getMinecraft().getRenderItem().renderItem(itemstack, transformedModel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.09375F);
            }
        }


        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(te.getEntityBlock());

        if (flag) {
            Minecraft
                    .getMinecraft()
                    .getRenderManager().renderEngine
                    .getTexture(this.getEntityTexture(te.getEntityBlock()))
                    .restoreLastBlurMipmap();
        }
    }

    protected boolean bindEntityTexture(TileEntityBlock entity) {
        ResourceLocation resourcelocation = this.getEntityTexture(entity);

        if (resourcelocation == null) {
            return false;
        } else {
            this.bindTexture(resourcelocation);
            return true;
        }
    }

    protected ResourceLocation getEntityTexture(TileEntityBlock entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

}
