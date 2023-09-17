package com.denfop.render.transport;

import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class TileEntityCableRender<T extends TileEntityMultiCable> extends TileEntitySpecialRenderer<T> {


    private ResourceLocation textures;


    public void render(
            @Nonnull TileEntityMultiCable te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {

        this.textures = te.getTexture();


        DataCable data = te.dataCable;
        if (data == null) {
            byte connect = te.connectivity;
            final ModelBaseCable model = new ModelBaseCable(connect);
            data = new DataCable(te.connectivity, model, ItemStack.EMPTY, null);
            te.dataCable = data;
        }
        byte connect = te.connectivity;
        if (data.getConnect() != connect) {
            final ModelBaseCable model = new ModelBaseCable(connect);
            data.setConnect(te.connectivity);
            data.setModelCables(model);
        }
        this.bindTexture(this.textures);
         GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        data.getModelCables().render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);




        if (te.stackFacade != null && !te.stackFacade.isEmpty()) {
            if (data.getItemStack() == ItemStack.EMPTY || !data.getItemStack().isItemEqual(te.stackFacade)) {
                data.setItemStack(te.stackFacade);
                RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
                IBakedModel itemModel = renderItem.getItemModelWithOverrides(data.getItemStack(), te.getWorld(), null);
                data.setBakedModel(itemModel);
            }
            GlStateManager.pushMatrix();
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            renderBlocks(data);
            GlStateManager.popMatrix();
        } else {
            if (data.getItemStack() != ItemStack.EMPTY) {
                data.setItemStack(ItemStack.EMPTY);
                data.setBakedModel(null);
            }
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
