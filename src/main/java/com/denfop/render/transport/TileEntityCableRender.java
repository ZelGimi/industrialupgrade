package com.denfop.render.transport;

import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class TileEntityCableRender<T extends TileEntityMultiCable> extends TileEntitySpecialRenderer<T> {


    public void render(
            @Nonnull TileEntityMultiCable te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GlStateManager.pushMatrix();


        DataCable data = te.dataCable;
        if (data == null) {
            data = new DataCable(te.connectivity, ItemStack.EMPTY, null);
            te.dataCable = data;
        }


        GlStateManager.translate(x, y, z);
        if (te.stackFacade != null && !te.stackFacade.isEmpty()) {
            if (data.getItemStack() == ItemStack.EMPTY || !data.getItemStack().isItemEqual(te.stackFacade)) {
                data.setItemStack(te.stackFacade);
                RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
                IBakedModel itemModel = renderItem.getItemModelWithOverrides(data.getItemStack(), te.getWorld(), null);
                data.setBakedModel(itemModel);
            }

            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            renderBlocks(data);

        }
        GlStateManager.popMatrix();

    }


    private void renderBlocks(final DataCable data) {

        renderBlock(data);

    }

    private void renderBlock(DataCable item) {
        GlStateManager.translate(0.5, 0.5D, 0.5);
        GlStateManager.scale(1F, 1F, 1F);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItem(item.getItemStack(), item.getBakedModel());

    }

}
