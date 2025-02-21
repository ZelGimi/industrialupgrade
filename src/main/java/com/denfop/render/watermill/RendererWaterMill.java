package com.denfop.render.watermill;

import com.denfop.tiles.mechanism.TileEntityWaterMill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RendererWaterMill {

    private float time;

    public void render(TileEntityWaterMill te) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(te.getPos().getX() - 1, te.getPos().getY() - 1, te.getPos().getZ() - 1);

        if (!Minecraft.getMinecraft().isGamePaused()) {
            time += 0.00025f;
        }

        GlStateManager.scale(3, 3, 3);
        RenderHelper.enableGUIStandardItemLighting();

        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher blockRenderer = mc.getBlockRendererDispatcher();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.enableCull();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        blockRenderer.renderBlock(te.dataBlock.getBlockState(), BlockPos.ORIGIN, mc.world, buffer);
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.scale(1 / 3f, 1 / 3f, 1 / 3f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();


    }

    @SideOnly(Side.CLIENT)
    public void render(IBakedModel model, IBlockState state, EnumFacing enumfacing) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int i = 0;
        final List<BakedQuad> listQuads = model.getQuads(state, enumfacing, 0L);
        for (int j = listQuads.size(); i < j; ++i) {
            BakedQuad bakedquad = listQuads.get(i);


            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
            bufferbuilder.addVertexData(bakedquad.getVertexData());


            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            bufferbuilder.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            tessellator.draw();
        }

    }

}
