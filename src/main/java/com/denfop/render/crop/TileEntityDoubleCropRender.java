package com.denfop.render.crop;

import com.denfop.render.base.BakedBlockModel;
import com.denfop.tiles.crop.TileEntityCrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.function.Function;

public class TileEntityDoubleCropRender extends TileEntitySpecialRenderer<TileEntityCrop> {

    public static Function createFunction(TileEntityCrop tile) {
        Function function = o -> {
            if (tile.upDataBlock != null) {
                GL11.glPushMatrix();
                GL11.glTranslated(tile.getPos().getX(), tile.getPos().getY() + 1, tile.getPos().getZ());

                RenderHelper.enableStandardItemLighting();
                GlStateManager.depthMask(true);
                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                render((BakedBlockModel) tile.upDataBlock.getState(), tile.upDataBlock.getBlockState(), null);

                GL11.glPopMatrix();
            }
            return 0;
        };
        return function;
    }

    public static void render(BakedBlockModel model, IBlockState state, EnumFacing enumfacing) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int i = 0;
        final List<BakedQuad> listQuads = ((IBakedModel) model).getQuads(state, enumfacing, 0L);
        for (int j = listQuads.size(); i < j; ++i) {
            BakedQuad bakedquad = listQuads.get(i);


            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
            bufferbuilder.addVertexData(bakedquad.getVertexData());


            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            bufferbuilder.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            tessellator.draw();
        }

    }

    public void render(
            TileEntityCrop tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {


    }

}
