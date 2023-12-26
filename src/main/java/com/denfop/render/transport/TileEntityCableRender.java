package com.denfop.render.transport;

import com.denfop.render.base.BasicBakedBlockModel;
import com.denfop.render.base.ModelCuboidUtil;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class TileEntityCableRender<T extends TileEntityMultiCable> extends TileEntitySpecialRenderer<T>  {






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
            data = new DataCable(te.connectivity, null, ItemStack.EMPTY, null);
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
