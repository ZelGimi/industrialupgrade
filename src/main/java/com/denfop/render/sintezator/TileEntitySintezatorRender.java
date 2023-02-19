package com.denfop.render.sintezator;

import com.denfop.Constants;
import com.denfop.api.render.IModelCustom;
import com.denfop.render.base.AdvancedModelLoader;
import com.denfop.tiles.base.TileEntitySintezator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class TileEntitySintezatorRender extends TileEntitySpecialRenderer<TileEntitySintezator> {

    public static final ResourceLocation texture = new ResourceLocation(Constants.TEXTURES, "textures/models/sintezator.png");
    static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.TEXTURES,
            "models/sintezator.obj"
    ));

    public void render(@Nonnull TileEntitySintezator tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix(); // Push the matrix to avoid unexpected behavior (we don't want to move an entire world)

        GlStateManager.translate(x + 0.5f, y, z + 0.5f); // The base model has an offset by default, so we move it a little

        bindTexture(texture); // Bind the base texture
        model.renderAll(); // Render the base

        renderBlocks(tile); // Render little blocks inside

        GlStateManager.popMatrix(); // Pop the matrix
    }

    private void renderBlocks(TileEntitySintezator tile) {
        for (int i = 0; i < 9; i++) {
            ItemStack panelStack = tile.inputslot.get(i);

            if (panelStack == null || panelStack.isEmpty()) {
                continue;
            }

            GlStateManager.pushMatrix();

            renderBlock(panelStack, tile, i);

            GlStateManager.popMatrix();
        }
    }

    private void renderBlock(ItemStack item, TileEntitySintezator tile, int index) {
        GlStateManager.scale(0.125f, 0.125f, 0.125f); // Scaling down the block

        GlStateManager.translate(1.25, 1f, -1.25); // Moving into the first slot

        // Calculating offsets for the grid
        float gridOffset = 1.75f;

        int xOffset = (index / 3);

        float posX = -(xOffset * gridOffset);
        float posZ = gridOffset * ((index % 3));

        GlStateManager.translate(posX + 0.5, 0.5, posZ - 0.5); // Applying offsets

        // Rendering an item
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        IBakedModel itemModel = renderItem.getItemModelWithOverrides(item, tile.getWorld(), null);

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        renderItem.renderItem(item, itemModel);
    }
}
