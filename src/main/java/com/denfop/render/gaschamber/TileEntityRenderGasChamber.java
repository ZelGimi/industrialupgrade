package com.denfop.render.gaschamber;

import com.denfop.blocks.FluidName;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.mechanism.TileEntityPrimalGasChamber;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TileEntityRenderGasChamber extends TileEntitySpecialRenderer<TileEntityPrimalGasChamber> {

    public void render(
            TileEntityPrimalGasChamber tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        renderTanks(tile, x, y, z);
        if (this.rendererDispatcher.cameraHitResult != null && (tile
                .getPos()
                .equals(this.rendererDispatcher.cameraHitResult.getBlockPos()) || tile
                .getPos().up()
                .equals(this.rendererDispatcher.cameraHitResult.getBlockPos()))) {
            this.setLightmapDisabled(true);
            String text;
            if (tile.fluidTank1.getFluid() == null) {
                text = "FluidTank: 0/" + tile.fluidTank1.getCapacity();
            } else {
                text =
                        tile.fluidTank1
                                .getFluid()
                                .getLocalizedName() + ":" + tile.fluidTank1.getFluidAmount() + "/" + tile.fluidTank1.getCapacity();
            }
            String text1;
            if (tile.fluidTank2.getFluid() == null) {
                text1 = "FluidTank: 0/" + tile.fluidTank2.getCapacity();
            } else {
                text1 =
                        tile.fluidTank2
                                .getFluid()
                                .getLocalizedName() + ":" + tile.fluidTank2.getFluidAmount() + "/" + tile.fluidTank2.getCapacity();
            }
            String text2;
            if (tile.fluidTank3.getFluid() == null) {
                text2 = "FluidTank: 0/" + tile.fluidTank3.getCapacity();
            } else {
                text2 =
                        tile.fluidTank3
                                .getFluid()
                                .getLocalizedName() + ":" + tile.fluidTank3.getFluidAmount() + "/" + tile.fluidTank3.getCapacity();
            }
            String text3 = String.format("%d", (int) (tile.getProgress() * 100)) + "%";
            final TextComponentString itextcomponent = new TextComponentString(text);
            final TextComponentString itextcomponent1 = new TextComponentString(text1);
            final TextComponentString itextcomponent2 = new TextComponentString(text3);
            final TextComponentString itextcomponent3 = new TextComponentString(text2);
            this.drawNameplate(tile, itextcomponent.getFormattedText(), x, y + 1.5, z, 12);
            this.drawNameplate(tile, itextcomponent1.getFormattedText(), x, y + 1.25, z, 12);
            this.drawNameplate(tile, itextcomponent2.getFormattedText(), x, y + 1, z, 12);
            this.drawNameplate(tile, itextcomponent3.getFormattedText(), x, y + 0.75, z, 12);
            this.setLightmapDisabled(false);
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

    private void renderTanks(final TileEntityPrimalGasChamber tile, final double x, final double y, final double z) {
        if (tile.fluidTank1.getFluid() != null && tile.fluidTank1.getFluid().getFluid() != null & tile.fluidTank1
                .getFluid()
                .getFluid()
                .getBlock() != null) {
            GL11.glPushMatrix();
            float dopY = 0;
            if (tile.fluidTank1.getFluid().getFluid().isGaseous()) {
                dopY = 0.94f - tile.fluidTank1.getFluidAmount() * 1F / tile.fluidTank1.getCapacity();
            }
            if (tile.facing == 2 || tile.facing == 3) {
                GL11.glTranslated(x + 0.67, y + 1 + dopY, z + 0.022);
            } else {
                GL11.glTranslated(x + 0.022, y + 1 + dopY, z + 0.67);
            }
            final float scale = tile.fluidTank1.getFluidAmount() * 1F / tile.fluidTank1.getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            if (tile.facing == 2 || tile.facing == 3) {
                GlStateManager.scale(0.38, 0.95 * scale, 0.95);
            } else {
                GlStateManager.scale(0.95, 0.95 * scale, 0.38);
            }
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = tile.fluidTank1.getFluid();
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
        if (tile.fluidTank2.getFluid() != null && tile.fluidTank2.getFluid().getFluid() != null & tile.fluidTank2
                .getFluid()
                .getFluid()
                .getBlock() != null) {
            GL11.glPushMatrix();
            float dopY = 0;
            if (tile.fluidTank2.getFluid().getFluid().isGaseous()) {
                dopY = 0.94f - tile.fluidTank2.getFluidAmount() * 1F / tile.fluidTank2.getCapacity();
            }
            if (tile.facing == 2 || tile.facing == 3) {
                GL11.glTranslated(x, y + 1 + dopY, z + 0.022);
            } else {
                GL11.glTranslated(x + 0.022, y + 1 + dopY, z);
            }
            final float scale = tile.fluidTank2.getFluidAmount() * 1F / tile.fluidTank2.getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            if (tile.facing == 2 || tile.facing == 3) {
                GlStateManager.scale(0.38, 0.95 * scale, 0.95);
            } else {
                GlStateManager.scale(0.95, 0.95 * scale, 0.38);
            }
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = tile.fluidTank2.getFluid();
            if (tile.dataFluid1 == null) {
                tile.dataFluid1 = new DataFluid(fluidStack.getFluid());
            }
            IBlockState state = fluidStack.getFluid().getBlock().getDefaultState();
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
        if (tile.fluidTank3.getFluid() != null && tile.fluidTank3.getFluid().getFluid() != null & tile.fluidTank3
                .getFluid()
                .getFluid()
                .getBlock() != null) {
            GL11.glPushMatrix();
            float dopY = 0;
            if (tile.fluidTank3.getFluid().getFluid().isGaseous()) {
                dopY = 0.94f - tile.fluidTank3.getFluidAmount() * 1F / tile.fluidTank3.getCapacity();
            }
            GL11.glTranslated(x + 0.025, y + 0.05 + dopY, z + 0.025);
            final float scale = tile.fluidTank3.getFluidAmount() * 1F / tile.fluidTank3.getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.95, 0.95 * scale, 0.95);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final FluidStack fluidStack = tile.fluidTank3.getFluid();
            if (tile.dataFluid2 == null) {
                tile.dataFluid2 = new DataFluid(fluidStack.getFluid());
            }
            IBlockState state = fluidStack.getFluid().getBlock().getDefaultState();
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

}
