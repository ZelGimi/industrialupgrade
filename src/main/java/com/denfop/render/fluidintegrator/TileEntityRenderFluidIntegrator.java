package com.denfop.render.fluidintegrator;

import com.denfop.blocks.FluidName;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.mechanism.TileEntityPrimalFluidIntegrator;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TileEntityRenderFluidIntegrator extends TileEntitySpecialRenderer<TileEntityPrimalFluidIntegrator> {

    float rotation;
    float prevRotation;

    public void render(
            TileEntityPrimalFluidIntegrator tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        renderTanks(tile, x, y, z);
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
            float scale = tile.fluidTank1.getFluidAmount() * 1F / tile.fluidTank1.getCapacity();
            scale = Math.min(1, scale);
            switch (tile.facing) {
                case 3:
                    GlStateManager.translate(0.2, 0.3 + 0.6 * scale, 0.5);
                    break;
                case 2:
                    GlStateManager.translate(0.75, 0.3 + 0.6 * scale, 0.5);
                    break;
                case 4:
                    GlStateManager.translate(0.5, 0.3 + 0.6 * scale, 0.2);
                    break;
                case 5:
                    GlStateManager.translate(0.5, 0.3 + 0.6 * scale, 0.75);
                    break;
            }
            if (scale > 0.1) {
                GL11.glRotatef(rotation, 1F, 1F, 1F);
            } else {
                GL11.glRotatef(90, 1F, 0F, 0F);
            }
            GlStateManager.scale(0.9, 0.9, 0.9);
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
        if (this.rendererDispatcher.cameraHitResult != null && (tile
                .getPos()
                .equals(this.rendererDispatcher.cameraHitResult.getBlockPos()) || tile
                .getPos().up()
                .equals(this.rendererDispatcher.cameraHitResult.getBlockPos()))) {
            this.setLightmapDisabled(true);
            String text3 = String.format("%d", (int) (tile.getProgress() * 100)) + "%";
            final TextComponentString itextcomponent2 = new TextComponentString(text3);
            this.drawNameplate(tile, itextcomponent2.getFormattedText(), x, y + 0.25, z, 12);
            this.setLightmapDisabled(false);
        }
        GlStateManager.pushMatrix();
        final ItemStack itemstack1 = tile.outputSlot.get();
        if (!itemstack1.isEmpty() && itemstack1.getCount() > 0) {
            GlStateManager.translate(x, y, z);
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
            switch (tile.facing) {
                case 3:
                    GlStateManager.translate(1, 0.87, 0.5);
                    break;
                case 2:
                    GlStateManager.translate(0, 0.87, 0.5);
                    break;
                case 4:
                    GlStateManager.translate(0.5, 0.87, 0.9);
                    break;
                case 5:
                    GlStateManager.translate(0.5, 0.87, -0.1);
                    break;
            }

            GlStateManager.rotate(90, 1, 0, 0);
            IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                    itemstack1,
                    tile.getWorld(),
                    null
            );

            IBakedModel transformedModel1 = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    ibakedmodel,
                    ItemCameraTransforms.TransformType.GROUND,
                    false
            );
            for (int i = 0; i < 1; i++) {
                GlStateManager.translate(0, 0, -0.0075);

                Minecraft.getMinecraft().getRenderItem().renderItem(itemstack1, transformedModel1);


            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            Minecraft
                    .getMinecraft()
                    .getRenderManager().renderEngine
                    .getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                    .restoreLastBlurMipmap();
        }
        GlStateManager.popMatrix();
        rotation = prevRotation + (rotation - prevRotation) * partialTicks;
        prevRotation = rotation;
        rotation += 0.25;
    }

    private void renderTanks(final TileEntityPrimalFluidIntegrator tile, final double x, final double y, final double z) {
        if (tile.fluidTank1.getFluid() != null && tile.fluidTank1.getFluid().getFluid() != null & tile.fluidTank1
                .getFluid()
                .getFluid()
                .getBlock() != null) {
            GL11.glPushMatrix();
            float dopY = 0;
            if (tile.fluidTank1.getFluid().getFluid().isGaseous()) {
                dopY = 0.88f - tile.fluidTank1.getFluidAmount() * 1f / tile.fluidTank1.getCapacity();
            }
            switch (tile.facing) {
                case 2:
                    GL11.glTranslated(x + 0.44, y + 0.3 + dopY, z + 0.19);
                    break;
                case 3:
                    GL11.glTranslated(x - 0.059, y + 0.3 + dopY, z + 0.19);
                    break;
                case 4:
                    GL11.glTranslated(x + 0.19, y + 0.3 + dopY, z - 0.059);
                    break;
                case 5:
                    GL11.glTranslated(x + 0.19, y + 0.3 + dopY, z + 0.44);
                    break;
            }

            final float scale = tile.fluidTank1.getFluidAmount() * 1F / tile.fluidTank1.getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.62, 0.75 * scale, 0.62);
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
                dopY = 0.7f - tile.fluidTank2.getFluidAmount() * 0.7F / tile.fluidTank2.getCapacity();
            }
            switch (tile.facing) {
                case 2:
                    GL11.glTranslated(x - 0.24, y + 0.15 + dopY, z + 0.26);
                    break;
                case 3:
                    GL11.glTranslated(x + 0.76, y + 0.15 + dopY, z + 0.26);
                    break;
                case 4:
                    GL11.glTranslated(x + 0.26, y + 0.15 + dopY, z + 0.76);
                    break;
                case 5:
                    GL11.glTranslated(x + 0.26, y + 0.15 + dopY, z - 0.24);
                    break;
            }
            final float scale = tile.fluidTank2.getFluidAmount() * 1F / tile.fluidTank2.getCapacity();
            GlStateManager.enableLighting();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.48, 0.7 * scale, 0.48);
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
