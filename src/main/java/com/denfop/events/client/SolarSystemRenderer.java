package com.denfop.events.client;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.space.EnumRing;
import com.denfop.api.space.IAsteroid;
import com.denfop.api.space.IBody;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.ISatellite;
import com.denfop.api.space.IStar;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumRoversLevel;
import com.denfop.tiles.mechanism.TileEntityHologramSpace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SolarSystemRenderer {

    public static final ResourceLocation SATURN_RING_TEXTURE = new ResourceLocation(
            Constants.MOD_ID,
            "textures/planet/saturn_ring" +
                    ".png"
    );
    public static final ResourceLocation ASTEROID_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/planet/asteroid" +
            ".png");
    static Map<IBody, Map<IBody, Float[]>> trajectories = new HashMap<>();
    static Random random = new Random(42);
    boolean writeData = false;
    IStar star;
    List<IPlanet> planets;
    List<ISatellite> satellites = new ArrayList<>();
    private Minecraft minecraft = Minecraft.getMinecraft();
    private float time;
    private List<IAsteroid> asteroids;

    public SolarSystemRenderer() {
    }

    public static void renderParabolicTrajectory(float time, IBody planets, IBody planets1, float progress, ItemStack item) {
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
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        GlStateManager.glLineWidth(2);
        for (int i = 0; i < steps; i++) {
            float x = trajectory[i * 3];
            float y = trajectory[i * 3 + 1];
            float z = trajectory[i * 3 + 2];
            byte color = 0;
            if (progress * steps >= i) {
                color = 1;
            }
            buffer.pos(x, y, z).color(0, color, 0, 1f).endVertex();
        }

        tessellator.draw();
        GlStateManager.glLineWidth(1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

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
        // Средние координаты

        float centerX = (x1 + x2) / 2.0F;
        float centerZ = (z1 + z2) / 2.0F;

        // Вычисление коэффициентов параболы
        float a = (centerY - y1) / ((centerX - x1) * (centerX - x1));
        a = Math.max(a, -0.5f);
        Float[] trajectory = new Float[steps * 3];

        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);
            float x = x1 + t * (x2 - x1);
            float z = z1 + t * (z2 - z1);
            float y = a * (x - x1) * (x - x2) + y1; // Парабола

            trajectory[i * 3] = x;
            trajectory[i * 3 + 1] = y;
            trajectory[i * 3 + 2] = z;
        }
        return trajectory;

    }

    private static void renderItemOnTrajectory(float[] trajectory, float progress, ItemStack item) {
        // Определяем текущую позицию предмета на траектории
        int steps = trajectory.length / 3;
        int index = (int) (progress * (steps - 1));

        float x = trajectory[index * 3];
        float y = trajectory[index * 3 + 1];
        float z = trajectory[index * 3 + 2];
        int nextIndex = Math.min(index + 1, steps - 1);
        float nextX = trajectory[nextIndex * 3];
        float nextY = trajectory[nextIndex * 3 + 1];
        float nextZ = trajectory[nextIndex * 3 + 2];
        float vX = nextX - x;
        float vY = nextY - y;
        float vZ = nextZ - z;
        final EnumRoversLevel level = ((IRoversItem) item.getItem()).getLevel();
        switch (level) {
            case ONE:
                item = new ItemStack(IUItem.rocket);
                break;
            case TWO:
                item = new ItemStack(IUItem.adv_rocket);
                break;
            case THREE:
                item = new ItemStack(IUItem.imp_rocket);
                break;
            case FOUR:
                item = new ItemStack(IUItem.per_rocket);
                break;
        }


        // Вычисляем угол yaw (поворот вокруг оси Y)
        double yaw = Math.toDegrees(Math.atan2(
                vZ,
                vX
        )); // arctan2 используется для корректного вычисления угла в нужном квадранте

        // Вычисляем угол pitch (поворот относительно горизонтальной оси X)
        double pitch = Math.toDegrees(Math.atan2(vY, Math.sqrt(vX * vX + vZ * vZ))); // angle from horizontal plane

        // Roll не требуется в данном контексте, но если нужно вычислить, можно использовать:
        double roll = 0;

        // Теперь рендерим предмет
        GlStateManager.pushMatrix();

        // Перемещаем объект в точку начала траектории
        GlStateManager.translate(x, y, z);

        // Поворот по yaw (поворот вокруг оси Y)
        final IBakedModel model = Minecraft
                .getMinecraft()
                .getRenderItem()
                .getItemModelWithOverrides(item, null, null);


        float newPitch = (float) pitch - 22.5f;
        GlStateManager.rotate((float) -90, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((float) yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) pitch, 1.0F, 0, 0.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.scale(0.1F, 0.1F, 0.1F);

        Minecraft.getMinecraft().getRenderItem().renderItem(
                item, model

        );

        // Восстанавливаем матрицу
        GlStateManager.popMatrix();
    }

    private void renderPlanet(
            float radius,
            ResourceLocation texture,
            float x,
            float y,
            float z,
            float rotation,
            float rotationAngle
    ) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(rotationAngle, 0.0f, 0.0f, 1.0f);
        GlStateManager.color(1, 1, 1, 1);
        minecraft.getTextureManager().bindTexture(texture);
        Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();

        renderCube(buffer, radius);
        GlStateManager.popMatrix();
    }

    private void renderRings(float radius, ResourceLocation texture, float x, float y, float z, boolean isSaturn) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        minecraft.getTextureManager().bindTexture(texture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        if (!isSaturn) {

            GlStateManager.rotate(180, 1.0F, 1.0F, 0.0F);
        }
        buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
        renderRing(buffer, radius);
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    private void renderCube(BufferBuilder buffer, float radius) {
        float halfSize = radius / 2.0f;

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        // Front face
        buffer.pos(-halfSize, -halfSize, -halfSize).tex(0.0F, 0.0F).endVertex();
        buffer.pos(halfSize, -halfSize, -halfSize).tex(1.0F, 0.0F).endVertex();
        buffer.pos(halfSize, halfSize, -halfSize).tex(1.0F, 1.0F).endVertex();
        buffer.pos(-halfSize, halfSize, -halfSize).tex(0.0F, 1.0F).endVertex();

        // Back face
        buffer.pos(-halfSize, -halfSize, halfSize).tex(0.0F, 0.0F).endVertex();
        buffer.pos(halfSize, -halfSize, halfSize).tex(1.0F, 0.0F).endVertex();
        buffer.pos(halfSize, halfSize, halfSize).tex(1.0F, 1.0F).endVertex();
        buffer.pos(-halfSize, halfSize, halfSize).tex(0.0F, 1.0F).endVertex();

        // Left face
        buffer.pos(-halfSize, -halfSize, halfSize).tex(0.0F, 0.0F).endVertex();
        buffer.pos(-halfSize, -halfSize, -halfSize).tex(1.0F, 0.0F).endVertex();
        buffer.pos(-halfSize, halfSize, -halfSize).tex(1.0F, 1.0F).endVertex();
        buffer.pos(-halfSize, halfSize, halfSize).tex(0.0F, 1.0F).endVertex();

        // Right face
        buffer.pos(halfSize, -halfSize, halfSize).tex(0.0F, 0.0F).endVertex();
        buffer.pos(halfSize, -halfSize, -halfSize).tex(1.0F, 0.0F).endVertex();
        buffer.pos(halfSize, halfSize, -halfSize).tex(1.0F, 1.0F).endVertex();
        buffer.pos(halfSize, halfSize, halfSize).tex(0.0F, 1.0F).endVertex();

        // Top face
        buffer.pos(-halfSize, halfSize, -halfSize).tex(0.0F, 0.0F).endVertex();
        buffer.pos(halfSize, halfSize, -halfSize).tex(1.0F, 0.0F).endVertex();
        buffer.pos(halfSize, halfSize, halfSize).tex(1.0F, 1.0F).endVertex();
        buffer.pos(-halfSize, halfSize, halfSize).tex(0.0F, 1.0F).endVertex();

        // Bottom face
        buffer.pos(-halfSize, -halfSize, -halfSize).tex(0.0F, 0.0F).endVertex();
        buffer.pos(halfSize, -halfSize, -halfSize).tex(1.0F, 0.0F).endVertex();
        buffer.pos(halfSize, -halfSize, halfSize).tex(1.0F, 1.0F).endVertex();
        buffer.pos(-halfSize, -halfSize, halfSize).tex(0.0F, 1.0F).endVertex();

        Tessellator.getInstance().draw();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void renderRing(BufferBuilder buffer, float innerRadius) {
        final double outerRadius = innerRadius / 0.9;
        final int segments = 64;  // Increase the segments for smoother appearance
        for (int i = 0; i < segments; i++) {
            float theta = (float) (i * Math.PI * 2 / segments);

            // Outer ring vertices
            float xOuter = (float) (outerRadius * Math.cos(theta));
            float zOuter = (float) (outerRadius * Math.sin(theta));
            buffer.pos(xOuter, 0.0F, zOuter).tex((float) i / segments, 0.0F).endVertex();

            // Inner ring vertices
            float xInner = (float) (innerRadius * Math.cos(theta));
            float zInner = (float) (innerRadius * Math.sin(theta));
            buffer.pos(xInner, 0.0F, zInner).tex((float) i / segments, 1.0F).endVertex();
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

    public void render(final TileEntityHologramSpace te) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(te.getPos().getX() + 0.5f, te.getPos().getY() + 1,
                te.getPos().getZ() + 0.5f
        );
        if (!Minecraft.getMinecraft().isGamePaused()) {
            time += 0.00025f;
        }
        GlStateManager.scale(1, 1, 1);
        if (!writeData) {
            writeDataInfo();
        }
        GlStateManager.color(1, 1, 1, 1);
        for (IFakeBody fakeBody : te.fakeBodyList) {
            if (fakeBody.getTimerTo().canWork()) {
                renderParabolicTrajectory(
                        time,
                        SpaceInit.earth,
                        fakeBody.getBody(),
                        (float) fakeBody.getTimerTo().getProgressBar(),
                        fakeBody.getRover().getItemStack()
                );
            } else {
                renderParabolicTrajectory(
                        time,
                        fakeBody.getBody(),
                        SpaceInit.earth,
                        (float) fakeBody.getTimerFrom().getProgressBar(),
                        fakeBody.getRover().getItemStack()
                );
            }
        }
        renderPlanet((float) star.getSize(), star.getLocation(), (float) star.getRotationTimeX(time), 0.5F,
                (float) star.getRotationTimeZ(time), (float) star.getRotation(time), star.getRotationAngle()
        );

        this.planets.forEach(planets -> {
            renderPlanet((float) planets.getSize(), planets.getLocation(), (float) planets.getRotationTimeX(time), 0.5F,
                    (float) planets.getRotationTimeZ(time), (float) planets.getRotation(time), planets.getRotationAngle()
            );
            if (planets.getRing() != null) {
                renderRings((float) planets.getSize(), SATURN_RING_TEXTURE, (float) planets.getRotationTimeX(time), 0.5F,
                        (float) planets.getRotationTimeZ(time), planets.getRing() == EnumRing.HORIZONTAL
                );
            }
        });
        this.satellites.forEach(planets -> {
                    renderPlanet((float) planets.getSize(), planets.getLocation(), (float) planets.getRotationTimeX(time), 0.5F,
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


                renderAsteroid(miniAsteroid.getSize(), x, 0.5f, z, miniAsteroid.getRotationSpeed());
            });
        });
        GlStateManager.popMatrix();
    }

    private void renderAsteroid(float size, float x, float y, float z, float rotationSpeed) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(rotationSpeed, 0.0f, 1.0f, 0.0f);
        GlStateManager.color(0.5f, 0.5f, 0.5f); // Серый цвет

        // Отрисовка астероида (может быть простой сферой)
        renderPlanet(size, ASTEROID_TEXTURE, 0, 0, 0, 0, 0); // renderPlanet можно использовать для отрисовки объекта без текстуры

        GlStateManager.popMatrix();
    }

}
