package com.denfop.render.advoilrefiner;

import com.denfop.blocks.FluidName;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.mechanism.TileAdvOilRefiner;
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

public class TileEntityAdvOilRefinerRender extends TileEntitySpecialRenderer<TileAdvOilRefiner> {


    public void render(
            TileAdvOilRefiner tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        if (tile.getFluidTank(0).getFluid() != null) {
            GL11.glPushMatrix();
            float dopY = 0;
            if (tile.getFluidTank(0).getFluid().getFluid().isGaseous()) {
                dopY = 1f - tile.getFluidTank(0).getFluidAmount() * 1F / tile.getFluidTank(0).getCapacity();
            }
            if (tile.facing == 2) {
                GL11.glTranslated(x + 0.437, y + 0.25 + dopY, z - 0.0125);
            }
            if (tile.facing == 3) {
                GL11.glTranslated(x + 0.437, y + 0.25 + dopY, z + 0.8825);
            }
            if (tile.facing == 4) {
                GL11.glTranslated(x - 0.0125, y + 0.25 + dopY, z + 0.437);
            }
            if (tile.facing == 5) {
                GL11.glTranslated(x + 0.8825, y + 0.25 + dopY, z + 0.437);
            }
            final float scale = tile.getFluidTank(0).getFluidAmount() * 1F / tile.getFluidTank(0).getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.125, 1 * scale, 0.125);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = tile.getFluidTank(0).getFluid();
            if (tile.dataFluid == null) {
                tile.dataFluid = new DataFluid(fluidStack.getFluid());
            }
            IBlockState state = tile.getFluidTank(0).getFluid().getFluid().getBlock().getDefaultState();
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
        if (tile.getFluidTank(1).getFluid() != null) {
            GL11.glPushMatrix();
            float dopY = 0;
            if (tile.getFluidTank(1).getFluid().getFluid().isGaseous()) {
                dopY = 1f - tile.getFluidTank(1).getFluidAmount() * 1F / tile.getFluidTank(1).getCapacity();
            }
            if (tile.facing == 2) {
                GL11.glTranslated(x + 0.8225, y + 0.25 + dopY, z + 0.25);
            }
            if (tile.facing == 3) {
                GL11.glTranslated(x + 0.8225, y + 0.25 + dopY, z + 0.626);
            }
            if (tile.facing == 4) {
                GL11.glTranslated(x + 0.25, y + 0.25 + dopY, z + 0.8225);
            }
            if (tile.facing == 5) {
                GL11.glTranslated(x + 0.626, y + 0.25 + dopY, z + 0.8225);
            }
            final float scale = tile.getFluidTank(1).getFluidAmount() * 1F / tile.getFluidTank(1).getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.125, 1 * scale, 0.125);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = tile.getFluidTank(1).getFluid();
            if (tile.dataFluid1 == null) {
                tile.dataFluid1 = new DataFluid(fluidStack.getFluid());
            }
            IBlockState state = tile.getFluidTank(1).getFluid().getFluid().getBlock().getDefaultState();
            if (FluidRegistry.LAVA == fluidStack.getFluid()) {
                state = FluidName.fluidlava.getInstance().getBlock().getDefaultState();
            }
            if (FluidRegistry.WATER == fluidStack.getFluid()) {
                state = FluidName.fluidwater.getInstance().getBlock().getDefaultState();
            }
            IBakedModel model;
            if (tile.dataFluid1.getState() == null) {
                model = blockRenderer.getModelForState(state);
                tile.dataFluid1.setState(model);
            } else if (tile.dataFluid1.getFluid() != fluidStack.getFluid()) {
                tile.dataFluid1.setFluid(fluidStack.getFluid());
                model = blockRenderer.getModelForState(state);
                tile.dataFluid1.setState(model);
            } else {
                model = tile.dataFluid1.getState();
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

        if (tile.getFluidTank(2).getFluid() != null) {
            GL11.glPushMatrix();
            float dopY = 0;
            if (tile.getFluidTank(2).getFluid().getFluid().isGaseous()) {
                dopY = 1f - tile.getFluidTank(2).getFluidAmount() * 1F / tile.getFluidTank(2).getCapacity();
            }
            if (tile.facing == 2) {
                GL11.glTranslated(x + 0.0495, y + 0.25 + dopY, z + 0.25);
            }
            if (tile.facing == 3) {
                GL11.glTranslated(x + 0.0495, y + 0.25 + dopY, z + 0.626);
            }
            if (tile.facing == 4) {
                GL11.glTranslated(x + 0.25, y + 0.25 + dopY, z + 0.0495);
            }
            if (tile.facing == 5) {
                GL11.glTranslated(x + 0.626, y + 0.25 + dopY, z + 0.0495);
            }
            final float scale = tile.getFluidTank(2).getFluidAmount() * 1F / tile.getFluidTank(2).getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.125, 1 * scale, 0.125);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = tile.getFluidTank(2).getFluid();
            if (tile.dataFluid2 == null) {
                tile.dataFluid2 = new DataFluid(fluidStack.getFluid());
            }
            IBlockState state = tile.getFluidTank(2).getFluid().getFluid().getBlock().getDefaultState();
            if (FluidRegistry.LAVA == fluidStack.getFluid()) {
                state = FluidName.fluidlava.getInstance().getBlock().getDefaultState();
            }
            if (FluidRegistry.WATER == fluidStack.getFluid()) {
                state = FluidName.fluidwater.getInstance().getBlock().getDefaultState();
            }
            IBakedModel model;
            if (tile.dataFluid2.getState() == null) {
                model = blockRenderer.getModelForState(state);
                tile.dataFluid2.setState(model);
            } else if (tile.dataFluid2.getFluid() != fluidStack.getFluid()) {
                tile.dataFluid2.setFluid(fluidStack.getFluid());
                model = blockRenderer.getModelForState(state);
                tile.dataFluid2.setState(model);
            } else {
                model = tile.dataFluid2.getState();
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
