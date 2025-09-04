package com.denfop.render.oilquarry;

import com.denfop.IUItem;
import com.denfop.api.vein.common.Type;
import com.denfop.blockentity.base.BlockEntityQuarryVein;
import com.denfop.blocks.FluidName;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class QuarryOilRenderer implements BlockEntityRenderer<BlockEntityQuarryVein> {

    private final BlockEntityRendererProvider.Context contex;
    private float rotation = 0;
    private float prevRotation = 0;

    public QuarryOilRenderer(BlockEntityRendererProvider.Context context) {
        this.contex = context;
    }

    @Override
    public void render(BlockEntityQuarryVein tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int packedLight, int packedOverlay) {

        if (tile.vein == null || !tile.vein.get()) return;

        if (tile.vein.getType() != Type.EMPTY) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.35D, 0.5D);
            poseStack.scale(0.25f, 0.25f, 0.25f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(rotation));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotation));

            BlockState state = null;
            if (state == null) {
                if (tile.vein.getType() == Type.VEIN) {
                    state = tile.vein.isOldMineral()
                            ? IUItem.heavyore.getStateFromMeta(tile.vein.getMeta())
                            : IUItem.mineral.getStateFromMeta(tile.vein.getMeta());
                } else {
                    state = tile.vein.getType() == Type.OIL ? IUItem.oilblock.getBlock(0).defaultBlockState() : IUItem.gasBlock.getBlock(0).defaultBlockState();
                }
            }


            BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            BakedModel model = dispatcher.getBlockModel(state);

            dispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(RenderType.cutout()),
                    state, model, 1.0f, 1.0f, 1.0f, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();

            rotation += 0.25f;
            String stack = null;
            if (tile.vein.getType() == Type.VEIN) {
                if (tile.vein.isOldMineral()) {
                    stack = Localization.translate(new ItemStack(IUItem.heavyore.getItem(tile.vein.getMeta()), 1).getDescriptionId());
                } else {
                    stack = Localization.translate(new ItemStack(IUItem.mineral.getItem(tile.vein.getMeta()), 1).getDescriptionId());
                }

            } else if (tile.vein.getType() == Type.OIL) {
                stack = Localization.translate(new ItemStack(IUItem.oilblock.getItem()).getDescriptionId());
            } else if (tile.vein.getType() == Type.GAS) {
                stack = Localization.translate(FluidName.fluidgas.getInstance().get().getFluidType().getDescriptionId());

            }

            final int col = tile.vein.getCol();
            final boolean isOil = tile.vein.getType() == Type.OIL || tile.vein.getType() == Type.GAS;
            int colmax = tile.vein.getMaxCol();

            int variety = tile.vein.getMeta() / 3;
            int type = tile.vein.getMeta() % 3;
            String varietyString = variety == 0 ? "iu.sweet_oil" : "iu.sour_oil";
            String typeString = type == 0 ? "iu.light_oil" : type == 1 ? "iu.medium_oil" : "iu.heavy_oil";
            Component itextcomponent;
            if (isOil) {
                if (tile.vein.getType() != Type.GAS) {
                    itextcomponent =
                            Component.literal(Localization.translate(varietyString) + " " + Localization.translate(
                                    typeString)).append(Component.literal(" " + stack));
                } else {
                    itextcomponent = Component.literal(
                            stack);
                }
            } else {
                itextcomponent = Component.literal(
                        stack);


            }


            Component itextcomponent1 = Component.literal(col + (isOil ? "mb" :
                    "") + "/" + colmax + (
                    isOil
                            ?
                            "mb"
                            : ""));
            poseStack.translate(0.5, 2.15f, 0.5);
            drawNameplate(itextcomponent, poseStack, bufferSource, packedLight);
            poseStack.translate(0, -0.25f, 0);
            drawNameplate(itextcomponent1, poseStack, bufferSource, packedLight);
        }


    }


    private void drawNameplate(Component text, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(contex.getEntityRenderer().cameraOrientation());
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = poseStack.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 24;
        Font font = contex.getFont();
        float f2 = (float) (-font.width(text) / 2);
        font.drawInBatch(text, f2, (float) 0, 553648127, false, matrix4f, buffer, false, j, packedLight);
        if (true) {
            font.drawInBatch(text, f2, (float) 0, -1, false, matrix4f, buffer, false, 0, packedLight);
        }
        poseStack.popPose();
    }
}
