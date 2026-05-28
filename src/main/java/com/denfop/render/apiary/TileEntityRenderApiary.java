package com.denfop.render.apiary;

import com.denfop.blockentity.bee.BlockEntityApiary;
import com.denfop.blockentity.bee.EnumProblem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientHooks;

import static net.minecraft.world.item.ItemDisplayContext.FIXED;
import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class TileEntityRenderApiary implements BlockEntityRenderer<BlockEntityApiary> {
    public static boolean register = false;
    private final BlockEntityRendererProvider.Context contex;
    private ItemStack flower;
    private float rotation;
    private float prevRotation;

    public TileEntityRenderApiary(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;

    }

    @Override
    public void render(BlockEntityApiary te, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        if (te.problemList != null && !te.problemList.isEmpty()) {
            if (!register) {
                register = true;
                this.flower = new ItemStack(Items.POPPY);
            }
            if (flower == null)
                this.flower = new ItemStack(Items.POPPY);
            if (te.problemList.contains(EnumProblem.FOOD) || te.problemList.contains(EnumProblem.JELLY))
                renderItem(flower, te.getLevel(), poseStack, bufferSource, packedLight, combinedOverlay, partialTicks);
        }
    }

    private int transformModelCount(PoseStack poseStack, float partialTicks
    ) {

        int modelCount = 1;
        float offsetY = 1.0F;


        poseStack.translate(0.5, 0.25F * offsetY + 0.25, 0.5);


        poseStack.scale(0.75F, 0.75F, 0.75F);

        rotation = (prevRotation + (rotation - prevRotation) * (partialTicks)) % 360;

        prevRotation = rotation;
        rotation += 2F;

        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));


        return modelCount;
    }

    public void renderItem(ItemStack itemStack, Level level, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, float partialTicks) {
        if (itemStack.isEmpty()) {
            return;
        }

        BakedModel bakedModel = contex.getItemRenderer().getModel(itemStack, level, null, 0);
        RandomSource random = level.random;
        if (bakedModel != null) {
            boolean isGui3d = bakedModel.isGui3d();

            poseStack.pushPose();
            poseStack.translate(0, 1, 0);
            int count = transformModelCount(poseStack, partialTicks);

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

                BakedModel transformedModel = ClientHooks.handleCameraTransforms(new PoseStack(),
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
}
