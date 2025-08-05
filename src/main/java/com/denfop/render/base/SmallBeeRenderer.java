package com.denfop.render.base;

import com.denfop.Constants;
import com.denfop.api.bee.IBee;
import com.denfop.entity.SmallBee;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SmallBeeRenderer extends MobRenderer<SmallBee, BeeModel<SmallBee>> {
    private static final ResourceLocation ANGRY_BEE_TEXTURE = ResourceLocation.parse("textures/entity/bee/bee_angry.png");
    private static final ResourceLocation ANGRY_NECTAR_BEE_TEXTURE = ResourceLocation.parse("textures/entity/bee/bee_angry_nectar.png");
    private static final ResourceLocation BEE_TEXTURE = ResourceLocation.parse("textures/entity/bee/bee.png");
    private static final ResourceLocation NECTAR_BEE_TEXTURE = ResourceLocation.parse("textures/entity/bee/bee_nectar.png");
    private static final ResourceLocation[] BEE_TEXTURES = new ResourceLocation[5];
    private static final ResourceLocation[] NECTAR_BEE_TEXTURES = new ResourceLocation[5];
    private static final ResourceLocation[] ANGRY_BEE_TEXTURES = new ResourceLocation[5];
    private static final ResourceLocation[] ANGRY_NECTAR_BEE_TEXTURES = new ResourceLocation[5];
    public SmallBeeRenderer(EntityRendererProvider.Context p_173931_) {
        super(p_173931_, new BeeModel<>(p_173931_.bakeLayer(ModelLayers.BEE)), 0.4F);
    }

    @Override
    public void render(SmallBee pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.scale(0.5f, 0.5f, 0.5f);
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.popPose();
    }

    public ResourceLocation getTextureLocation(SmallBee pEntity) {
        IBee bee = pEntity.bee;
        if (bee != null) {
            String name = bee.getName();
            int id = bee.getId();
            ResourceLocation texture = BEE_TEXTURES[id];
            ResourceLocation texture_nectar = NECTAR_BEE_TEXTURES[id];
            ResourceLocation angry_texture = ANGRY_BEE_TEXTURES[id];
            ResourceLocation angry_texture_nectar = ANGRY_NECTAR_BEE_TEXTURES[id];
            if (pEntity.isAngry()) {
                if (pEntity.hasNectar()) {
                    if (angry_texture_nectar == null) {
                        angry_texture_nectar = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/entity/bee/" + name + "_angry_nectar_bee.png");
                        ANGRY_NECTAR_BEE_TEXTURES[id] = angry_texture_nectar;
                    }
                    return angry_texture_nectar;
                } else {
                    if (angry_texture == null) {
                        angry_texture = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/entity/bee/" + name + "_angry_bee.png");
                        ANGRY_BEE_TEXTURES[id] = angry_texture;
                    }
                    return angry_texture;
                }
            } else {
                if (pEntity.hasNectar()) {
                    if (texture_nectar == null) {
                        texture_nectar = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/entity/bee/" + name + "_nectar_bee.png");
                        NECTAR_BEE_TEXTURES[id] = texture_nectar;
                    }
                    return texture_nectar;
                } else {
                    if (texture == null) {
                        texture = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/entity/bee/" + name + "_bee.png");
                        BEE_TEXTURES[id] = angry_texture;
                    }
                    return texture;
                }
            }
        } else {
            if (pEntity.isAngry()) {
                return pEntity.hasNectar() ? ANGRY_NECTAR_BEE_TEXTURE : ANGRY_BEE_TEXTURE;
            } else {
                return pEntity.hasNectar() ? NECTAR_BEE_TEXTURE : BEE_TEXTURE;
            }
        }
    }
}
