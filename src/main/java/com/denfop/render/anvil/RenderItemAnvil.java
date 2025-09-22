package com.denfop.render.anvil;

import com.denfop.tiles.base.TileEntityAnvil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class RenderItemAnvil extends TileEntitySpecialRenderer<TileEntityAnvil> {

    public void render(
            TileEntityAnvil tile,
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
            GlStateManager.translate(0.5, 1, 0.3);
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


            if (itemstack.getCount() > 1) {
                if (tile.facing == 5 || tile.facing == 4) {
                    GlStateManager.translate(0, -1.75, 0);

                } else {
                    GlStateManager.translate(-1.75, 0, 0);
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
                GlStateManager.translate(1.3, 1, 0.4);
            } else {

                GlStateManager.translate(0.5, 1, 1.3);
            }
            GlStateManager.rotate(90, 1, 0, 0);
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
