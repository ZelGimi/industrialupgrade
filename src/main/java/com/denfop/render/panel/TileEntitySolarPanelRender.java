package com.denfop.render.panel;

import com.denfop.Constants;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class TileEntitySolarPanelRender implements BlockEntityRenderer<BlockEntitySolarPanel> {

    public static final ResourceLocation texture = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/pollution.png"
    );
    private final BlockEntityRendererProvider.Context contex;
    private float rotation = 0;
    private float prevRotation = 0;
    private Map<BlockPos, DataPollution> entries = new HashMap<>();
    public TileEntitySolarPanelRender(BlockEntityRendererProvider.Context context) {
        this.contex = context;
    }

    @Override
    public void render(BlockEntitySolarPanel te, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int packedLight, int packedOverlay) {
        if (!te.canRender()) {
            return;
        }
        poseStack.pushPose();
        ScreenIndustrialUpgrade.bindTexture(texture);
        DataPollution dataPollution = entries.get(te.getBlockPos());
        if (dataPollution == null) {
            dataPollution = new DataPollution(te.timer.getIndexWork(), new PollutionModel(
                    te.getWorld().random,
                    te.timer.getIndexWork()
            ));
            entries.put(te.getBlockPos(), dataPollution);
        }

        if (dataPollution.getIndex() != te.timer.getIndexWork()) {
            dataPollution.setIndex(te.timer.getIndexWork());
            dataPollution.setModel(null);
        }
        if (dataPollution.getModel() == null) {
            dataPollution.setModel(new PollutionModel(
                    te.getWorld().random,
                    te.timer.getIndexWork()
            ));
        }
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(texture));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        dataPollution.getModel().renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
