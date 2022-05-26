package com.quantumgenerators;


import com.denfop.api.render.IModelCustom;
import com.denfop.render.AdvancedModelLoader;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderQG extends TileEntitySpecialRenderer<TileEntityQuantumGenerator> {

    public static final ResourceLocation texture= null;
    static final IModelCustom model = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/qg.obj"));


    public void render(
            TileEntityQuantumGenerator tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        ResourceLocation texture = new ResourceLocation(
                Constants.TEXTURES,
                "textures/blocks/"+tile.texture+".png"
        );
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        bindTexture(texture);
        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();



    }

}
