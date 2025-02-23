package com.denfop.render.squeezer;

import com.denfop.tiles.mechanism.TileEntitySqueezer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TileEntityRenderSqueezer extends TileEntitySpecialRenderer<TileEntitySqueezer> {

    public void render(
            TileEntitySqueezer tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        ItemStack itemstack = tile.inputSlotA.get();
        if (!itemstack.isEmpty()) {
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
            if (tile.facing == 4) {
                GlStateManager.translate(0.48, 0.875, -0.125);
            }
            if (tile.facing == 5) {
                GlStateManager.translate(0.52, 0.875, 0.89);
            }
            if (tile.facing == 3) {
                GlStateManager.translate(0, 0.875, 0.48);
            }
            if (tile.facing == 2) {
                GlStateManager.translate(1, 0.875, 0.3);
            }
            GlStateManager.rotate(90, 1, 0, 0);
            IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                    itemstack,
                    tile.getWorld(),
                    null
            );

            IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    ibakedmodel,
                    ItemCameraTransforms.TransformType.GROUND,
                    false
            );
            Minecraft.getMinecraft().getRenderItem().renderItem(itemstack, transformedModel);
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            Minecraft
                    .getMinecraft()
                    .getRenderManager().renderEngine
                    .getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                    .restoreLastBlurMipmap();
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (tile.fluidTank1.getFluidAmount() > 0) {
            if (tile.facing == 4) {
                GlStateManager.translate(0.07, 0.16, 0.6);
            }
            if (tile.facing == 5) {
                GlStateManager.translate(0.07, 0.16, -0.4);
            }
            if (tile.facing == 2) {
                GlStateManager.translate(-0.4, 0.16d, 0.07);
            }
            if (tile.facing == 3) {
                GlStateManager.translate(0.6, 0.16d, 0.07);
            }
            final float scale = tile.fluidTank1.getFluidAmount() * 1F / tile.fluidTank1.getCapacity();

            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.8, 1.45 * scale, 0.8);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = tile.fluidTank1.getFluid();
            IBlockState state = tile.fluidTank1.getFluid().getFluid().getBlock().getDefaultState();
            IBakedModel model = blockRenderer.getModelForState(state);
            GL11.glColor4f((fluidStack.getFluid().getColor(fluidStack) >> 16 & 255) / 255.0F,
                    (fluidStack.getFluid().getColor(fluidStack) >> 8 & 255) / 255.0F,
                    (fluidStack.getFluid().getColor(fluidStack) & 255) / 255.0F, 1.0F
            );

            for (EnumFacing enumfacing : EnumFacing.values()) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                int i = 0;
                final List<BakedQuad> listQuads = model.getQuads(state, enumfacing, 0L);
                for (int j = listQuads.size(); i < j; ++i) {
                    BakedQuad bakedquad = listQuads.get(i);


                    bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
                    bufferbuilder.addVertexData(bakedquad.getVertexData());


                    bufferbuilder.putColorRGB_F4(
                            (fluidStack.getFluid().getColor(fluidStack) >> 16 & 255) / 255.0F,
                            (fluidStack.getFluid().getColor(fluidStack) >> 8 & 255) / 255.0F,
                            (fluidStack.getFluid().getColor(fluidStack) & 255) / 255.0F
                    );


                    Vec3i vec3i = bakedquad.getFace().getDirectionVec();
                    bufferbuilder.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
                    tessellator.draw();
                }
            }

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            int i = 0;
            final List<BakedQuad> listQuads = model.getQuads(state, null, 0L);
            for (int j = listQuads.size(); i < j; ++i) {
                BakedQuad bakedquad = listQuads.get(i);


                bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
                bufferbuilder.addVertexData(bakedquad.getVertexData());


                bufferbuilder.putColorRGB_F4(
                        (fluidStack.getFluid().getColor(fluidStack) >> 16 & 255) / 255.0F,
                        (fluidStack.getFluid().getColor(fluidStack) >> 8 & 255) / 255.0F,
                        (fluidStack.getFluid().getColor(fluidStack) & 255) / 255.0F
                );


                Vec3i vec3i = bakedquad.getFace().getDirectionVec();
                bufferbuilder.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
                tessellator.draw();
            }
            GlStateManager.disableLighting();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
    }

}
