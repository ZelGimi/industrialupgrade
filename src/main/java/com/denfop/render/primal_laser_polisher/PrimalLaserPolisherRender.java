package com.denfop.render.primal_laser_polisher;

import com.denfop.tiles.mechanism.TileEntityPrimalLaserPolisher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class PrimalLaserPolisherRender extends TileEntitySpecialRenderer<TileEntityPrimalLaserPolisher> {

    public void render(
            TileEntityPrimalLaserPolisher tile,
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
        final ItemStack itemstack1 = tile.outputSlot.get();
        itemstack = itemstack1.isEmpty() ? itemstack : itemstack1;
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
            GlStateManager.translate(0.5, 0.88, 0.4);
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
    }

}
