package com.denfop.render.tank;

import com.denfop.blocks.FluidName;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TileEntityTankRender extends TileEntitySpecialRenderer<TileEntityLiquedTank> {


    public void render(
            TileEntityLiquedTank tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {


        if (tile.getFluidTank().getFluid() != null && tile.getFluidTank().getFluid().getFluid() != null & tile
                .getFluidTank()
                .getFluid()
                .getFluid()
                .getBlock() != null) {
            GL11.glPushMatrix();
            float dopY = 0;
            if (tile.getFluidTank().getFluid().getFluid().isGaseous()) {
                dopY = 0.9f - tile.fluidTank.getFluidAmount() * 1F / tile.fluidTank.getCapacity();
            }
            GL11.glTranslated(x + 0.1, y+0.01  + dopY, z + 0.1);
            final float scale = tile.fluidTank.getFluidAmount() * 1F / tile.fluidTank.getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.8, 0.95 * scale, 0.8);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = tile.fluidTank.getFluid();
            if (tile.dataFluid == null) {
                tile.dataFluid = new DataFluid(fluidStack.getFluid());
            }
            IBlockState state = fluidStack.getFluid().getBlock().getDefaultState();
            if (FluidRegistry.LAVA == fluidStack.getFluid()) {
                state = FluidName.fluidlava.getInstance().getBlock().getDefaultState();
            }
            if (FluidRegistry.WATER == fluidStack.getFluid()) {
                state = FluidName.fluidwater.getInstance().getBlock().getDefaultState();
            }
            IBakedModel model;
            if (tile.dataFluid.getState() == null) {
                model = blockRenderer.getModelForState(state);
                tile.dataFluid.setState(model);
            } else if (tile.dataFluid.getFluid() != fluidStack.getFluid()) {
                tile.dataFluid.setFluid(fluidStack.getFluid());
                model = blockRenderer.getModelForState(state);
                tile.dataFluid.setState(model);
            } else {
                model = tile.dataFluid.getState();
            }

            GL11.glColor4f((fluidStack.getFluid().getColor(fluidStack) >> 16 & 255) / 255.0F,
                    (fluidStack.getFluid().getColor(fluidStack) >> 8 & 255) / 255.0F,
                    (fluidStack.getFluid().getColor(fluidStack) & 255) / 255.0F, 1.0F
            );

            for (EnumFacing enumfacing : EnumFacing.values()) {
                render(fluidStack, model, state, enumfacing);
            }

            render(fluidStack, model, state, null);
            GlStateManager.disableLighting();
            GlStateManager.disableColorMaterial();
            GL11.glPopMatrix();
        }
    }

    public void render(FluidStack fluidStack, IBakedModel model, IBlockState state, EnumFacing enumfacing) {

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

}
