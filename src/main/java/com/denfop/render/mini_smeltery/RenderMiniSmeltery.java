package com.denfop.render.mini_smeltery;

import com.denfop.tiles.mechanism.TileEntityMiniSmeltery;
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
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.function.Function;

public class RenderMiniSmeltery {
    @SideOnly(Side.CLIENT)
    public static void render(TileEntityMiniSmeltery te){
        GlStateManager.pushMatrix();
        if ( te.fluidTank1.getFluidAmount()  - 144 > 0) {
            final float scale = (te.fluidTank1.getFluidAmount() - 144) * 1F / te.fluidTank1.getCapacity();

            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translate(0.1,0,0.1);
            GlStateManager.scale(0.82, 1.2 * scale, 0.82);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = te.fluidTank1.getFluid();
            IBlockState state = te.fluidTank1.getFluid().getFluid().getBlock().getDefaultState();
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
        GlStateManager.pushMatrix();
        if (te.outputSlot.isEmpty() && te.fluidTank1.getFluidAmount() > 0) {
            final float scale = te.fluidTank1.getFluidAmount() * 1F / te.fluidTank1.getCapacity();

            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translate(0.04,0.86,0.04);
            GlStateManager.scale(0.95, 0.15, 0.95);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = te.fluidTank1.getFluid();
            IBlockState state = te.fluidTank1.getFluid().getFluid().getBlock().getDefaultState();
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
        GlStateManager.pushMatrix();
        ItemStack itemstack = te.outputSlot.get();

        if (!itemstack.isEmpty()) {

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.translate(0.5,0.93,0.25);
            if (te.getFacing() == EnumFacing.EAST || te.getFacing() == EnumFacing.WEST) {
                GlStateManager.translate(0.25,0,0.25);


            }
            GlStateManager.rotate(90, 1, 0, 0);
            if (te.getFacing() == EnumFacing.EAST || te.getFacing() == EnumFacing.WEST) {
                GlStateManager.rotate(90, 0, 0, 1);
            }
            GlStateManager.scale(2,2,2);
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );




            IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                    itemstack,
                    te.getWorld(),
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
    }
}
