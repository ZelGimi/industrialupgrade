package com.denfop.render.compressor;


import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityCompressor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.minecraft.world.item.ItemDisplayContext.FIXED;
import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class TileEntityRenderCompressor implements BlockEntityRenderer<BlockEntityCompressor> {
    private final BlockEntityRendererProvider.Context contex;
    private ItemStack stack;
    private float rotation;
    private float prevRotation;

    public TileEntityRenderCompressor(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    private int transformModelCount(PoseStack poseStack, float partialTicks
    ) {

        int modelCount = 1;
        float offsetY = 1.0F;


        poseStack.translate(0.5, 0.25F * offsetY + 0.25, 0.5);


        poseStack.scale(0.4F, 0.4F, 0.4F);

        rotation = (prevRotation + (rotation - prevRotation) * (partialTicks)) % 360;

        prevRotation = rotation;
        rotation += 2F;

        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));


        return modelCount;
    }

    public void renderItem(ItemStack itemStack, Level level, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (itemStack.isEmpty()) {
            return;
        }

        BakedModel bakedModel = this.contex.getItemRenderer().getModel(itemStack, level, null, 0);
        RandomSource random = level.random;
        if (bakedModel != null) {
            boolean isGui3d = bakedModel.isGui3d();

            poseStack.pushPose();
            poseStack.translate(0, 2, 0);

            int count = transformModelCount(poseStack, Minecraft.getInstance().getPartialTick());

            for (int i = 0; i < 1; ++i) {
                poseStack.pushPose();

                if (isGui3d) {
                    if (i > 0) {
                        float xOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float yOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float zOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        poseStack.translate(xOffset, yOffset, zOffset);
                    }
                } else {
                    if (i > 0) {
                        float xOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.075F;
                        float yOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.075F;
                        poseStack.translate(xOffset, yOffset, 0.0F);
                    }
                }

                BakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(new PoseStack(),
                        bakedModel,
                        GROUND,
                        false
                );
                contex.getItemRenderer().render(itemStack, FIXED, false, poseStack, buffer, light, overlay, transformedModel);
                poseStack.popPose();
            }

            poseStack.popPose();
        }
    }

    @Override
    public void render(BlockEntityCompressor te, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        ItemStack itemstack = te.outputSlot.get(0);
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            if ((itemstack.getItem() instanceof BlockItem)) {
                if (te.facing == 4 || te.facing == 5) {
                    poseStack.translate(0.5, 0.41, 0.31);
                } else {
                    poseStack.translate(0.5, 0.41, 0.3);
                }
            } else {
                poseStack.translate(0.5, 0.42, 0.37501);
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            contex.getItemRenderer().renderStatic(itemstack, GROUND,
                    packedLight, combinedOverlay, poseStack, bufferSource, te.getLevel(), 0);
            poseStack.popPose();
        }
        poseStack.pushPose();
        if (te.durability == 0) {
            if (this.stack == null) {
                this.stack = new ItemStack(IUItem.crafting_elements.getStack(76));
            }
            renderItem(stack, te.getLevel(), poseStack, bufferSource, packedLight, combinedOverlay);
        }
        poseStack.popPose();
        itemstack = te.inputSlotA.get(0);
        if (!itemstack.isEmpty()) {
            poseStack.pushPose();
            if ((itemstack.getItem() instanceof BlockItem)) {
                if (te.facing == 4 || te.facing == 5) {
                    poseStack.translate(0.5, 0.41, 0.31);
                } else {
                    poseStack.translate(0.5, 0.41, 0.3);
                }
            } else {
                poseStack.translate(0.5, 0.42, 0.37501);
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            contex.getItemRenderer().renderStatic(itemstack, GROUND,
                    packedLight, combinedOverlay, poseStack, bufferSource, te.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
