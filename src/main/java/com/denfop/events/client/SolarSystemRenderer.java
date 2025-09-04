package com.denfop.events.client;

import com.denfop.Constants;
import com.denfop.api.space.*;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.blockentity.mechanism.TileEntityHologramSpace;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.util.*;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX;

public class SolarSystemRenderer {
    public static final ResourceLocation SATURN_RING_TEXTURE = new ResourceLocation(
            Constants.MOD_ID,
            "textures/planet/saturn_ring" +
                    ".png"
    );

    static Map<IBody, Map<IBody, Float[]>> trajectories = new HashMap<>();
    static Random random = new Random(42);
    boolean writeData = false;
    IStar star;
    List<IPlanet> planets;
    List<ISatellite> satellites = new ArrayList<>();
    private Minecraft minecraft = Minecraft.getInstance();
    private float time;
    private List<IAsteroid> asteroids;

    public SolarSystemRenderer() {
    }

    public static void renderParabolicTrajectory(PoseStack poseStack, RenderLevelStageEvent event, float time, IBody planets, IBody planets1, float progress, ItemStack item) {
        // Начальные и конечные координаты
        float x1 = (float) planets.getRotationTimeX(time);
        float z1 = (float) planets.getRotationTimeZ(time);
        float x2 = (float) planets1.getRotationTimeX(time);
        float z2 = (float) planets1.getRotationTimeZ(time);
        if (planets instanceof IAsteroid) {
            IAsteroid asteroid = (IAsteroid) planets;
            float currentAngle =
                    asteroid.getMiniAsteroid().get(0).getY() + asteroid.getMiniAsteroid().get(0).getRotationSpeed() * time;
            x1 = asteroid.getMiniAsteroid().get(0).getX() * (float) Math.cos(currentAngle);
            z1 = asteroid.getMiniAsteroid().get(0).getX() * (float) Math.sin(currentAngle);

        }
        if (planets1 instanceof IAsteroid) {
            IAsteroid asteroid = (IAsteroid) planets1;
            float currentAngle =
                    asteroid.getMiniAsteroid().get(0).getY() + asteroid.getMiniAsteroid().get(0).getRotationSpeed() * time;
            x2 = asteroid.getMiniAsteroid().get(0).getX() * (float) Math.cos(currentAngle);
            z2 = asteroid.getMiniAsteroid().get(0).getX() * (float) Math.sin(currentAngle);

        }
        float y1 = 0.5F;
        float y2 = 0.5F;
        float centerY = -0.2F;
        int steps = 400;
        final float finalX = x1;
        final float finalZ = z1;
        final float finalX1 = x2;
        final float finalZ1 = z2;
        Float[] trajectory = calculateTrajectory(finalX, y1, finalZ,
                finalX1, y2, finalZ1, centerY, steps
        );
        poseStack.pushPose();

        Matrix4f matrix = poseStack.last().pose();

        Matrix3f matrix3f = poseStack.last().normal();
        VertexConsumer p_109623_ = Minecraft.getInstance()
                .renderBuffers()
                .bufferSource()
                .getBuffer(RenderType.lines());
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        RenderSystem.lineWidth(2);
        for (int i = 0; i < steps; i++) {
            float x = trajectory[i * 3];
            float y = trajectory[i * 3 + 1];
            float z = trajectory[i * 3 + 2];
            float color = 0;
            if (progress * steps >= i) {
                color = 1;
            }
            if (color != 0)
                p_109623_.vertex(matrix, x, y, z).color(0, color, 0, 1f).normal(matrix3f, 1, 0, 0).endVertex();
            else
                p_109623_.vertex(matrix, x, y, z).color(0, 0, 0.65f, 1f).normal(matrix3f, 1, 0, 0).endVertex();

        }


        RenderSystem.lineWidth(1);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        poseStack.popPose();

    }

    private static Float[] calculateTrajectory(
            float x1,
            float y1,
            float z1,
            float x2,
            float y2,
            float z2,
            float centerY,
            int steps
    ) {

        float centerX = (x1 + x2) / 2.0F;
        float centerZ = (z1 + z2) / 2.0F;


        float a = (centerY - y1) / ((centerX - x1) * (centerX - x1));
        a = Math.max(a, -0.5f);
        Float[] trajectory = new Float[steps * 3];

        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);
            float x = x1 + t * (x2 - x1);
            float z = z1 + t * (z2 - z1);
            float y = a * (x - x1) * (x - x2) + y1;

            trajectory[i * 3] = x;
            trajectory[i * 3 + 1] = y;
            trajectory[i * 3 + 2] = z;
        }
        return trajectory;

    }

    private void renderPlanet(
            PoseStack poseStack, RenderLevelStageEvent event, float radius,
            ResourceLocation texture,
            float x,
            float y,
            float z,
            float rotation,
            float rotationAngle
    ) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotationAngle));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        ScreenIndustrialUpgrade.bindTexture(texture);
        Tesselator tessellator = Tesselator.getInstance();
        final BufferBuilder buffer = tessellator.getBuilder();

        renderCube(poseStack, buffer, radius);
        poseStack.popPose();
    }

    private void renderRings(PoseStack poseStack, RenderLevelStageEvent event, float radius, ResourceLocation texture, float x, float y, float z, boolean isSaturn) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        ScreenIndustrialUpgrade.bindTexture(texture);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        if (!isSaturn) {
            poseStack.mulPose(new Vector3f(1.0F, 1.0F, 0.0F).rotationDegrees(180));
            poseStack.scale(0.35f, 0.35f, 0.35f);
        } else {
            poseStack.scale(0.8f, 0.8f, 0.8f);
        }
        buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, POSITION_TEX);
        renderRing(poseStack, buffer, radius);
        tessellator.end();

        poseStack.popPose();
    }

    private void renderCube(PoseStack poseStack, BufferBuilder buffer, float radius) {
        float halfSize = radius / 2.0f;
        Matrix4f matrix = poseStack.last().pose();
        poseStack.pushPose();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();

        buffer.begin(VertexFormat.Mode.QUADS, POSITION_TEX);


        buffer.vertex(matrix, -halfSize, -halfSize, -halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, -halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, -halfSize).uv(0.0F, 1.0F).endVertex();

        // Back face
        buffer.vertex(matrix, -halfSize, -halfSize, halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        // Left face
        buffer.vertex(matrix, -halfSize, -halfSize, halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, -halfSize, -halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, -halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        // Right face
        buffer.vertex(matrix, halfSize, -halfSize, halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, -halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        // Top face
        buffer.vertex(matrix, -halfSize, halfSize, -halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        // Bottom face
        buffer.vertex(matrix, -halfSize, -halfSize, -halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, -halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        Tesselator.getInstance().end();
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private void renderRing(PoseStack poseStack, BufferBuilder buffer, float innerRadius) {
        final double outerRadius = innerRadius / 0.9;
        Matrix4f matrix = poseStack.last().pose();
        final int segments = 32;  // Increase the segments for smoother appearance
        for (int i = 0; i <= segments; i++) {
            float theta = (float) (i * Math.PI * 2 / segments);

            // Outer ring vertices
            float xOuter = (float) (outerRadius * Math.cos(theta));
            float zOuter = (float) (outerRadius * Math.sin(theta));
            buffer.vertex(matrix, xOuter, 0.0F, zOuter).uv((float) i / segments, 0.0F).endVertex();

            // Inner ring vertices
            float xInner = (float) (innerRadius * Math.cos(theta));
            float zInner = (float) (innerRadius * Math.sin(theta));
            buffer.vertex(matrix, xInner, 0.0F, zInner).uv((float) i / segments, 1.0F).endVertex();
        }
    }

    public void writeDataInfo() {
        this.star = SpaceInit.solarSystem.getStarList().get(0);
        this.planets = star.getPlanetList();
        this.planets.forEach(planets -> satellites.addAll(planets.getSatelliteList()));
        this.asteroids = star.getAsteroidList();
        writeData = true;
        time = (float) (75 * random.nextDouble());
    }

    public void render(final TileEntityHologramSpace te, RenderLevelStageEvent event) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(te.getPos().getX() + 0.5f, te.getPos().getY() + 1,
                te.getPos().getZ() + 0.5f
        );
        if (!Minecraft.getInstance().isPaused()) {
            time += 0.00025f;
        }
        poseStack.scale(1, 1, 1);
        if (!writeData) {
            writeDataInfo();
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
        for (IFakeBody fakeBody : te.fakeBodyList) {
            if (fakeBody.getTimerTo().canWork()) {
                renderParabolicTrajectory(poseStack, event,
                        time,
                        SpaceInit.earth,
                        fakeBody.getBody(),
                        (float) fakeBody.getTimerTo().getProgressBar(),
                        fakeBody.getRover().getItemStack()
                );
            } else {
                renderParabolicTrajectory(poseStack, event,
                        time,
                        fakeBody.getBody(),
                        SpaceInit.earth,
                        (float) fakeBody.getTimerFrom().getProgressBar(),
                        fakeBody.getRover().getItemStack()
                );
            }
        }
        renderPlanet(poseStack, event, (float) star.getSize(), star.getLocation(), (float) star.getRotationTimeX(time), 0.5F,
                (float) star.getRotationTimeZ(time), (float) star.getRotation(time), star.getRotationAngle()
        );

        this.planets.forEach(planets -> {
            renderPlanet(poseStack, event, (float) planets.getSize(), planets.getLocation(), (float) planets.getRotationTimeX(time), 0.5F,
                    (float) planets.getRotationTimeZ(time), (float) planets.getRotation(time), planets.getRotationAngle()
            );
            if (planets.getRing() != null) {
                renderRings(poseStack, event, (float) planets.getSize(), SATURN_RING_TEXTURE, (float) planets.getRotationTimeX(time), 0.5F,
                        (float) planets.getRotationTimeZ(time), planets.getRing() == EnumRing.HORIZONTAL
                );
            }
        });
        this.satellites.forEach(planets -> {
                    renderPlanet(poseStack, event, (float) planets.getSize(), planets.getLocation(), (float) planets.getRotationTimeX(time), 0.5F,
                            (float) planets.getRotationTimeZ(time), (float) planets.getRotation(time), planets.getRotationAngle()
                    );
                }
        );
        final float deltaTime = time;
        asteroids.forEach(asteroids -> {
            asteroids.getMiniAsteroid().forEach(miniAsteroid -> {

                float currentAngle = miniAsteroid.getY() + miniAsteroid.getRotationSpeed() * deltaTime;


                float x = miniAsteroid.getX() * (float) Math.cos(currentAngle);
                float z = miniAsteroid.getX() * (float) Math.sin(currentAngle);


                renderAsteroid(poseStack, event, miniAsteroid.getSize(), x, 0.5f, z, miniAsteroid.getRotationSpeed(), asteroids);
            });
        });
        poseStack.popPose();
    }

    private void renderAsteroid(PoseStack poseStack, RenderLevelStageEvent event, float size, float x, float y, float z, float rotationSpeed, IAsteroid asteroid) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotationSpeed));
        RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1);

        renderPlanet(poseStack, event, size, asteroid.getLocation(), 0, 0, 0, 0, 0);
        poseStack.popPose();
    }
}
