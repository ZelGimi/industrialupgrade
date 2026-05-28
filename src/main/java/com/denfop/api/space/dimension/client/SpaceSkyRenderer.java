package com.denfop.api.space.dimension.client;

import com.denfop.Constants;
import com.denfop.api.space.EnumRing;
import com.denfop.api.space.IBody;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceBodyRef;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

public final class SpaceSkyRenderer {

    private static final List<Vector3f> STARS = generateStars(900, 10842L);
    private static final ResourceLocation SUN_TEXTURE = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/planet/sun.png");

    private static final double EARTH_SOLAR_DISTANCE = 0.9D;
    private static final float EARTH_BASE_SUN_SIZE = 5.0F;

    private static final double PLANET_BELOW_START_ALTITUDE = 200.0D;
    private static final double PLANET_BELOW_FULL_ALTITUDE = 400.0D;
    private static final float PLANET_BELOW_MIN_DISTANCE = 48.0F;
    private static final float PLANET_BELOW_MAX_DISTANCE = 88.0F;
    private static final float PLANET_BELOW_MIN_SIZE = 24.0F;
    private static final float PLANET_BELOW_MAX_SIZE = 64.0F;

    private static final float BODY_DISTANCE_BACK = 96.0F;
    private static final float BODY_DISTANCE_NORMAL = 95.0F;
    private static final float BODY_DISTANCE_FRONT = 94.0F;

    private static final double TWO_PI = Math.PI * 2.0D;
    private static final double REMOTE_SATELLITE_ORBIT_SCALE = 0.18D;

    private SpaceSkyRenderer() {
    }

    public static void render(
            final SpaceDimensionProfile profile,
            final ClientLevel level,
            final int ticks,
            final float partialTick,
            final PoseStack poseStack,
            final Camera camera,
            final Matrix4f projectionMatrix,
            final boolean isFoggy,
            final Runnable setupFog
    ) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();

        if (profile.drawStars()) {
            drawStars(profile, poseStack, ticks, partialTick);
        }

        drawSun(profile, level, partialTick, poseStack);
        drawNeighborBodies(profile, level, partialTick, poseStack);
        if (!profile.body().isAsteroid())
            drawPlanetBelow(profile, level, partialTick, poseStack, camera);

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        setupFog.run();
    }

    private static float computeSunSize(final SpaceDimensionProfile profile) {
        double bodyDistance = Math.max(0.08D, profile.body().distanceFromStar());
        double scale = EARTH_SOLAR_DISTANCE / bodyDistance;
        float size = (float) (EARTH_BASE_SUN_SIZE * profile.sunSize() * scale);
        return Mth.clamp(size, 0.0F, 64.0F);
    }

    private static void drawSun(
            final SpaceDimensionProfile profile,
            final ClientLevel level,
            final float partialTick,
            final PoseStack poseStack
    ) {
        float dayAngle = level.getTimeOfDay(partialTick) * 360.0F;
        float sunSize = computeSunSize(profile);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(dayAngle));
        poseStack.translate(0.0D, 0.0D, -100.0D);
        drawTexturedQuad(poseStack, SUN_TEXTURE, sunSize, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    private static void drawNeighborBodies(
            final SpaceDimensionProfile currentProfile,
            final ClientLevel level,
            final float partialTick,
            final PoseStack poseStack
    ) {
        final SpaceBodyRef self = currentProfile.body();
        final double skyTime = (level.getGameTime() + partialTick) / 24000.0D;

        final List<BodyRenderEntry> entries = new ArrayList<>();

        for (SpaceBodyRef other : SpaceBodyCatalog.allBodies()) {
            if (other == null) {
                continue;
            }
            if (other.name().equals(self.name())) {
                continue;
            }
            if (!other.systemName().equals(self.systemName())) {
                continue;
            }

            final BodyRenderParams params = computeBodyRenderParams(self, other, skyTime);
            if (params == null || params.size() <= 0.2F) {
                continue;
            }

            entries.add(new BodyRenderEntry(other, params));
        }

        if (entries.isEmpty()) {
            return;
        }

        final List<BodyRenderEntry> visibleEntries = resolveOcclusion(entries);

        visibleEntries.sort(
                Comparator.comparingDouble((BodyRenderEntry entry) -> entry.params().distance()).reversed()
                        .thenComparingDouble(entry -> entry.params().priority())
        );

        for (BodyRenderEntry entry : visibleEntries) {
            BodyRenderParams params = entry.params();

            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(params.yaw()));
            poseStack.mulPose(Axis.XP.rotationDegrees(params.pitch()));
            poseStack.translate(0.0D, 0.0D, -params.distance());
            poseStack.mulPose(Axis.ZP.rotationDegrees(params.spin()));

            renderCelestialBody(
                    poseStack,
                    entry.body(),
                    entry.body().texture(),
                    params
            );

            poseStack.popPose();
        }
    }

    private static List<BodyRenderEntry> resolveOcclusion(final List<BodyRenderEntry> entries) {
        final List<BodyRenderEntry> sorted = new ArrayList<>(entries);
        sorted.sort(
                Comparator.comparingDouble((BodyRenderEntry entry) -> entry.params().priority()).reversed()
                        .thenComparingDouble(entry -> entry.params().size()).reversed()
        );

        final List<BodyRenderEntry> kept = new ArrayList<>();

        for (BodyRenderEntry candidate : sorted) {
            boolean hidden = false;

            for (BodyRenderEntry blocker : kept) {
                if (!overlaps(blocker.params(), candidate.params())) {
                    continue;
                }

                final boolean blockerSatellite = blocker.body().isSatellite();
                final boolean candidateSatellite = candidate.body().isSatellite();

                if (blockerSatellite != candidateSatellite) {
                    final float larger = Math.max(blocker.params().size(), candidate.params().size());
                    final float smaller = Math.min(blocker.params().size(), candidate.params().size());

                    if (larger >= smaller * 2.2F) {
                        continue;
                    }
                }

                hidden = true;
                break;
            }

            if (!hidden) {
                kept.add(candidate);
            }
        }

        return kept;
    }

    private static boolean overlaps(final BodyRenderParams a, final BodyRenderParams b) {
        final float yawDelta = Math.abs(Mth.wrapDegrees(a.yaw() - b.yaw()));
        final float pitchDelta = Math.abs(a.pitch() - b.pitch());
        final double dist = Math.sqrt(yawDelta * yawDelta + pitchDelta * pitchDelta);
        return dist <= (a.angularRadiusDeg() + b.angularRadiusDeg()) * 0.92D;
    }

    private static void drawPlanetBelow(
            final SpaceDimensionProfile profile,
            final ClientLevel level,
            final float partialTick,
            final PoseStack poseStack,
            final Camera camera
    ) {
        Vec3 cameraPos = camera.getPosition();
        double altitude = cameraPos.y - (profile.seaLevel() + 32.0D);

        if (altitude <= PLANET_BELOW_START_ALTITUDE) {
            return;
        }

        float visibility = (float) Mth.clamp(
                (altitude - PLANET_BELOW_START_ALTITUDE) / (PLANET_BELOW_FULL_ALTITUDE - PLANET_BELOW_START_ALTITUDE),
                0.0D,
                1.0D
        );

        float distance = Mth.lerp(visibility, PLANET_BELOW_MIN_DISTANCE, PLANET_BELOW_MAX_DISTANCE);
        float size = Mth.lerp(visibility, PLANET_BELOW_MIN_SIZE, PLANET_BELOW_MAX_SIZE);
        float alpha = 1.0F;

        float rotation = (float) ((level.getGameTime() + partialTick) * 0.015D % 360.0D);
        ResourceLocation surfaceTexture = resolvePlanetBelowTexture(profile);

        poseStack.pushPose();
        poseStack.translate(0.0D, -distance, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));
        drawTexturedQuad(poseStack, surfaceTexture, size, 1.0F, 1.0F, 1.0F, alpha);
        poseStack.popPose();
    }

    private static ResourceLocation resolvePlanetBelowTexture(final SpaceDimensionProfile profile) {
        ResourceLocation customSurface = ResourceLocation.tryBuild(
                Constants.MOD_ID,
                "textures/planet_surface/" + profile.body().name().toLowerCase(Locale.ROOT) + ".png"
        );

        if (Minecraft.getInstance().getResourceManager().getResource(customSurface).isPresent()) {
            return customSurface;
        }

        return profile.body().texture();
    }

    private static void drawGlowQuad(
            final PoseStack poseStack,
            final float size,
            final float red,
            final float green,
            final float blue,
            final float alpha
    ) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = poseStack.last().pose();

        buffer.addVertex(matrix, -size, -size, -0.01F).setColor(red, green, blue, alpha);
        buffer.addVertex(matrix, size, -size, -0.01F).setColor(red, green, blue, alpha);
        buffer.addVertex(matrix, size, size, -0.01F).setColor(red, green, blue, alpha);
        buffer.addVertex(matrix, -size, size, -0.01F).setColor(red, green, blue, alpha);
        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void renderCelestialBody(
            final PoseStack poseStack,
            final SpaceBodyRef body,
            final ResourceLocation texture,
            final BodyRenderParams params
    ) {
        final EnumRing ringType = params.ringType();
        if (body.isAsteroid())
            return;
        if (ringType != null) {
            poseStack.pushPose();
            applyRingTransform(poseStack, ringType, params.ringTilt(), params.spin());
            drawProceduralRingHalf(poseStack, params.size(), params.alpha(), ringType, false);
            poseStack.popPose();
        }

        drawTexturedQuad(poseStack, texture, params.size(), 1.0F, 1.0F, 1.0F, params.alpha());

        if (ringType != null) {
            poseStack.pushPose();
            applyRingTransform(poseStack, ringType, params.ringTilt(), params.spin());
            drawProceduralRingHalf(poseStack, params.size(), params.alpha(), ringType, true);
            poseStack.popPose();
        }
    }

    private static void applyRingTransform(
            final PoseStack poseStack,
            final EnumRing ringType,
            final float tilt,
            final float spin
    ) {
        if (ringType == EnumRing.VERTICAL) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(tilt));
            poseStack.mulPose(Axis.ZP.rotationDegrees(spin * 0.25F));
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(tilt));
            poseStack.mulPose(Axis.ZP.rotationDegrees(spin * 0.25F));
        }
    }

    private static void drawProceduralRingHalf(
            final PoseStack poseStack,
            final float bodySize,
            final float bodyAlpha,
            final EnumRing ringType,
            final boolean frontHalf
    ) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        final Tesselator tessellator = Tesselator.getInstance();
        final BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        final Matrix4f matrix = poseStack.last().pose();

        final RingBand[] bands = ringType == EnumRing.VERTICAL
                ? new RingBand[]{
                new RingBand(1.12F, 1.34F, 0.70F, 0.82F, 0.92F, 0.20F),
                new RingBand(1.40F, 1.74F, 0.55F, 0.70F, 0.84F, 0.12F)
        }
                : new RingBand[]{
                new RingBand(1.10F, 1.26F, 0.92F, 0.84F, 0.68F, 0.34F),
                new RingBand(1.30F, 1.54F, 0.82F, 0.76F, 0.60F, 0.24F),
                new RingBand(1.58F, 1.98F, 0.72F, 0.67F, 0.56F, 0.14F)
        };


        final int segments = ringType == EnumRing.VERTICAL ? 88 : 96;

        for (RingBand band : bands) {
            final float innerRadius = bodySize * band.innerScale();
            final float outerRadius = bodySize * band.outerScale();

            final float innerAlpha = bodyAlpha * band.alphaScale();
            final float outerAlpha = innerAlpha * 0.20F;

            for (int i = 0; i < segments; i++) {
                final double angle1 = TWO_PI * i / segments;
                final double angle2 = TWO_PI * (i + 1) / segments;
                final double mid = (angle1 + angle2) * 0.5D;

                if (isFrontHalf(ringType, mid) != frontHalf) {
                    continue;
                }

                final float x1o = (float) (Math.cos(angle1) * outerRadius);
                final float y1o = (float) (Math.sin(angle1) * outerRadius);
                final float x2o = (float) (Math.cos(angle2) * outerRadius);
                final float y2o = (float) (Math.sin(angle2) * outerRadius);

                final float x1i = (float) (Math.cos(angle1) * innerRadius);
                final float y1i = (float) (Math.sin(angle1) * innerRadius);
                final float x2i = (float) (Math.cos(angle2) * innerRadius);
                final float y2i = (float) (Math.sin(angle2) * innerRadius);

                buffer.addVertex(matrix, x1o, y1o, 0.0F).setColor(band.red(), band.green(), band.blue(), outerAlpha);
                buffer.addVertex(matrix, x2o, y2o, 0.0F).setColor(band.red(), band.green(), band.blue(), outerAlpha);
                buffer.addVertex(matrix, x2i, y2i, 0.0F).setColor(band.red(), band.green(), band.blue(), innerAlpha);
                buffer.addVertex(matrix, x1i, y1i, 0.0F).setColor(band.red(), band.green(), band.blue(), innerAlpha);
            }
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static boolean isFrontHalf(final EnumRing ringType, final double angle) {
        if (ringType == EnumRing.VERTICAL) {
            return Math.cos(angle) <= 0.0D;
        }
        return Math.sin(angle) >= 0.0D;
    }

    private static float computePlanetAngularSize(
            final double currentDistance,
            final double otherDistance,
            final double otherPhysicalSize
    ) {
        double delta = Math.abs(otherDistance - currentDistance);

        if (delta <= 0.20D) {
            return (float) Mth.clamp(otherPhysicalSize / Math.max(0.08D, delta) * 3.0D, 0.0D, 12.0D);
        }

        if (delta <= 0.45D) {
            return (float) Mth.clamp(otherPhysicalSize / delta * 3.0D, 0.0D, 8.0D);
        }

        return (float) Mth.clamp(otherPhysicalSize / delta * 3.0D, 0.0D, 3.5D);
    }

    private static boolean isPlanetVisibleBySolarDistance(
            final double currentDistance,
            final double otherDistance
    ) {
        double delta = Math.abs(otherDistance - currentDistance);

        if (currentDistance <= 0.75D) {
            return delta <= 0.80D;
        }

        if (currentDistance <= 1.0D) {
            return delta <= 0.90D;
        }

        if (currentDistance <= 2.0D) {
            return delta <= 1.10D;
        }

        if (currentDistance <= 5.0D) {
            return delta <= 1.80D;
        }

        return delta <= 2.20D;
    }

    private static double getReferenceStarDistance(final SpaceBodyRef body) {
        return body.distanceFromStar();
    }

    private static BodyRenderParams computeBodyRenderParams(
            final SpaceBodyRef self,
            final SpaceBodyRef other,
            final double skyTime
    ) {
        final double orbitPeriod = Math.max(0.08D, other.orbitPeriod());
        final double cycle = skyTime / orbitPeriod * TWO_PI;
        final float spin = (float) ((skyTime * 720.0D / orbitPeriod) % 360.0D);


        if (self.isSatellite() && other.name().equals(self.parentName())) {
            final float yaw = (float) (self.rotationAngle() + Math.sin(skyTime / Math.max(0.08D, self.orbitPeriod())) * 18.0D);
            final float pitch = 30.0F + (float) Math.sin(skyTime * 1.4D) * 6.0F;
            final float size = (float) self.orbitDistance() * 64.0F;

            return buildParams(other, yaw, pitch, size, 0.98F, spin * 0.2F, BODY_DISTANCE_NORMAL, 600.0F);
        }


        if (!self.isSatellite() && other.isSatellite() && self.name().equals(other.parentName())) {
            final float yaw = (float) (other.rotationAngle() + (skyTime * 360.0D / orbitPeriod));
            final float pitch = 18.0F + (float) Math.sin(cycle * 0.35D) * 14.0F;
            final float size = (float) Mth.clamp(
                    other.size() / Math.max(0.05D, other.orbitDistance()) * 0.05D,
                    1.0D,
                    7.5D
            );
            final float priority = (float) (1100.0D + 240.0D / Math.max(0.02D, other.orbitDistance()));

            return buildParams(other, yaw, pitch, size, 0.95F, spin, BODY_DISTANCE_NORMAL, priority);
        }


        if (self.isSatellite() && other.isSatellite()
                && self.parentName() != null
                && self.parentName().equals(other.parentName())) {
            final double moonDelta = Math.abs(self.orbitDistance() - other.orbitDistance());
            final boolean closerToPlanet = other.orbitDistance() < self.orbitDistance();

            final float yaw = (float) (other.rotationAngle() + 35.0F + Math.sin(cycle) * 12.0D);
            final float pitch = 16.0F + (float) Math.cos(cycle) * 10.0F;
            final float size = (float) moonDelta * 16.0F;

            final float distance = closerToPlanet ? BODY_DISTANCE_FRONT : BODY_DISTANCE_BACK;
            final float alpha = closerToPlanet ? 0.86F : 0.80F;
            final float priority = (float) ((closerToPlanet ? 1400.0D : 700.0D) + 100.0D / Math.max(0.01D, moonDelta));

            return buildParams(other, yaw, pitch, size, alpha, spin, distance, priority);
        }


        if (!other.isSatellite()) {
            final double selfStarDistance = getReferenceStarDistance(self);
            final double otherStarDistance = other.distanceFromStar();
            final double delta = Math.abs(otherStarDistance - selfStarDistance);

            if (!isPlanetVisibleBySolarDistance(selfStarDistance, otherStarDistance)) {
                return null;
            }

            final float size = computePlanetAngularSize(selfStarDistance, otherStarDistance, other.size());
            if (size < 0.15F) {
                return null;
            }

            final double signedDelta = otherStarDistance - selfStarDistance;

            final float yawBase = (float) Mth.clamp(signedDelta * 85.0D, -110.0D, 110.0D);
            final float yaw = yawBase + (float) Math.sin(cycle) * 8.0F + other.rotationAngle() * 0.12F;
            final float pitch = 16.0F + (float) Math.cos(cycle * 0.7D) * (delta < 0.35D ? 8.0F : 5.0F);
            final float alpha = (float) Mth.clamp(0.95D - delta * 0.45D, 0.35D, 0.92D);
            final float priority = (float) (320.0D + size * 10.0D - delta * 30.0D);

            return buildParams(other, yaw, pitch, size, alpha, spin * 0.35F, BODY_DISTANCE_NORMAL, priority);
        }


        return null;
    }

    private static BodyRenderParams buildParams(
            final SpaceBodyRef body,
            final float yaw,
            final float pitch,
            final float size,
            final float alpha,
            final float spin,
            final float distance,
            final float priority
    ) {
        final EnumRing ringType = resolveRingType(body);
        final float ringScale = ringType == null ? 1.0F : getRingVisualScale(ringType);
        final float angularRadiusDeg = computeAngularRadiusDegrees(size * ringScale, distance);
        final float ringTilt = computeRingTilt(body, ringType);

        return new BodyRenderParams(
                yaw,
                pitch,
                size,
                alpha,
                spin,
                distance,
                priority,
                angularRadiusDeg,
                ringType,
                ringTilt
        );
    }

    private static EnumRing resolveRingType(final SpaceBodyRef bodyRef) {
        final IBody body = SpaceNet.instance.getBodyFromName(bodyRef.name());
        if (body instanceof IPlanet planet) {
            return planet.getRing();
        }
        return null;
    }

    private static float getRingVisualScale(final EnumRing ringType) {
        return ringType == EnumRing.VERTICAL ? 1.78F : 2.02F;
    }

    private static float computeRingTilt(final SpaceBodyRef body, final EnumRing ringType) {
        if (ringType == null) {
            return 0.0F;
        }

        if (ringType == EnumRing.VERTICAL) {
            return 14.0F + stableVerticalFactor(body) * 12.0F;
        }

        return 72.0F + stableVerticalFactor(body) * 8.0F;
    }

    private static float computeAngularRadiusDegrees(final float size, final float distance) {
        return (float) Math.toDegrees(Math.atan(size / Math.max(1.0F, distance)));
    }

    private static Vec3 computePseudoPosition(final SpaceBodyRef body, final double skyTime) {
        if (body.isSatellite()) {
            final SpaceBodyRef parent = SpaceBodyCatalog.byName(body.parentName());
            final Vec3 parentPos = parent != null ? computePseudoPosition(parent, skyTime) : Vec3.ZERO;
            final Vec3 dir = computePseudoOrbitDirection(body, skyTime);
            return parentPos.add(dir.scale(Math.max(0.02D, body.orbitDistance()) * REMOTE_SATELLITE_ORBIT_SCALE));
        }

        final double orbitPeriod = Math.max(0.08D, body.orbitPeriod());
        final double angle = skyTime / orbitPeriod * TWO_PI
                + Math.toRadians(body.rotationAngle())
                + stablePhaseRad(body);

        final double dist = Math.max(0.08D, body.distanceFromStar());
        return new Vec3(
                Math.cos(angle) * dist,
                Math.sin(angle * (0.55D + stableVerticalFactor(body) * 0.25D)) * dist * 0.04D,
                Math.sin(angle) * dist
        );
    }

    private static Vec3 computePseudoOrbitDirection(final SpaceBodyRef body, final double skyTime) {
        final double orbitPeriod = Math.max(0.08D, body.orbitPeriod());
        final double angle = skyTime / orbitPeriod * TWO_PI
                + Math.toRadians(body.rotationAngle())
                + stablePhaseRad(body);

        Vec3 dir = new Vec3(
                Math.cos(angle),
                Math.sin(angle * (0.85D + stableVerticalFactor(body) * 0.35D)) * (0.18D + stableVerticalFactor(body) * 0.14D),
                Math.sin(angle)
        ).normalize();

        dir = rotateZ(dir, stableInclinationRad(body));
        dir = rotateY(dir, stableNodeRad(body));

        return dir.normalize();
    }

    private static Vec3 rotateY(final Vec3 vec, final double angleRad) {
        final double cos = Math.cos(angleRad);
        final double sin = Math.sin(angleRad);

        return new Vec3(
                vec.x * cos + vec.z * sin,
                vec.y,
                -vec.x * sin + vec.z * cos
        );
    }

    private static Vec3 rotateZ(final Vec3 vec, final double angleRad) {
        final double cos = Math.cos(angleRad);
        final double sin = Math.sin(angleRad);

        return new Vec3(
                vec.x * cos - vec.y * sin,
                vec.x * sin + vec.y * cos,
                vec.z
        );
    }

    private static double stablePhaseRad(final SpaceBodyRef body) {
        final long seed = stableSeed(body);
        return ((seed & 0xFFFFL) / 65535.0D) * TWO_PI;
    }

    private static double stableNodeRad(final SpaceBodyRef body) {
        final long seed = stableSeed(body) * 31L + 17L;
        return (((seed >>> 8) & 0xFFFFL) / 65535.0D) * TWO_PI;
    }

    private static float stableVerticalFactor(final SpaceBodyRef body) {
        final long seed = stableSeed(body) * 17L + 23L;
        return (float) (((seed >>> 16) & 0xFFFFL) / 65535.0D);
    }

    private static double stableInclinationRad(final SpaceBodyRef body) {
        return Math.toRadians(-18.0D + stableVerticalFactor(body) * 36.0D);
    }

    private static long stableSeed(final SpaceBodyRef body) {
        long hash = 1469598103934665603L;
        hash = mixSeed(hash, body.name());
        hash = mixSeed(hash, body.systemName());
        hash = mixSeed(hash, body.parentName());
        return hash;
    }

    private static long mixSeed(long hash, final String value) {
        if (value == null) {
            return hash * 1099511628211L;
        }

        for (int i = 0; i < value.length(); i++) {
            hash ^= value.charAt(i);
            hash *= 1099511628211L;
        }
        return hash;
    }

    private static void drawStars(
            final SpaceDimensionProfile profile,
            final PoseStack poseStack,
            final int ticks,
            final float partialTick
    ) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = poseStack.last().pose();
        float brightness = profile.starBrightness();
        float twinkle = 0.92F + Mth.sin((ticks + partialTick) * 0.005F) * 0.08F;

        for (Vector3f star : STARS) {
            float size = 0.20F + (Math.abs(star.x) + Math.abs(star.y) + Math.abs(star.z)) * 0.06F;
            float x = star.x * 100.0F;
            float y = star.y * 100.0F;
            float z = star.z * 100.0F;
            addStarQuad(buffer, matrix, x, y, z, size, brightness * twinkle);
        }
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private static void addStarQuad(
            final BufferBuilder buffer,
            final Matrix4f matrix,
            final float x,
            final float y,
            final float z,
            final float size,
            final float alpha
    ) {
        buffer.addVertex(matrix, x - size, y - size, z).setColor(1.0F, 1.0F, 1.0F, alpha);
        buffer.addVertex(matrix, x + size, y - size, z).setColor(1.0F, 1.0F, 1.0F, alpha);
        buffer.addVertex(matrix, x + size, y + size, z).setColor(1.0F, 1.0F, 1.0F, alpha);
        buffer.addVertex(matrix, x - size, y + size, z).setColor(1.0F, 1.0F, 1.0F, alpha);
    }

    private static void drawTexturedQuad(
            final PoseStack poseStack,
            final ResourceLocation texture,
            final float size,
            final float red,
            final float green,
            final float blue,
            final float alpha
    ) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(red, green, blue, alpha);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = poseStack.last().pose();

        buffer.addVertex(matrix, -size, -size, 0.0F).setUv(0.0F, 1.0F);
        buffer.addVertex(matrix, size, -size, 0.0F).setUv(1.0F, 1.0F);
        buffer.addVertex(matrix, size, size, 0.0F).setUv(1.0F, 0.0F);
        buffer.addVertex(matrix, -size, size, 0.0F).setUv(0.0F, 0.0F);
        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static List<Vector3f> generateStars(final int count, final long seed) {
        Random random = new Random(seed);
        List<Vector3f> stars = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            double x = random.nextDouble() * 2.0D - 1.0D;
            double y = random.nextDouble() * 2.0D - 1.0D;
            double z = random.nextDouble() * 2.0D - 1.0D;
            double len = Math.sqrt(x * x + y * y + z * z);

            if (len < 0.05D || len > 1.0D) {
                i--;
                continue;
            }

            stars.add(new Vector3f((float) (x / len), (float) (y / len), (float) (z / len)));
        }

        return stars;
    }

    private record RingBand(
            float innerScale,
            float outerScale,
            float red,
            float green,
            float blue,
            float alphaScale
    ) {
    }

    private record BodyRenderEntry(
            SpaceBodyRef body,
            BodyRenderParams params
    ) {
    }

    private record BodyRenderParams(
            float yaw,
            float pitch,
            float size,
            float alpha,
            float spin,
            float distance,
            float priority,
            float angularRadiusDeg,
            EnumRing ringType,
            float ringTilt
    ) {
    }
}