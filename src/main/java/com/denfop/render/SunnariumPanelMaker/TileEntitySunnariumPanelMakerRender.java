package com.denfop.render.SunnariumPanelMaker;

import com.denfop.Constants;
import com.denfop.api.render.IModelCustom;
import com.denfop.render.AdvancedModelLoader;
import com.denfop.tiles.mechanism.TileEntitySunnariumPanelMaker;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntitySunnariumPanelMakerRender extends TileEntitySpecialRenderer<TileEntitySunnariumPanelMaker> {

    public static final ResourceLocation texture = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/ProizvSanariumChunks.png"
    );
    static final IModelCustom model = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/ProizvSanariumChunks.obj"));

    public void render(
            TileEntitySunnariumPanelMaker tile,
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
