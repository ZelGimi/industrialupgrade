package com.denfop.api.space.dimension.client;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;

public class SpaceDimensionSpecialEffects extends DimensionSpecialEffects {

    private final SpaceDimensionProfile profile;

    public SpaceDimensionSpecialEffects(final SpaceDimensionProfile profile) {
        super(
                profile.cloudHeight(),
                true,
                profile.hasAtmosphere() ? SkyType.NORMAL : SkyType.NONE,
                false,
                profile.ambientLight() > 0.35F
        );
        this.profile = profile;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(final Vec3 color, final float sunHeight) {
        double mix = this.profile.hasAtmosphere() ? Math.max(0.15D, this.profile.atmosphereDensity()) : 0.02D;
        Vec3 fog = this.profile.fogColor();
        double brightness = this.profile.hasAtmosphere() ? 0.55D + sunHeight * 0.45D : 0.25D + sunHeight * 0.10D;
        return new Vec3(
                fog.x * brightness * mix + color.x * (1.0D - mix),
                fog.y * brightness * mix + color.y * (1.0D - mix),
                fog.z * brightness * mix + color.z * (1.0D - mix)
        );
    }

    @Override
    public boolean isFoggyAt(final int x, final int z) {
        return this.profile.atmosphereDensity() >= 0.70F;
    }

    @Override
    public boolean renderSky(
            final ClientLevel level,
            final int ticks,
            final float partialTick,
            final PoseStack poseStack,
            final Camera camera,
            final Matrix4f projectionMatrix,
            final boolean isFoggy,
            final Runnable setupFog
    ) {
        SpaceSkyRenderer.render(this.profile, level, ticks, partialTick, poseStack, camera, projectionMatrix, isFoggy, setupFog);
        return true;
    }

    @Override
    public boolean renderClouds(
            final ClientLevel level,
            final int ticks,
            final float partialTick,
            final PoseStack poseStack,
            final double camX,
            final double camY,
            final double camZ,
            final Matrix4f projectionMatrix
    ) {
        return !this.profile.hasAtmosphere();
    }

    @Override
    public boolean renderSnowAndRain(
            final ClientLevel level,
            final int ticks,
            final float partialTick,
            final LightTexture lightTexture,
            final double camX,
            final double camY,
            final double camZ
    ) {
        return !this.profile.hasAtmosphere();
    }

    @Override
    public void adjustLightmapColors(
            final ClientLevel level,
            final float partialTicks,
            final float skyDarken,
            final float blockLightRedFlicker,
            final float skyLight,
            final int pixelX,
            final int pixelY,
            final Vector3f colors
    ) {
        float atmosphereTint = this.profile.hasAtmosphere() ? this.profile.atmosphereDensity() * 0.10F : 0.0F;
        colors.setX(Math.min(1.0F, colors.x() + atmosphereTint * 0.80F));
        colors.setY(Math.min(1.0F, colors.y() + atmosphereTint * 0.50F));
        colors.setZ(Math.min(1.0F, colors.z() + atmosphereTint * 0.25F));
    }
}
