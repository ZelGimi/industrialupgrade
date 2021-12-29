package com.denfop.render.tank;

import com.denfop.Constants;
import com.denfop.api.render.IModelCustom;
import com.denfop.render.AdvancedModelLoader;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityTankRender extends TileEntitySpecialRenderer<TileEntityLiquedTank> {

    static final IModelCustom model = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/tank.obj"));

    static final IModelCustom model1 = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/oil.obj"));


    public void render(
            TileEntityLiquedTank tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(0.6F, 0.51F, 0.7F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glRotatef(0F, 0.0F, 0F, 0F);
        GL11.glScalef(1F, 0.8F, 1F);
        bindTexture(tile.texture);
        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        if (tile.getFluidTank().getFluid() != null) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        double m1 = (tile.gaugeLiquidScaled(0.51));
        GL11.glTranslatef(0.6F, (float) m1, 0.7F);

        GL11.glRotatef(0F, 0.0F, 0F, 0F);
        double m = (tile.gaugeLiquidScaled(0.8));
        m = Math.min(0.8, m);
        GL11.glScalef(1F, (float) m, 1F);
            final ResourceLocation tex = tile.getFluidTank().getFluid().getFluid().getStill();
           final ResourceLocation resorce = new ResourceLocation(tex.getResourceDomain(),"textures/"+tex.getResourcePath()+
                   ".png");
            bindTexture(resorce);
            model1.renderAll();


        GL11.glPopMatrix();
        }
    }

}
