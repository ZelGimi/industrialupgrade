package com.denfop.render.base;

import com.denfop.Constants;
import com.denfop.entity.SmallBee;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SmallBeeRenderer extends MobRenderer<SmallBee, BeeModel<SmallBee>> {

    private static final ResourceLocation ANGRY_BEE_TEXTURE =
            ResourceLocation.tryBuild("minecraft", "textures/entity/bee/bee_angry.png");
    private static final ResourceLocation ANGRY_NECTAR_BEE_TEXTURE =
            ResourceLocation.tryBuild("minecraft", "textures/entity/bee/bee_angry_nectar.png");
    private static final ResourceLocation BEE_TEXTURE =
            ResourceLocation.tryBuild("minecraft", "textures/entity/bee/bee.png");
    private static final ResourceLocation NECTAR_BEE_TEXTURE =
            ResourceLocation.tryBuild("minecraft", "textures/entity/bee/bee_nectar.png");

    private static final Map<String, ResourceLocation> TEXTURE_CACHE = new HashMap<>();

    public SmallBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new BeeModel<>(context.bakeLayer(ModelLayers.BEE)), 0.4F);
    }

    @Override
    public void render(
            SmallBee entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SmallBee entity) {
        String beeName = entity.getBeeRenderName();

        if (beeName == null || beeName.isBlank()) {
            if (entity.isAngry()) {
                return entity.hasNectar() ? ANGRY_NECTAR_BEE_TEXTURE : ANGRY_BEE_TEXTURE;
            } else {
                return entity.hasNectar() ? NECTAR_BEE_TEXTURE : BEE_TEXTURE;
            }
        }

        final String variant;
        if (entity.isAngry()) {
            variant = entity.hasNectar() ? "_angry_nectar_bee" : "_angry_bee";
        } else {
            variant = entity.hasNectar() ? "_nectar_bee" : "_bee";
        }

        String key = beeName + variant;

        return TEXTURE_CACHE.computeIfAbsent(
                key,
                k -> ResourceLocation.tryBuild(Constants.MOD_ID, "textures/entity/bee/" + beeName + variant + ".png")
        );
    }
}