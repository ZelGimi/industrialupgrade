package com.denfop.render.anvil;

import com.denfop.IUItem;
import com.denfop.tiles.base.IIsMolecular;
import com.denfop.tiles.base.TileEntityAnvil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import static net.minecraft.world.item.ItemDisplayContext.FIXED;
import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class RenderItemAnvil implements BlockEntityRenderer<TileEntityAnvil> {


    private final ItemRenderer itemRenderer;
    private ItemStack stack;
    private float rotation;
    private float prevRotation;

    public RenderItemAnvil(BlockEntityRendererProvider.Context p_173636_) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }
    private int transformModelCount(PoseStack poseStack, float partialTicks
    ) {

        int modelCount = 1;
        float offsetY = 1.0F;


        poseStack.translate(0.5, 0.25F * offsetY + 0.25, 0.5);


        poseStack.scale(0.4F, 0.4F, 0.4F);

        rotation = (prevRotation + (rotation - prevRotation) * (partialTicks)) % 360;

        prevRotation = rotation;
        rotation +=2F;

        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));


        return modelCount;
    }
    public void renderItem(ItemStack itemStack, Level level, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (itemStack.isEmpty()) {
            return;
        }

        BakedModel bakedModel = this.itemRenderer.getModel(itemStack,level,null,0);
        RandomSource random = level.random;
        if (bakedModel != null) {
            boolean isGui3d = bakedModel.isGui3d();

            poseStack.pushPose();
            poseStack.translate(0,2,0);

            int count =transformModelCount(poseStack, Minecraft.getInstance().getPartialTick());

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

                BakedModel transformedModel =net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(new PoseStack(),
                        bakedModel,
                        GROUND,
                        false
                );
                this.itemRenderer .render(itemStack,FIXED, false, poseStack, buffer, light, overlay, transformedModel);
                poseStack.popPose();
            }

            poseStack.popPose();
        }
    }
    @Override
    public void render(TileEntityAnvil tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack itemStack = tile.inputSlotA.get(0);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1, 0.3);

            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            itemRenderer.renderStatic(itemStack, GROUND,
                    combinedLight, combinedOverlay, poseStack, buffer,tile.getLevel(), 0);
            if (tile.facing == 5 || tile.facing == 4) {
                poseStack.translate(0, -1, -0.05);

            } else {
                poseStack.translate(-1, 0, -0.05);
            }
            for (int i = 0; i < itemStack.getCount() - 1; i++) {
                poseStack.translate(0, 0, -0.0075);
                itemRenderer.renderStatic(itemStack, GROUND,
                        combinedLight, combinedOverlay, poseStack, buffer,tile.getLevel(), 0);
            }
            poseStack.popPose();
        }
        poseStack.pushPose();
        if (!tile.active.isEmpty()){
            if (this.stack == null){
                this.stack = new ItemStack(IUItem.iuingot.getStack(10));
            }
            renderItem(stack,tile.getLevel(),poseStack,buffer,combinedLight,combinedOverlay);
        }
        poseStack.popPose();
         ItemStack outputStack = tile.outputSlot.get(0);
        if (!outputStack.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(1.3, 1.0, 0.4);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            if (tile.facing == 5 || tile.facing == 4) {
                poseStack.translate(-0.75, 1, 0.0);

            } else {
                poseStack.translate(0, 0, 0.0);
            }

            for (int i = 0; i < outputStack.getCount(); i++) {
                poseStack.translate(0, 0, -0.0075);
                itemRenderer.renderStatic(outputStack, GROUND,
                        combinedLight, combinedOverlay, poseStack, buffer,tile.getLevel(), 0);

            }


            poseStack.popPose();
        }
    }
}
