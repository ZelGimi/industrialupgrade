package com.denfop.render.fluidheater;

import com.denfop.tiles.mechanism.TileEntityPrimalFluidHeater;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.text.TextComponentString;

public class TileEntityRenderFluidHeater extends TileEntitySpecialRenderer<TileEntityPrimalFluidHeater> {

    public void render(
            TileEntityPrimalFluidHeater tile,
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
            if (tile.fluidTank1.getFluid() == null) {
                text = "FluidTank: 0/" + tile.fluidTank1.getCapacity();
            } else {
                text =
                        tile.fluidTank1
                                .getFluid()
                                .getLocalizedName() + ":" + tile.fluidTank1.getFluidAmount() + "/" + tile.fluidTank1.getCapacity();
            }
            String text1;
            if (tile.fluidTank2.getFluid() == null) {
                text1 = "FluidTank: 0/" + tile.fluidTank2.getCapacity();
            } else {
                text1 =
                        tile.fluidTank2
                                .getFluid()
                                .getLocalizedName() + ":" + tile.fluidTank2.getFluidAmount() + "/" + tile.fluidTank2.getCapacity();
            }
            String text2 = String.format("%d", (int) (tile.getProgress() * 100)) + "%";
            final TextComponentString itextcomponent = new TextComponentString(text);
            final TextComponentString itextcomponent1 = new TextComponentString(text1);
            final TextComponentString itextcomponent2 = new TextComponentString(text2);
            this.drawNameplate(tile, itextcomponent.getFormattedText(), x, y + 0.75, z, 12);
            this.drawNameplate(tile, itextcomponent1.getFormattedText(), x, y + 0.5, z, 12);
            this.drawNameplate(tile, itextcomponent2.getFormattedText(), x, y + 0.25, z, 12);
            this.setLightmapDisabled(false);
        }
    }

}
