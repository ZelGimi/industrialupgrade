package com.denfop.render.oilrefiner;

import com.denfop.Constants;
import com.denfop.api.render.IModelCustom;
import com.denfop.render.base.AdvancedModelLoader;
import com.denfop.tiles.mechanism.TileEntityOilRefiner;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityOilRefinerRender extends TileEntitySpecialRenderer<TileEntityOilRefiner> {

    public static final ResourceLocation texture = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/oilrefiner.png"
    );
    public static final ResourceLocation texture1 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/blocks/fluid/neft_still.png"
    );
    public static final ResourceLocation texture2 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/blocks/fluid/dizel_still.png"
    );
    public static final ResourceLocation texture3 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/blocks/fluid/benz_still.png"
    );
    static final IModelCustom model = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/oilrefiner.obj"));
    static final IModelCustom model1 = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/oil.obj"));
    static final IModelCustom model2 = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/oil1.obj"));
    static final IModelCustom model3 = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/oil2.obj"));

    public void getRotate( TileEntityOilRefiner tile){

        if(tile.getFacing() == EnumFacing.SOUTH){
            GL11.glRotatef(180F, 0.0F, 1F, 0F);
            GL11.glTranslatef(0.19F, 0,-0.05f);
            return;
        }
        if(tile.getFacing() == EnumFacing.WEST){
            GL11.glRotatef(90F, 0.0F, 1F, 0F);
            GL11.glTranslatef(0.08F, 0,-0.15f);
            return;
        }
        if(tile.getFacing() == EnumFacing.EAST){
            GL11.glRotatef(-90F, 0.0F, 1F, 0F);
            GL11.glTranslatef(0.1F, 0,0.13f);
            return;
        }
    }
    public void render(
            TileEntityOilRefiner tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(0.6F, 0.51F, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        getRotate(tile);

        GL11.glScalef(1F, 0.8F, 1F);
        bindTexture(texture);
        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        double m1 = (tile.gaugeLiquidScaled(0.51));
        GL11.glTranslatef(0.6F, (float) ((float) m1 + 0.002), 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        getRotate(tile);
        double m = (tile.gaugeLiquidScaled(0.8));
        m = Math.min(0.8, m);
        GL11.glScalef(1F, (float) m, 1F);
        bindTexture(texture1);
        if (m1 != 0) {
            model1.renderAll();
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        m1 = (tile.gaugeLiquidScaled1(0.51));
        GL11.glTranslatef(0.6F, (float) ((float) m1 + 0.002), 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        getRotate(tile);
        m = (tile.gaugeLiquidScaled1(0.8));
        m = Math.min(0.8, m);
        GL11.glScalef(1F, (float) m, 1F);
        bindTexture(texture3);
        if (m1 != 0) {
            model3.renderAll();
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        m1 = (tile.gaugeLiquidScaled2(0.51));
        GL11.glTranslatef(0.6F, (float) ((float) m1 + 0.002), 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        getRotate(tile);
        m = (tile.gaugeLiquidScaled2(0.8));
        m = Math.min(0.8, m);
        GL11.glScalef(1F, (float) m, 1F);
        bindTexture(texture2);
        if (m1 != 0) {
            model2.renderAll();
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

    }


}
