package com.denfop.render.oilquarry;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.render.IModelCustom;
import com.denfop.api.vein.Type;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.render.base.AdvancedModelLoader;
import com.denfop.tiles.base.TileEntityQuarryVein;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityQuarryOilRender extends TileEntitySpecialRenderer<TileEntityQuarryVein> {

    public static final ResourceLocation texture = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/quarryoil.png"
    );
    public static final ResourceLocation texture1 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/quarryoil_1.png"
    );
    public static final ResourceLocation texture2 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/quarryoil_2.png"
    );
    public static final ResourceLocation texture3 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/quarryoil_3.png"
    );
    static final IModelCustom model = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/quarryoil.obj"));
    static final IModelCustom ore = AdvancedModelLoader
            .loadModel(new ResourceLocation(Constants.TEXTURES, "models/ore.obj"));
    float rotation;
    float prevRotation;


    public void render(
            TileEntityQuarryVein tile,
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
        switch (tile.level) {
            case 2:
                bindTexture(texture1);
                break;
            case 3:
                bindTexture(texture2);
                break;
            case 4:
                bindTexture(texture3);
                break;
            default:
                bindTexture(texture);
                break;
        }


        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        if (tile.vein != null && tile.vein.get()) {
            if (tile.vein.getType() != Type.EMPTY) {
                GL11.glPushMatrix();
                GL11.glTranslated(x, y, z);
                GL11.glTranslatef(0.45F, 0, 0.5F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glRotatef(rotation, 0F, 1F, 0F);
                BlockRendererDispatcher ren = Minecraft.getMinecraft().getBlockRendererDispatcher();
                IBlockState state;
                if (tile.vein.getType() == Type.VEIN) {
                    state = IUItem.heavyore.getDefaultState().withProperty(
                            BlockHeavyOre.VARIANT,
                            BlockHeavyOre.Type.getFromID(tile.vein.getMeta())
                    );
                } else {
                    state = IUItem.oilblock.getDefaultState();
                }
                final String texture1 = ren
                        .getModelForState(state)
                        .getQuads(state, EnumFacing.NORTH, 0)
                        .get(0)
                        .getSprite().getIconName() + ".png";
                String dom = texture1.substring(0, texture1.indexOf(":"));
                String path = "textures/" + texture1.substring(texture1.indexOf(":") + 1);
                final ResourceLocation resorce = new ResourceLocation(dom, path);
                bindTexture(resorce);
                ore.renderAll();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
                rotation = prevRotation + (rotation - prevRotation) * partialTicks;
                prevRotation = rotation;
                rotation += 0.25;
            }
        }


    }

}
