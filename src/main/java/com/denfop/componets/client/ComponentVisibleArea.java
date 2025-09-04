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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

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
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
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
            Matrix4f matrix = poseStack.last().pose();
            float x1 = (float) aabb.minX;
            float y1 = (float) aabb.minY;
            float z1 = (float) aabb.minZ;
            float x2 = (float) aabb.maxX;
            float y2 = (float) aabb.maxY;
            float z2 = (float) aabb.maxZ;
            float red = 0f, green = 124 / 255f, blue = 170 / 255f, alpha = 0.5f;
            int f = (int) aabb.minX;
            int f1 = (int) aabb.minY;
            int f2 = (int) aabb.minZ;
            int f3 = (int) aabb.maxX;
            int f4 = (int) aabb.maxY;
            int f5 = (int) aabb.maxZ;
            buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha);

            // Верхняя грань
            buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha);

            // Передняя грань
            buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha);

            // Задняя грань
            buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha);

            // Левая грань
            buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha);

            // Правая грань
            buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha);
            buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha);


            poseStack.popPose();
            poseStack.pushPose();
            PoseStack.Pose matrix3f = poseStack.last();
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
            p_109623_.addVertex(matrix4f, f, f1, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f, f1, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f, f4, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f, f1, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
            p_109623_.addVertex(matrix4f, f, f1, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
            p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, -1.0F, 0.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f, f4, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, -1.0F, 0.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f, f4, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
            p_109623_.addVertex(matrix4f, f, f4, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
            p_109623_.addVertex(matrix4f, f, f4, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, -1.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f, f1, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, -1.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f, f1, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 0.0F, -1.0F);
            p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 0.0F, -1.0F);
            p_109623_.addVertex(matrix4f, f, f4, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 1.0F, 0.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
            p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);
            p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(red, green, blue, alpha).setNormal(matrix3f, 0.0F, 0.0F, 1.0F);


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
