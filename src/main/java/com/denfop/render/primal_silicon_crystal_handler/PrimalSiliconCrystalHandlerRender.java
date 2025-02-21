package com.denfop.render.primal_silicon_crystal_handler;

import com.denfop.tiles.mechanism.TileEntityPrimalSiliconCrystalHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.text.TextComponentString;

public class PrimalSiliconCrystalHandlerRender extends TileEntitySpecialRenderer<TileEntityPrimalSiliconCrystalHandler> {

    public void render(
            TileEntityPrimalSiliconCrystalHandler tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GlStateManager.popMatrix();
        if (this.rendererDispatcher.cameraHitResult != null && (tile
                .getPos()
                .equals(this.rendererDispatcher.cameraHitResult.getBlockPos()) || tile
                .getPos().up()
                .equals(this.rendererDispatcher.cameraHitResult.getBlockPos()))) {
            this.setLightmapDisabled(true);
            String text3 = tile.timer.getTime();
            final TextComponentString itextcomponent2 = new TextComponentString(text3);
            this.drawNameplate(tile, itextcomponent2.getFormattedText(), x, y + 0.5, z, 12);
            this.setLightmapDisabled(false);
        }
        GlStateManager.pushMatrix();
    }

}
