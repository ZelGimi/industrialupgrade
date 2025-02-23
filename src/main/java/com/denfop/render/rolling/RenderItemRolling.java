package com.denfop.render.rolling;

import com.denfop.tiles.mechanism.TileEntityRollingMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class RenderItemRolling extends TileEntitySpecialRenderer<TileEntityRollingMachine> {

    public void render(
            TileEntityRollingMachine tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        ItemStack itemstack = tile.inputSlotA.get();
        if (!itemstack.isEmpty()) {
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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
            if (!tile.getActive()) {
                GlStateManager.translate(0.5, 0.8, 0.4);
                if (tile.facing == 5 || tile.facing == 4) {
                    GlStateManager.translate(0, 0, 0.02);
                }
                GlStateManager.scale(0.65, 0.65, 0.65);
            } else {
                GlStateManager.translate(0.49, 0.8, 0.3);
                if (tile.facing == 5 || tile.facing == 4) {
                    GlStateManager.translate(0, 0, 0.15);
                    GlStateManager.scale(1.5, 0.4, 0.4);

                } else {
                    GlStateManager.scale(0.4, 0.4, 1.5);
                }
            }

            GlStateManager.rotate(90, 1, 0, 0);
            IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                    itemstack,
                    tile.getWorld(),
                    null
            );

            IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    ibakedmodel,
                    ItemCameraTransforms.TransformType.GROUND,
                    false
            );
            Minecraft.getMinecraft().getRenderItem().renderItem(itemstack, transformedModel);


            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            Minecraft
                    .getMinecraft()
                    .getRenderManager().renderEngine
                    .getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                    .restoreLastBlurMipmap();
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (!itemstack.isEmpty()) {
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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

            IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                    itemstack,
                    tile.getWorld(),
                    null
            );

            IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    ibakedmodel,
                    ItemCameraTransforms.TransformType.GROUND,
                    false
            );


            if (itemstack.getCount() > 1) {
                if (tile.facing == 5 || tile.facing == 4) {
                    GlStateManager.translate(1, 1.775, 0.15);
                    GlStateManager.rotate(90, 1, 0, 0);
                } else {
                    GlStateManager.translate(1.85, 1.775, 1);
                    GlStateManager.rotate(90, 1, 0, 0);
                }
                for (int i = 0; i < itemstack.getCount() - 1; i++) {
                    GlStateManager.translate(0, 0, -0.0075);
                    Minecraft.getMinecraft().getRenderItem().renderItem(itemstack, transformedModel);


                }
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            Minecraft
                    .getMinecraft()
                    .getRenderManager().renderEngine
                    .getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                    .restoreLastBlurMipmap();
        }
        GlStateManager.popMatrix();


        GlStateManager.pushMatrix();
        final ItemStack itemstack1 = tile.outputSlot.get();
        if (!itemstack1.isEmpty() && itemstack1.getCount() > 0) {
            GlStateManager.translate(x, y, z);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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

            if (tile.facing != 5 && tile.facing != 4) {
                GlStateManager.translate(0.19, 1.01, 0.5);
            } else {
                GlStateManager.translate(0.49, 1.01, 0.81);
            }

            GlStateManager.rotate(90, 1, 0, 0);
            if (tile.facing != 5 && tile.facing != 4) {
                GlStateManager.rotate(90, 0, 0, 1);
            }
            IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                    itemstack1,
                    tile.getWorld(),
                    null
            );

            IBakedModel transformedModel1 = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    ibakedmodel,
                    ItemCameraTransforms.TransformType.GROUND,
                    false
            );
            for (int i = 0; i < itemstack1.getCount(); i++) {
                GlStateManager.translate(0, 0, -0.0075);

                Minecraft.getMinecraft().getRenderItem().renderItem(itemstack1, transformedModel1);


            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            Minecraft
                    .getMinecraft()
                    .getRenderManager().renderEngine
                    .getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                    .restoreLastBlurMipmap();
        }
        GlStateManager.popMatrix();
    }

}
