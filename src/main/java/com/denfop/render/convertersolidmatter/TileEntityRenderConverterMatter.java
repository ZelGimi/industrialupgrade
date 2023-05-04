package com.denfop.render.convertersolidmatter;

import com.denfop.Constants;
import com.denfop.api.render.IModelCustom;
import com.denfop.render.base.AdvancedModelLoader;
import com.denfop.tiles.base.TileEntityConverterSolidMatter;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class TileEntityRenderConverterMatter extends TileEntitySpecialRenderer<TileEntityConverterSolidMatter> {

    public static final ResourceLocation texture = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/SintezatorSolidVeshestv.png"
    );
    static final IModelCustom model = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/SintezatorSolidVeshestv.obj"));


    public void render(
            @Nonnull TileEntityConverterSolidMatter te,
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
        GL11.glRotatef(0F, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        bindTexture(texture);
        model.renderAll();
        GL11.glPopMatrix();

    }

}
