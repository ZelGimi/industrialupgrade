package com.denfop.render.solargenerator;

import com.denfop.Constants;
import com.denfop.api.render.IModelCustom;
import com.denfop.render.AdvancedModelLoader;
import com.denfop.tiles.se.TileImpSolarGenerator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityImpSolarEnergyRender extends TileEntitySpecialRenderer<TileImpSolarGenerator> {

    static final IModelCustom model = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.MOD_ID, "models/PreobrazovatlSunRays3.obj"));
    public static final ResourceLocation texture = new ResourceLocation(
            Constants.MOD_ID,
            "textures/models/PreobrazovatlSunRays2.png"
    );


    public void render(
            TileImpSolarGenerator te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(0.5F, 0F, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glRotatef(0F, 0.0F, 0F, 0F);
        bindTexture(texture);

        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

}
