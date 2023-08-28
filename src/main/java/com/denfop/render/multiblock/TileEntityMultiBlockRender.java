package com.denfop.render.multiblock;

import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import javax.annotation.Nonnull;

public class TileEntityMultiBlockRender<T extends TileMultiBlockBase> extends TileEntitySpecialRenderer<T> {


    public void render(
            @Nonnull TileMultiBlockBase te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {

        GlStateManager.pushMatrix(); // Push the matrix to avoid unexpected behavior (we don't want to move an entire world)
        GlStateManager.translate(x + 0.5f, y, z + 0.5f); // The base model has an offset by default, so we move it a little
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        te.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.popMatrix(); // Pop the matrix

    }

}
