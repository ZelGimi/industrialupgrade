package com.denfop.render.pump;

import com.denfop.tiles.mechanism.TileEntityPrimalPump;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.text.TextComponentString;

public class TileEntityRenderPump extends TileEntitySpecialRenderer<TileEntityPrimalPump> {

    public void render(
            TileEntityPrimalPump tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        if (this.rendererDispatcher.cameraHitResult != null && tile
                .getPos()
                .equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
            this.setLightmapDisabled(true);
            String text;
            if (tile.fluidTank.getFluid() == null) {
                text = "FluidTank: 0/" + tile.fluidTank.getCapacity();
            } else {
                text =
                        tile.fluidTank
                                .getFluid()
                                .getLocalizedName() + ":" + tile.fluidTank.getFluidAmount() + "/" + tile.fluidTank.getCapacity();
            }

            final TextComponentString itextcomponent = new TextComponentString(text);
            this.drawNameplate(tile, itextcomponent.getFormattedText(), x, y + 0.25, z, 12);
            this.setLightmapDisabled(false);
        }
    }

}
