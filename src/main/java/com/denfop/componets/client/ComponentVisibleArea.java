package com.denfop.componets.client;

import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.componets.AbstractComponent;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.render.base.RenderType;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class ComponentVisibleArea extends AbstractComponent {
    public AABB aabb;
    private boolean visible;

    public ComponentVisibleArea(BlockEntityBase parent) {
        super(parent);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("component.visible_area.info"));
    }

    @Override
    public CompoundTag writeToNbt() {
        CompoundTag tag = super.writeToNbt();
        tag.putBoolean("visible", visible);
        return tag;
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        super.readFromNbt(nbt);
        visible = nbt.getBoolean("visible");
    }

    @Override
    public void onNetworkUpdate(final CustomPacketBuffer is) throws IOException {
        super.onNetworkUpdate(is);
        this.visible = is.readBoolean();
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeBoolean(this.visible);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    @Override
    public CustomPacketBuffer updateComponent() {
        CustomPacketBuffer packetBuffer = super.updateComponent();
        packetBuffer.writeBoolean(this.visible);
        return packetBuffer;
    }

    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand) {
        this.visible = !visible;
        return super.onSneakingActivated(player, hand);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (parent.getWorld().isClientSide) {
            GlobalRenderManager.addRender(parent.getWorld(), parent.getPos(), createFunction(this));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private Function<RenderLevelStageEvent, Void> createFunction(ComponentVisibleArea componentVisibleArea) {
        Function<RenderLevelStageEvent, Void> function = event -> {
            PoseStack poseStack = event.getPoseStack();
            if (!visible)
                return null;
            poseStack.pushPose();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            VertexConsumer buffer = Minecraft.getInstance()
                    .renderBuffers()
                    .bufferSource()
                    .getBuffer(RenderType.QUAD_TRANSPARENT_OVER_WATER);
            float p_109630_ = 1.0f;
            float p_109635_ = 1.0f;
            float p_109636_ = 1.0f;
            float p_109633_ = 1.0f;
            float p_109634_ = 1.0f;
            float p_109631_ = 1.0f;
            float p_109632_ = 1.0f;
            Matrix4f matrix = poseStack.last().pose();
            Matrix3f matrix3f = poseStack.last().normal();
            int f = (int) aabb.minX;
            int f1 = (int) aabb.minY;
            int f2 = (int) aabb.minZ;
            int f3 = (int) aabb.maxX;
            int f4 = (int) aabb.maxY;
            int f5 = (int) aabb.maxZ;
            float x1 = (float) aabb.minX;
            float y1 = (float) aabb.minY;
            float z1 = (float) aabb.minZ;
            float x2 = (float) aabb.maxX;
            float y2 = (float) aabb.maxY;
            float z2 = (float) aabb.maxZ;
            float red = 0f, green = 124 / 255f, blue = 170 / 255f, alpha = 0.5f;

            buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();

            // Верхняя грань
            buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();

            // Передняя грань
            buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();

            // Задняя грань
            buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();

            // Левая грань
            buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();

            // Правая грань
            buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();


            poseStack.popPose();
            poseStack.pushPose();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            VertexConsumer p_109623_ = Minecraft.getInstance()
                    .renderBuffers()
                    .bufferSource()
                    .getBuffer(net.minecraft.client.renderer.RenderType.lines());


            red = 0f;
            green = 124 / 255f;
            blue = 255f / 255f;
            alpha = 1f;
            Matrix4f matrix4f = poseStack.last().pose();
            p_109623_.vertex(matrix4f, f, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f2).color(red, green, blue, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f2).color(red, green, blue, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();


            poseStack.popPose();
            poseStack.pushPose();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            p_109623_ = Minecraft.getInstance()
                    .renderBuffers()
                    .bufferSource()
                    .getBuffer(net.minecraft.client.renderer.RenderType.lines());


            red = 0f;
            green = 124 / 255f;
            blue = 255f / 255f;
            alpha = 1f;
            matrix4f = poseStack.last().pose();
            p_109623_.vertex(matrix4f, f, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f2).color(red, green, blue, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f2).color(red, green, blue, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
            p_109623_.vertex(matrix4f, f, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f1, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f2).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            p_109623_.vertex(matrix4f, f3, f4, f5).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();


            poseStack.popPose();
            return null;
        };
        return function;

    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (parent.getWorld().isClientSide) {
            GlobalRenderManager.removeRender(parent.getWorld(), parent.getPos());
        }
    }
}
