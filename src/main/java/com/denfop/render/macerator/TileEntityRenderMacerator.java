package com.denfop.render.macerator;

import com.denfop.IUItem;
import com.denfop.tiles.mechanism.TileEntityMacerator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientHooks;

import static net.minecraft.world.item.ItemDisplayContext.FIXED;
import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class TileEntityRenderMacerator implements BlockEntityRenderer<TileEntityMacerator> {

    private final BlockEntityRendererProvider.Context contex;
    private ItemStack stack;
    private float rotation;
    private float prevRotation;

    public TileEntityRenderMacerator(BlockEntityRendererProvider.Context context) {
        this.contex = context;
    }

    @Override
    public void render(TileEntityMacerator tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        if (tile.durability == 0) {
            if (this.stack == null) {
                this.stack = new ItemStack(IUItem.crafting_elements.getStack(41));
            }
            renderItem(stack, tile.getLevel(), poseStack, buffer, combinedLight, combinedOverlay, partialTicks);
        }
        poseStack.popPose();
        ItemStack input = tile.inputSlotA.get(0);
        if (!input.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.55, tile.facing == 4 || tile.facing == 5 ? 0.31 : 0.3);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            itemRenderer.renderStatic(
                    input,
                    GROUND,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    buffer, tile.getLevel(),
                    0
            );
            poseStack.popPose();
        }

        ItemStack output = tile.outputSlot.get(0);
        if (!output.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.55, 0.4);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            itemRenderer.renderStatic(
                    output,
                    GROUND,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    buffer, tile.getLevel(),
                    0
            );
            poseStack.popPose();
        }
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

    public void renderItem(ItemStack itemStack, Level level, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, float partialTicks) {
        if (itemStack.isEmpty()) {
            return;
        }

        BakedModel bakedModel = this.contex.getItemRenderer().getModel(itemStack, level, null, 0);
        RandomSource random = level.random;
        if (bakedModel != null) {
            boolean isGui3d = bakedModel.isGui3d();

            poseStack.pushPose();
            poseStack.translate(0, 2, 0);

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
