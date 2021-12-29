package com.denfop.render.doublemoleculartransformer;

import com.denfop.Constants;
import com.denfop.api.render.IModelCustom;
import com.denfop.render.AdvancedModelLoader;
import com.denfop.tiles.base.TileEntityDoubleMolecular;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class TileEntityDoubleMolecularRender extends TileEntitySpecialRenderer<TileEntityDoubleMolecular> {

    public static final ResourceLocation texture = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt.png"
    );
    public static final ResourceLocation texture1 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt1.png"
    );
    public static final ResourceLocation texture2 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt2.png"
    );
    public static final ResourceLocation texture3 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt3.png"
    );
    public static final ResourceLocation texture4 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt4.png"
    );
    public static final ResourceLocation texture5 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt5.png"
    );
    public static final ResourceLocation texture6 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt6.png"
    );
    public static final ResourceLocation texture7 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt7.png"
    );
    public static final ResourceLocation active_texture = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt_active.png"
    );
    public static final ResourceLocation active_texture1 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt1_active.png"
    );
    public static final ResourceLocation active_texture2 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt2_active.png"
    );
    public static final ResourceLocation active_texture3 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt3_active.png"
    );
    public static final ResourceLocation active_texture4 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt4_active.png"
    );
    public static final ResourceLocation active_texture5 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt5_active.png"
    );
    public static final ResourceLocation active_texture6 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt6_active.png"
    );
    public static final ResourceLocation active_texture7 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/doublemt7_active.png"
    );
    static final IModelCustom model = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/DoubleMolecular.obj"));


    public void render(
            TileEntityDoubleMolecular tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(0.5F, 0.98F, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glBlendFunc(770, 771);

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(1.0F, 1F, 1.0F);
        if (!tile.getActive()) {
            switch (tile.redstoneMode) {
                case 0:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
                    break;
                case 1:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture1);
                    break;
                case 2:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture2);
                    break;
                case 3:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture3);
                    break;
                case 4:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture4);
                    break;
                case 5:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture5);
                    break;
                case 6:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture6);
                    break;
                case 7:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture7);
                    break;
            }
        } else {
            switch (tile.redstoneMode) {
                case 0:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(active_texture);
                    break;
                case 1:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(active_texture1);
                    break;
                case 2:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(active_texture2);
                    break;
                case 3:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(active_texture3);
                    break;
                case 4:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(active_texture4);
                    break;
                case 5:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(active_texture5);
                    break;
                case 6:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(active_texture6);
                    break;
                case 7:
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(active_texture7);
                    break;
            }
        }
        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

    }

}
