package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.space.*;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.widget.*;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuResearchTableSpace;
import com.denfop.network.packet.PacketAddBuildingToColony;
import com.denfop.network.packet.PacketUpdateBody;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.lang.System;
import java.util.*;
import java.util.stream.Collectors;

import static com.denfop.api.space.BaseSpaceSystem.rocketFuel;
import static com.denfop.api.space.BaseSpaceSystem.rocketFuelCoef;

public class ScreenResearchTableSpace<T extends ContainerMenuResearchTableSpace> extends ScreenMain<ContainerMenuResearchTableSpace> {

    public final List<float[]> cachedStars = new ArrayList<>();
    private final float ANIMATION_DURATION = 900f;
    public int mode = 0;
    public IStar star;
    public IBody focusedPlanet = null;
    public boolean starsGenerated1 = false;
    public boolean hoverColonies = false;
    public boolean hoverResource = false;
    public boolean hoverExpedition = false;
    public int textIndex = 0;
    public boolean starsGenerated = false;
    public float scale = 1.0F;
    public int offsetX = 0, offsetY = 0;
    public boolean isDragging = false;
    public int lastMouseX, lastMouseY;
    public Result minimumLimit;
    public List<ScreenDefaultResearchTable> defaultResearchGuis = new ArrayList<>();
    boolean hoverUp = false;
    boolean hoverOpen = false;
    boolean hoverDown = false;
    int systemId = 0;
    List<ISystem> systems;
    double scaleWindow = -1f;
    boolean hoverBack = false;
    int valuePage = 0;
    int maxValuePage = 9;
    boolean hoverNextStar = false;
    boolean hoverBackStar = false;
    Map<Integer, Integer> rocketPlanetRequirements = Map.of(
            1, 1,
            2, 1,
            3, 3,
            4, 5
    );
    Map<Integer, Integer> fuelPlanetRequirements = Map.of(
            1, 1,
            2, 1,
            3, 5,
            4, 6
    );
    private int addPoint;
    private int valueBody = 0;
    private float scaleBackStar = 1.0f;
    private float scaleNextStar = 1.0f;
    private boolean growingBack = true;
    private boolean growingNext = true;
    private float animationProgress = 1.0f;
    private boolean animating = false;
    private boolean animatingForward = true;
    private long animationStartTime;

    public ScreenResearchTableSpace(ContainerMenuResearchTableSpace guiContainer) {
        super(guiContainer, EnumTypeStyle.PERFECT);
        imageHeight = 255;
        imageWidth = 255;
        this.componentList.clear();

    }

    public static void enableScissor(int p_239261_, int p_239262_, int p_239263_, int p_239264_) {
        Window window = Minecraft.getInstance().getWindow();
        int i = window.getHeight();
        double d0 = window.getGuiScale();
        double d1 = (double) p_239261_ * d0;
        double d2 = (double) i - (double) p_239264_ * d0;
        double d3 = (double) (p_239263_ - p_239261_) * d0;
        double d4 = (double) (p_239264_ - p_239262_) * d0;
        RenderSystem.enableScissor((int) d1, (int) d2, Math.max(0, (int) d3), Math.max(0, (int) d4));
    }

    public void renderBackground(PoseStack pPoseStack, int pVOffset) {
        if (this.minecraft.level != null) {
            this.fillGradient(pPoseStack, 0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.renderDirtBackground(pVOffset);
        }

    }

    @Override
    public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
        imageHeight = 255;
        imageWidth = 255;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        super.render(p_97795_, p_97796_, p_97797_, p_97798_);

    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {

    }

    private void renderRings(PoseStack poseStack, IPlanet planet) {
        if (planet.getRing() == null) {
            return;
        }
        final boolean isSaturn = planet.getRing() == EnumRing.HORIZONTAL;
        PoseStack pose = poseStack;
        pose.pushPose();
        double time = container.base.getWorld().getGameTime();
        double angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
        float planetX = (float) (planet.getDistance() * Math.cos(angle));
        float planetY = (float) (planet.getDistance() * Math.sin(angle));
        final double size = planet.getSize() * 2;
        pose.translate(3 - 1.2, -0.2125, 0);
        pose.translate(planetX, planetY, 0);

        if (!isSaturn) {
            pose.mulPose(Vector3f.ZP.rotationDegrees(270));
        }
        pose.scale((float) ((0.5 / 128D) * size), (float) ((0.5 / 128D) * size / 32), 1);

        RenderSystem.setShaderColor(0, 0, 1, 1);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
        drawTexturedModalRect(poseStack, -128, -128, 0, 0, 256, 256);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        pose.popPose();
    }

    public void renderStars(PoseStack poseStack, int x, int y, int width, int height, int starCount) {
        if (!starsGenerated) {
            generateStars(poseStack, x, y, width, height, starCount);
            starsGenerated = true;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        for (float[] star : cachedStars) {
            buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION);

            buffer.vertex(matrix, star[0], star[1], 0).endVertex();
            buffer.vertex(matrix, star[0] * 1.001f, star[1] * 1.001f, 0).endVertex();
            tessellator.end();
        }


        RenderSystem.disableBlend();
    }

    private void generateStars(PoseStack poseStack, int x, int y, int width, int height, int starCount) {
        Random random = new Random();
        cachedStars.clear();

        for (int i = 0; i < starCount; i++) {
            float starX = x + random.nextFloat() * width;
            float starY = y + random.nextFloat() * height;
            cachedStars.add(new float[]{starX, starY});
        }
    }

    private void drawLine(PoseStack poseStack, BufferBuilder buffer, double x1, double y1, double x2, double y2) {
        buffer.vertex(poseStack.last().pose(), (float) x1, (float) y1, 0).color(0, 255, 1, 255).endVertex();
        buffer.vertex(poseStack.last().pose(), (float) x2, (float) y2, 0).color(0, 255, 1, 255).endVertex();
    }

    protected void drawBackground(PoseStack poseStack) {
        componentList.forEach(guiComponent -> guiComponent.drawBackground(poseStack, guiLeft(), guiTop()));

    }

    @Override
    public void changeParams() {
        super.changeParams();
        imageHeight = 4000;
        imageWidth = 9000;
    }

    private void drawScaledTexture(PoseStack graphics, float x, float y, int u, int v, int w, int h, float scale) {
        PoseStack poseStack = graphics;
        poseStack.pushPose();
        poseStack.translate(x + w / 2f, y + h / 2f, 0);
        poseStack.scale(scale, scale, 1.0f);
        poseStack.translate(-w / 2f, -h / 2f, 0);
        drawTexturedModalRect(graphics, 0, 0, u, v, w, h);
        poseStack.popPose();
    }

    public boolean needRenderForeground() {
        return false;
    }

    public void drawRect(PoseStack poseStack, ResourceLocation texture, float x, float y, float z, float scaleX, float scaleY, int u, int v, int w, int h) {
        PoseStack pose = poseStack;
        pose.pushPose();
        RenderSystem.enableBlend();
        bindTexture(texture);
        pose.translate(x, y, z);
        pose.scale(scaleX, scaleY, 1);
        drawTexturedModalRect(poseStack, 0, 0, u, v, w, h);
        RenderSystem.disableBlend();
        pose.popPose();
    }

    public void drawCenteredText(PoseStack poseStack, String text, float x, float y, float z, float scale, int color) {
        PoseStack pose = poseStack;
        pose.pushPose();
        pose.translate(x - (getStringWidth(text) / 2f) * scale, y, z);
        pose.scale(scale, scale, 1);
        this.drawString(poseStack, text, 0, 0, color);
        pose.popPose();
    }

    public void drawAvailability(PoseStack poseStack, boolean available, String label, float x, float y, float z) {
        drawRect(poseStack,
                new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"),
                x, y, z, 0.7f, 0.7f,
                0, available ? 125 : 145, 103, 20
        );
        drawCenteredText(poseStack, available ? Localization.translate("iu.space.planet.available") + " " + label : Localization.translate("iu.space.planet.unavailable") + " " + label,
                x + (102 / 2f) * 0.7f, y + 5, z + 5, 0.6f,
                ModUtils.convertRGBAcolorToInt(255, 255, 255)
        );
    }

    public void drawLevelIcon(PoseStack poseStack, int level, float x, float y, float z, float scale) {
        PoseStack pose = poseStack;
        pose.pushPose();
        RenderSystem.enableBlend();
        pose.translate(x, y, z);
        pose.scale(scale, scale, 1);
        switch (level) {
            case 1 -> drawTexturedModalRect(poseStack, 0, 0, 90, 0, 7, 12);
            case 2 -> drawTexturedModalRect(poseStack, 0, 0, 98, 0, 14, 12);
            case 3 -> drawTexturedModalRect(poseStack, 0, 0, 113, 0, 18, 12);
            case 4 -> drawTexturedModalRect(poseStack, 0, 0, 131, 0, 19, 12);
            case 5 -> drawTexturedModalRect(poseStack, 0, 0, 149, 0, 12, 12);
            case 6 -> drawTexturedModalRect(poseStack, 0, 0, 162, 0, 18, 12);
        }
        RenderSystem.disableBlend();
        pose.popPose();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        PoseStack pose = poseStack;
        this.addPoint = 0;
        valueBody = 0;
        for (int i = 0; i < 9; i++)
            drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 6 + 18 + i * 18, guiTop + 231, 0, 1f, 1f, 222, 1, 20, 20);
        for (int i = 10; i < 11; i++) {
            RenderSystem.enableBlend();
            drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 6 + 18 + i * 18, guiTop + 231, 0, 1f, 1f, 201, 1, 20, 20);
            RenderSystem.disableBlend();
        }
        if (mode == 0) {
            renderMainMenu(poseStack, pose, partialTicks, mouseX, mouseY);
        } else if (mode == 1) {
            renderStarSystem(poseStack, pose, partialTicks, mouseX, mouseY);
        } else if (mode == 2) {
            boolean isScissor = !defaultResearchGuis.isEmpty();
            if (!isScissor) {

                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 9, guiTop + 28, 20, 0.5f, 0.5f, !hoverBack ? 139 : 170, 1, 30, 30);
                RenderSystem.enableBlend();
                new SpaceMainBlueInterfaceWidget(this, 25, 25, 200, 200).drawBackground(poseStack, guiLeft, guiTop);
                RenderSystem.disableBlend();
                drawRect(poseStack, focusedPlanet.getLocation(), guiLeft + 47, guiTop + 35, 20, 1 / (256 / 32f), 1 / (256 / 32f), 0, 0, 256, 256);
                RenderSystem.enableBlend();
                new SpaceMainGreenInterfaceWidget(this, 45, 33, 37, 36).drawBackground(poseStack, guiLeft, guiTop);
                RenderSystem.disableBlend();
                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30, guiTop + 70, 0, 0.63f, 0.63f, 0, 105, 103, 20);

                drawCenteredText(poseStack, Localization.translate("iu.body." + focusedPlanet.getName()), guiLeft + 30 + (102 / 2f) * 0.63f, guiTop + 70 + 3, 20, 0.63f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30, guiTop + 70 + 17, 20, 0.5f, 0.5f, 0, 182, 127, 49);

                Data data = this.container.base.dataMap.get(focusedPlanet);
                int percent = (int) (data.getPercent() * 122 / 100D);

                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 33, guiTop + 70 + 17, 20, 0.5f, 0.5f, 5, 165, percent, 17);

                if (data.getPercent() >= 0)
                    drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30, guiTop + 70 + 26 - 0.5f, 20, 0.5f, 0.5f, 142, 63, 26, 46);
                if (data.getPercent() >= 20)
                    drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 45, guiTop + 70 + 25.5f, 20, 0.5f, 0.5f, 169, 63, 21, 46);
                if (data.getPercent() >= 50)
                    drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 57.5f, guiTop + 70 + 26, 20, 0.5f, 0.5f, 190, 64, 26, 42);
                if (data.getPercent() >= 80)
                    drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 71.5f, guiTop + 70 + 25.5f, 20, 0.5f, 0.5f, 216, 63, 20, 42);
                if (data.getPercent() >= 100)
                    drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 83, guiTop + 70 + 25.5f, 20, 0.5f, 0.5f, 236, 63, 20, 42);

                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30, guiTop + 115, 0, 0.63f, 0.63f, 0, 105, 103, 20);
                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30, guiTop + 135, 0, 0.63f, 0.63f, 0, 105, 103, 20);
                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30, guiTop + 155, 0, 0.63f, 0.63f, 0, data.getPercent() >= 100 ? 105 : 145, 103, 20);

                drawCenteredText(poseStack, Localization.translate("iu.space.planet.resource"), guiLeft + 30 + (102 / 2f) * 0.63f, guiTop + 115 + 3, 20, 0.63f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
                drawCenteredText(poseStack, Localization.translate("iu.space.planet.expedition"), guiLeft + 30 + (102 / 2f) * 0.63f, guiTop + 135 + 3, 20, 0.63f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
                drawCenteredText(poseStack, Localization.translate("iu.space.planet.colony"), guiLeft + 30 + (102 / 2f) * 0.63f, guiTop + 155 + 3, 20, 0.63f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
                RenderSystem.enableBlend();

                new SpaceMainBlueInterfaceWidget(this, 30, 170, 66, 40).drawBackground(poseStack, guiLeft, guiTop);
                RenderSystem.disableBlend();

                bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"));
                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 35, guiTop + 175, 0, 0.63f, 0.63f, 0, 0, 21, 21);

                Result result = minimumLimit;
                drawLevelIcon(poseStack, result.allocations.fuelLevel, guiLeft + 43, guiTop + 182, 0, 0.63f);
                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 58, guiTop + 175, 0, 0.63f, 0.63f, 168, 21, 24, 23);
                drawLevelIcon(poseStack, result.allocations.rocketLevel, guiLeft + 66, guiTop + 182, 0, 0.63f);
                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 80, guiTop + 175, 0, 0.63f, 0.63f, 22, 23, 13, 20);

                drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 78, guiTop + 192, 0, 0.63f, 0.63f, 192, 22, 21, 21);


                int temperature = focusedPlanet.getTemperature();
                if (temperature > 150) {
                    int count = (int) Math.ceil((temperature - 150) / 350D);
                    drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 35, guiTop + 192, 0, 0.63f, 0.63f, 44, 0, 21, 21);
                    drawLevelIcon(poseStack, count, guiLeft + 45, guiTop + 200, 0, 0.63f);
                } else if (temperature < -125) {
                    int count = (int) Math.ceil((Math.abs(temperature) - 125) / 37D);
                    drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 35, guiTop + 192, 0, 0.63f, 0.63f, 22, 0, 21, 21);
                    drawLevelIcon(poseStack, count, guiLeft + 45, guiTop + 200, 0, 0.63f);
                }

                if ((focusedPlanet instanceof IPlanet p && p.getPressure()) || (focusedPlanet instanceof ISatellite s && s.getPressure())) {
                    drawRect(poseStack, new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"), guiLeft + 58, guiTop + 192, 0, 0.63f, 0.63f, 66, 0, 21, 21);
                }
                RenderSystem.enableBlend();
                new SpaceMainBlueInterfaceWidget(this, 110, 33, 100, 100).drawBackground(poseStack, guiLeft, guiTop);
                RenderSystem.disableBlend();

                String text = getInformationFromBody(focusedPlanet, data.getPercent());
                drawTextInCanvas(poseStack, text, 115, 40, 100, 100, 0.5f);

                drawAvailability(poseStack, true, Localization.translate("iu.rover").toLowerCase(), guiLeft + 123, guiTop + 142, 0);
                drawAvailability(poseStack, data.getPercent() >= 20, Localization.translate("iu.probe").toLowerCase(), guiLeft + 123, guiTop + 162, 0);
                drawAvailability(poseStack, data.getPercent() >= 50, Localization.translate("iu.satellite").toLowerCase(), guiLeft + 123, guiTop + 182, 0);
                drawAvailability(poseStack, data.getPercent() >= 80, Localization.translate("iu.rocket").toLowerCase(), guiLeft + 123, guiTop + 202, 0);
            }
            pose.pushPose();
            RenderSystem.defaultBlendFunc();
            for (int i = 0; i < defaultResearchGuis.size(); i++) {
                ScreenDefaultResearchTable defaultResearchTable = defaultResearchGuis.get(i);
                pose.translate(0, 0, 200);
                defaultResearchTable.drawGuiContainerBackgroundLayer(poseStack, partialTicks, guiLeft, guiTop);
            }
            pose.popPose();

        }


    }

    public void drawTextInCanvas(PoseStack graphics, String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, float scale) {
        int maxWidth = (int) (canvasWidth / scale);
        int x = canvasX;
        int y = canvasY;
        PoseStack poseStack = graphics;
        List<String> lines = wrapTextWithNewlines(text, maxWidth);

        for (String line : lines) {
            poseStack.pushPose();
            poseStack.translate(guiLeft + x, guiTop + y, 0);
            poseStack.scale(scale, scale, scale);
            font.draw(poseStack, line, 0, 0, 0xFFFFFF);
            poseStack.popPose();

            y += 11;
        }
    }

    private void renderStarSystem(PoseStack poseStack, PoseStack pose, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 0.2F, 1.0F, 1.0F);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
        this.drawTexturedModalRect(poseStack, guiLeft + 30, guiTop + 30, 2, 2, 175, 175);
        RenderSystem.setShaderColor(1, 1, 1.0F, 1.0F);
        pose.pushPose();
        pose.translate(guiLeft + 9, guiTop + 28, 20);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png"));
        pose.scale(0.5f, 0.5f, 1);
        if (!hoverBack)
            drawTexturedModalRect(poseStack, 0, 0, 139, 1, 30, 30);
        else
            drawTexturedModalRect(poseStack, 0, 0, 170, 1, 30, 30);
        pose.popPose();
        enableScissor(this.guiLeft + 30, this.guiTop + 30, this.guiLeft + 30 + 175, this.guiTop + 30 + +175);
        bindTexture(star.getLocation());
        if (scaleWindow != Minecraft.getInstance().getWindow().getGuiScale()) {
            starsGenerated = false;
            scaleWindow = (double) Minecraft.getInstance().getWindow().getGuiScale();
        }
        renderStars(poseStack, this.guiLeft + 30, this.guiTop + 30, 175, 175, 800);

        pose.pushPose();
        float planetX = 0;
        float planetY = 0;
        if (focusedPlanet != null) {
            offsetX = 0;
            offsetY = 0;
            if (focusedPlanet instanceof IPlanet) {
                double time = container.base.getWorld().getGameTime();
                double angle = 2 * Math.PI * (time * focusedPlanet.getOrbitPeriod()) / 400D;
                planetX = (float) (focusedPlanet.getDistance() * Math.cos(angle)) * scale * 16;
                planetY = (float) (focusedPlanet.getDistance() * Math.sin(angle)) * scale * 16;
            } else if (focusedPlanet instanceof ISatellite) {
                ISatellite satellite1 = (ISatellite) focusedPlanet;

                double time = container.base.getWorld().getGameTime();
                double angle = 2 * Math.PI * (time * satellite1.getPlanet().getOrbitPeriod()) / 400D;
                double angle1 = 2 * Math.PI * (time * satellite1.getOrbitPeriod()) / 400D;
                planetX =
                        (float) ((float) (satellite1
                                .getPlanet()
                                .getDistance() * Math.cos(angle)) + (satellite1.getDistance() * 0.8 * Math.cos(angle1))) * scale * 16;
                planetY =
                        (float) ((float) (satellite1
                                .getPlanet()
                                .getDistance() * Math.sin(angle)) + (satellite1.getDistance() * 0.8 * Math.sin(angle1))) * scale * 16;
            } else if (focusedPlanet instanceof IAsteroid) {

                double time = container.base.getWorld().getGameTime();
                double angle = 2 * Math.PI * (time * focusedPlanet.getOrbitPeriod()) / 400D;
                planetX = (float) (focusedPlanet.getDistance() * Math.cos(angle)) * scale * 16;
                planetY = (float) (focusedPlanet.getDistance() * Math.sin(angle)) * scale * 16;
            }
            double centerX = this.guiLeft + 7 + 162 / 2D;
            double centerY = this.guiTop + 80 + 82 / 2D;
            double planetSize = focusedPlanet.getSize() * 2 * scale * 16;

            if (scale > 2D / (focusedPlanet.getSize() * scale)) {
                pose.popPose();
            }

            pose.pushPose();
            pose.translate(centerX - planetX + offsetX, centerY - planetY + offsetY - 3.5 * scale, 0);
            int squareSize = (int) (planetSize * 1.2);
            pose.translate(planetX, planetY, 0);
            pose.scale((float) scale, (float) scale, (float) scale);

            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();

            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            drawLine(pose, buffer, (-squareSize / 2), -squareSize / 2, -squareSize / 4D, -squareSize / 2);
            drawLine(pose, buffer, (squareSize / 2), -squareSize / 2, squareSize / 4D, -squareSize / 2);

            drawLine(pose, buffer, squareSize / 2, -squareSize / 2, squareSize / 2, -squareSize / 4);
            drawLine(pose, buffer, squareSize / 2, squareSize / 2, squareSize / 2, squareSize / 4);

            drawLine(pose, buffer, (-squareSize / 2), squareSize / 2, -squareSize / 4D, squareSize / 2);
            drawLine(pose, buffer, (squareSize / 2), squareSize / 2, squareSize / 4D, squareSize / 2);

            drawLine(pose, buffer, -squareSize / 2, -squareSize / 2, -squareSize / 2, -squareSize / 4);
            drawLine(pose, buffer, -squareSize / 2, squareSize / 2, -squareSize / 2, squareSize / 4);
            tessellator.end();

            pose.popPose();

            pose.translate(this.guiLeft + 7 + 162 / 2D + offsetX - planetX - 28.75 * scale,
                    this.guiTop + 80 + 82 / 2D + offsetY - planetY, 0
            );
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.lineWidth(2);

        } else {
            pose.translate(this.guiLeft + 7 + 162 / 2D + offsetX - planetX,
                    this.guiTop + 80 + 82 / 2D + offsetY - planetY, 0
            );
        }
        pose.scale(scale * 16, scale * 16, 1.0F);

        bindTexture(star.getLocation());
        final double size = star.getSize();
        pose.pushPose();
        pose.translate(3 - 1.2, -0.2125, 0);
        pose.scale((float) ((0.25 / 128D) * size), (float) ((0.25 / 128D) * size), 1);
        drawTexturedModalRect(poseStack, -128, -128, 0, 0, 256, 256);
        pose.popPose();
        pose.pushPose();
        pose.translate(3 - 1.2, -0.2125, 0);
        for (IPlanet planet : star.getPlanetList()) {
            if (planet != SpaceInit.ceres) {
                renderOrbit(pose, planet.getDistance(), planet.getPressure(), planet.hasOxygen());
            }
        }
        pose.popPose();
        for (IPlanet planet : star.getPlanetList()) {
            renderPlanet(poseStack, planet);
            renderRings(poseStack, planet);
            planet.getSatelliteList().forEach(satellite -> renderSatellite(poseStack, planet, satellite));
        }
        star.getAsteroidList().forEach(iAsteroid -> iAsteroid
                .getMiniAsteroid()
                .forEach(asteroid -> this.renderAsteroid(poseStack, asteroid, iAsteroid)));
        pose.popPose();
        disableScissor();
        new SpaceMainInterfaceWidget(this, 30 - 2, 30 - 2, 175 + 4, 175 + 4).drawBackground(poseStack, this.guiLeft, guiTop);
        List<IBody> list = new ArrayList<>(star.getPlanetList());
        list.addAll(star.getAsteroidList());
        list = list.stream()
                .sorted(Comparator.comparingDouble(IBody::getDistance))
                .collect(Collectors.toList());
        maxValuePage = list.size();
        int tempMaxValuePage = Math.min(9, maxValuePage - valuePage);
        int j = 0;
        ResourceLocation back1 = new ResourceLocation("industrialupgrade", "textures/gui/gui_space_other.png");
        bindTexture(back1);
        float scaleSpeed = 0.02f;
        float maxScale = 1.2f;
        float minScale = 1.0f;
        if (hoverUp) {
            if (growingBack) {
                scaleBackStar += scaleSpeed;
                if (scaleBackStar >= maxScale) {
                    scaleBackStar = maxScale;
                    growingBack = false;
                }
            } else {
                scaleBackStar -= scaleSpeed;
                if (scaleBackStar <= minScale) {
                    scaleBackStar = minScale;
                    growingBack = true;
                }
            }
        } else {
            scaleBackStar = 1.0f;
        }


        if (hoverDown) {
            if (growingNext) {
                scaleNextStar += scaleSpeed;
                if (scaleNextStar >= maxScale) {
                    scaleNextStar = maxScale;
                    growingNext = false;
                }
            } else {
                scaleNextStar -= scaleSpeed;
                if (scaleNextStar <= minScale) {
                    scaleNextStar = minScale;
                    growingNext = true;
                }
            }
        } else {
            scaleNextStar = 1.0f;
        }
        if (valuePage > 0) {
            pose.pushPose();
            pose.translate(guiLeft + 229, guiTop + 30, 20);
            pose.scale(scaleBackStar * 0.5f, scaleBackStar * 0.5f, 1);
            if (!hoverUp)
                drawTexturedModalRect(poseStack, 0, 0, 74, 0, 32, 18);
            else
                drawTexturedModalRect(poseStack, 0, 0, 107, 0, 32, 18);
            pose.popPose();
        }
        if (focusedPlanet != null) {
            EnumLevels level1 = EnumLevels.NONE;

            if (focusedPlanet instanceof IPlanet) {
                level1 = ((IPlanet) focusedPlanet).getLevels();
            }
            if (focusedPlanet instanceof ISatellite) {
                level1 = ((ISatellite) focusedPlanet).getLevels();
            }
            if (focusedPlanet instanceof IAsteroid) {
                level1 = ((IAsteroid) focusedPlanet).getLevels();
            }
            pose.pushPose();
            RenderSystem.enableBlend();
            pose.translate(guiLeft + 255 / 4 + 10, guiTop + 3, 20);
            pose.scale(0.75f, 0.75f, 1);
            if (this.container.base.level != null && this.container.base.level != EnumLevels.NONE && this.container.base.level.ordinal() >= level1.ordinal())
                drawTexturedModalRect(poseStack, 0, 0, 0, 37, 103, 70 - 36);
            else
                drawTexturedModalRect(poseStack, 0, 0, 0, 71, 103, 70 - 36);
            RenderSystem.disableBlend();
            pose.popPose();
            pose.pushPose();
            String name = Localization.translate("iu.body." + focusedPlanet.getName());
            pose.translate(this.guiLeft + 255 / 4 + 10 + (103 / 2) * 0.75 - (getStringWidth(name) / 2) * 0.75, guiTop + 7, 20);
            pose.scale(0.75f, 0.75f, 1);
            font.draw(poseStack, name, 0, 0, ModUtils.convertRGBAcolorToInt(255, 255, 255));
            pose.popPose();
            pose.pushPose();


            name = level1.name();

            pose.translate(this.guiLeft + 255 / 4 + 10 + (103 / 2) * 0.75 - (getStringWidth(name) / 2) * 0.75, guiTop + 19, 20);
            pose.scale(0.75f, 0.75f, 1);
            font.draw(poseStack, name, 0, 0, ModUtils.convertRGBAcolorToInt(255, 255, 255));
            pose.popPose();
        }

        int addedPoint = 0;
        for (int i = valuePage; i < tempMaxValuePage + valuePage; i++) {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            bindTexture(back1);
            pose.pushPose();
            IBody body = list.get(i);

            int dop = 0;
            if (focusedPlanet == body)
                dop = 15;
            if (focusedPlanet instanceof ISatellite)
                if (((ISatellite) focusedPlanet).getPlanet() == body)
                    dop = 15;
            if (dop != 0)
                valueBody = i;
            pose.translate(guiLeft + 207 + dop, guiTop + 28 + 12 + j * 19 * 0.65 + addedPoint, 20);
            pose.scale(0.65f, 0.65f, 1);
            EnumLevels level = EnumLevels.NONE;

            if (body instanceof IPlanet) {
                level = ((IPlanet) body).getLevels();
            }
            if (body instanceof ISatellite) {
                level = ((ISatellite) body).getLevels();
            }
            if (body instanceof IAsteroid) {
                level = ((IAsteroid) body).getLevels();
            }
            if (focusedPlanet != body) {
                if (container.base.level != null && container.base.level.ordinal() >= level.ordinal() && level != EnumLevels.NONE && container.base.level != EnumLevels.NONE)
                    drawTexturedModalRect(poseStack, 0, 0, 152, 160, 103, 19);
                else
                    drawTexturedModalRect(poseStack, 0, 0, 152, 180, 103, 19);
            } else {
                drawTexturedModalRect(poseStack, 0, 0, 152, 200, 103, 19);
            }
            pose.scale(1 / 0.65f, 1 / 0.65f, 1);
            bindTexture(body.getLocation());
            pose.translate(4, 2.5, 20);

            pose.scale(1 / (256 / 8f), 1 / (256 / 8f), 1);
            drawTexturedModalRect(poseStack, 0, 0, 0, 0, 256, 256);
            pose.popPose();
            RenderSystem.disableBlend();
            pose.pushPose();
            pose.translate(guiLeft + 207 + 7 + dop + 8, guiTop + 28 + 16 + j * 19 * 0.65 + addedPoint, 20);
            pose.scale(0.65f, 0.65f, 1);
            String name = Localization.translate("iu.body." + list.get(i).getName());
            font.draw(poseStack, name, 0, 0, ModUtils.convertRGBAcolorToInt(255, 255, 255));
            pose.popPose();
            j++;
            if (dop != 0) {
                int size1 = 0;
                List<ISatellite> satelliteList = Collections.emptyList();
                if (focusedPlanet instanceof Planet)
                    satelliteList = ((Planet) focusedPlanet).getSatelliteList();
                if (focusedPlanet instanceof ISatellite)
                    if (((ISatellite) focusedPlanet).getPlanet() == body)
                        satelliteList = ((ISatellite) focusedPlanet).getPlanet().getSatelliteList();
                size1 = satelliteList.size();

                for (int ii = 0; ii < size1; ii++) {
                    bindTexture(back1);
                    ISatellite satellite1 = satelliteList.get(ii);
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.enableBlend();
                    pose.pushPose();
                    pose.translate(guiLeft + 207 + dop * 2, guiTop + 28 + 12 + j * 19 * 0.65 + addedPoint + ii * 19 * 0.65, 20);
                    pose.scale(0.65f, 0.65f, 1);
                    level = EnumLevels.NONE;
                    level = satellite1.getLevels();


                    if (focusedPlanet != satellite1) {
                        if (container.base.level != null && container.base.level.ordinal() >= level.ordinal() && level != EnumLevels.NONE && container.base.level != EnumLevels.NONE)
                            drawTexturedModalRect(poseStack, 0, 0, 152, 161, 103, 19);
                        else
                            drawTexturedModalRect(poseStack, 0, 0, 152, 180, 103, 19);
                    } else {
                        drawTexturedModalRect(poseStack, 0, 0, 152, 200, 103, 19);
                    }
                    pose.scale(1 / 0.65f, 1 / 0.65f, 1);
                    bindTexture(satellite1.getLocation());
                    pose.translate(4, 1.75, 20);

                    pose.scale(1 / (256 / 8f), 1 / (256 / 8f), 1);
                    drawTexturedModalRect(poseStack, 0, 0, 0, 0, 256, 256);
                    pose.popPose();


                    pose.pushPose();
                    pose.translate(guiLeft + 207 + 7 + dop * 2 + 8, guiTop + 28 + 16 + j * 19 * 0.65 + addedPoint + ii * 19 * 0.65, 20);
                    pose.scale(0.65f, 0.65f, 1);
                    name = Localization.translate("iu.body." + satellite1.getName());
                    font.draw(poseStack, name, 0, 0, ModUtils.convertRGBAcolorToInt(255, 255, 255));
                    pose.popPose();
                }
                addedPoint += size1 * 19 * 0.65;
            }

        }
        this.addPoint = addedPoint;
        bindTexture(back1);
        if (tempMaxValuePage > 0 && (maxValuePage - valuePage) > 9) {
            pose.pushPose();
            pose.translate(guiLeft + 230, guiTop + (tempMaxValuePage - 1) * 19 + addedPoint, 20);
            pose.scale(scaleNextStar * 0.5f, scaleNextStar * 0.5f, 1);
            if (!hoverDown)
                drawTexturedModalRect(poseStack, 0, 0, 74, 18, 32, 18);
            else
                drawTexturedModalRect(poseStack, 0, 0, 107, 18, 32, 18);
            pose.popPose();

        }
        if (focusedPlanet != null) {
            EnumLevels level = EnumLevels.NONE;

            if (focusedPlanet instanceof IPlanet) {
                level = ((IPlanet) focusedPlanet).getLevels();
            }
            if (focusedPlanet instanceof ISatellite) {
                level = ((ISatellite) focusedPlanet).getLevels();
            }
            if (focusedPlanet instanceof IAsteroid) {
                level = ((IAsteroid) focusedPlanet).getLevels();
            }
            if (container.base.level != null && container.base.level.ordinal() >= level.ordinal() && level != EnumLevels.NONE && container.base.level != EnumLevels.NONE) {
                RenderSystem.enableBlend();
                bindTexture(back1);
                drawTexturedModalRect(poseStack, guiLeft + 30 + 175 / 2 - 102 / 2, guiTop + 210, 0, 105, 102, 20);
                RenderSystem.disableBlend();
            }
        }
    }

    private void startAnimation(boolean forward) {
        if (animating) return;
        this.animating = true;
        this.animatingForward = forward;
        this.animationStartTime = System.currentTimeMillis();
        this.animationProgress = 0f;
    }

    private void renderMainMenu(PoseStack poseStack, PoseStack pose, float partialTicks, int mouseX, int mouseY) {
        List<ISystem> systems1 = SpaceNet.instance.getSystem();
        this.systems = systems1.stream().filter(iSystem -> !iSystem.getStarList().isEmpty()).collect(Collectors.toList());
        new SpaceMainInterfaceWidget(this, imageWidth / 2 - 80 + 30, 30 + 30, 90, 90).drawBackground(poseStack, this.guiLeft, guiTop);
        ScreenIndustrialUpgrade.bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/gui_space_main.png"));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        pose.pushPose();
        int width1 = 90;
        int height1 = 90;
        float centerX = guiLeft + this.imageWidth / 2 - 80 + 94 + 30;
        float centerY = guiTop + 30 + 30;
        pose.translate(centerX, centerY, 30);
        pose.mulPose(Vector3f.ZP.rotationDegrees(0));
        pose.mulPose(Vector3f.XP.rotationDegrees(25));
        pose.mulPose(Vector3f.YP.rotationDegrees(-45));
        drawTexturedModalRect(poseStack, -2, 0, 0, 0, width1 - 3, height1 + 2);
        drawTexturedModalRect(poseStack, width1 - 6, 0, 251, 0, 5, height1 + 2);
        drawTexturedModalRect(poseStack, -2, height1 + 2, 0, 250, width1 - 1, 6);
        drawTexturedModalRect(poseStack, width1 - 7, height1 + 2, 250, 250, 6, 5);

        pose.popPose();


        ScreenIndustrialUpgrade.bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/gui_space_main.png"));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        pose.pushPose();
        width1 = 90;
        height1 = 90;
        centerX = guiLeft + this.imageWidth / 2 - 80 - 94 + 27 + 30;
        centerY = guiTop + 30 - 25 + 30;
        pose.translate(centerX, centerY, 80);
        pose.mulPose(Vector3f.ZP.rotationDegrees(-0));
        pose.mulPose(Vector3f.XP.rotationDegrees(-25));
        pose.mulPose(Vector3f.YP.rotationDegrees(-45));
        drawTexturedModalRect(poseStack, -2, 0, 0, 0, width1 - 3, height1 + 2);
        drawTexturedModalRect(poseStack, width1 - 6, 0, 251, 0, 5, height1 + 2);
        drawTexturedModalRect(poseStack, -2, height1 + 2, 0, 250, width1 - 1, 6);
        drawTexturedModalRect(poseStack, width1 - 7, height1 + 2, 250, 250, 6, 5);
        pose.popPose();
        bindTexture();
        if (animating) {
            long elapsed = System.currentTimeMillis() - animationStartTime;
            animationProgress = Math.min(elapsed / ANIMATION_DURATION, 1.0f);

            if (animationProgress >= 1.0f) {
                animating = false;
            }
        }

        float offset = 50f * (1.0f - animationProgress);
        if (!animatingForward) offset *= -1;

        // Индексы
        int nextIndex = (systemId + 1) % systems.size();
        int prevIndex = (systemId - 1);
        if (prevIndex < 0)
            prevIndex = systems.size() - 1;
        IStar centerStar = systems.get(systemId).getStarList().get(0);
        IStar nextStar = systems.get(nextIndex).getStarList().get(0);
        IStar prevStar = systems.get(prevIndex).getStarList().get(0);


        enableScissor(guiLeft + imageWidth / 2 - 80 + 4 + 30, guiTop + 30 + 4 + 30,
                guiLeft + imageWidth / 2 - 80 + 90 - 4 + 30, guiTop + 30 + 90 - 4 + 30);
        centerX = guiLeft + this.imageWidth / 2 - 80 + 94 + 30;
        centerY = guiTop + 30 + 30;
        renderPlanet1(poseStack, 52f, centerStar.getLocation(), (float) this.guiLeft + imageWidth / 2 - 35 + offset + 30, (float) this.guiTop + 75 + 30, 0,
                (float) centerStar.getRotation(this.container.base.getWorld().getGameTime()),
                centerStar.getRotationAngle(), 0
        );

        disableScissor();


        pose.pushPose();
        pose.translate(0, 0, 40);

        enableScissor(guiLeft + imageWidth / 2 - 80 + 94 + 1 + 30, guiTop + 10 + 4 + 30,
                guiLeft + imageWidth / 2 - 80 + 94 + 60 + 1 + 30, guiTop + 30 + 70 + 30);
        renderPlanet1(poseStack, 34f, nextStar.getLocation(), (float) centerX + 30 + offset, (float) centerY + 32, 0,
                (float) -45,
                -10, -10
        );

        disableScissor();
        pose.popPose();

        pose.pushPose();
        pose.translate(0, 0, 40);
        centerX = guiLeft + this.imageWidth / 2 - 80 - 94 + 27 + 30;
        centerY = guiTop + 30 - 25 + 30;
        enableScissor((int) (centerX + 1), guiTop + 10 + 4 + 30,
                (int) (centerX + 60), guiTop + 30 + 70 + 30);

        renderPlanet1(poseStack, 34f, prevStar.getLocation(), (float) centerX + 30 + offset, (float) centerY + 32 + 25, 0,
                (float) -45,
                10, -10
        );

        disableScissor();
        pose.popPose();
        ResourceLocation background1 = new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_other.png");
        bindTexture(background1);
        float scaleSpeed = 0.02f;
        float maxScale = 1.2f;
        float minScale = 1.0f;
        if (hoverBackStar) {
            if (growingBack) {
                scaleBackStar += scaleSpeed;
                if (scaleBackStar >= maxScale) {
                    scaleBackStar = maxScale;
                    growingBack = false;
                }
            } else {
                scaleBackStar -= scaleSpeed;
                if (scaleBackStar <= minScale) {
                    scaleBackStar = minScale;
                    growingBack = true;
                }
            }
        } else {
            scaleBackStar = 1.0f;
        }


        if (hoverNextStar) {
            if (growingNext) {
                scaleNextStar += scaleSpeed;
                if (scaleNextStar >= maxScale) {
                    scaleNextStar = maxScale;
                    growingNext = false;
                }
            } else {
                scaleNextStar -= scaleSpeed;
                if (scaleNextStar <= minScale) {
                    scaleNextStar = minScale;
                    growingNext = true;
                }
            }
        } else {
            scaleNextStar = 1.0f;
        }


        if (hoverBackStar) {
            float x = this.guiLeft + imageWidth / 2 - 110 + 30;
            float y = this.guiTop + 75 - 20 + 75 + 30;
            drawScaledTexture(poseStack, x, y, 37, 0, 17, 32, scaleBackStar);
        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + imageWidth / 2 - 110 + 30, (int) this.guiTop + 75 - 20 + 75 + 30, 0, 0, 17, 32);
        }
        if (hoverNextStar) {
            float x = this.guiLeft + imageWidth / 2 + 20 + 30;
            float y = this.guiTop + 75 - 20 + 75 + 30;
            drawScaledTexture(poseStack, x, y, 56, 0, 17, 32, scaleNextStar);
        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + imageWidth / 2 + 20 + 30, (int) this.guiTop + 75 - 20 + 75 + 30, 19, 0, 17, 32);

        }
    }

    private void renderAsteroid(PoseStack poseStack, MiniAsteroid miniAsteroid, IAsteroid asteroid) {
        double time = container.base.getWorld().getGameTime();
        double angle = 2 * Math.PI * (time * miniAsteroid.getRotationSpeed()) / 800D;
        float planetX = (float) (miniAsteroid.getX() * Math.cos(angle));
        float planetY = (float) (miniAsteroid.getX() * Math.sin(angle));
        final double size = miniAsteroid.getSize() * 2;
        bindTexture(asteroid.getLocation());
        PoseStack pose = poseStack;
        pose.pushPose();
        pose.translate(3 - 1.2, -0.2125, 0);
        pose.translate(planetX, planetY, 0);
        pose.scale((float) ((0.25 / 128D) * size), (float) ((0.25 / 128D) * size), 1);
        drawTexturedModalRect(poseStack, -128, -128, 0, 0, 256, 256);
        pose.popPose();
    }

    private void renderOrbit(PoseStack poseStack, double radius, boolean hasPressure, boolean hasOxygen) {
        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        int segments = (int) (32 * Math.max(1, radius / 2));
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float x = (float) (radius * Math.cos(angle));
            float y = (float) (radius * Math.sin(angle));
            if (hasPressure) {
                buffer.vertex(matrix, x, y, 0.0F).color(255, 0, 0, 255).endVertex();

            } else if (hasOxygen) {
                buffer.vertex(matrix, x, y, 0.0F).color(0, 255, 0, 255).endVertex();

            } else {
                buffer.vertex(matrix, x, y, 0.0F).color(0, 0, 255, 255).endVertex();

            }
        }


        tessellator.end();
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        mouseX -= (double) this.leftPos;
        mouseY -= (double) this.topPos;
        if (mode == 1 && mouseX >= 30 && mouseY >= 30 && mouseX <= 30 + 175 && mouseY <= 30 + 175) {
            if (button == 0) {
                int dx = (int) dragX;
                int dy = (int) dragY;
                offsetX += dx;
                offsetY += dy;
                isDragging = true;
            } else {
                isDragging = false;
            }
        } else if (mode == 2) {
            for (int ii = 0; ii < defaultResearchGuis.size(); ii++) {
                ScreenDefaultResearchTable defaultResearchTable = defaultResearchGuis.get(ii);
                if (defaultResearchTable.mouseDragged(mouseX, mouseY, dragX, dragY))
                    return true;
            }
        }
        return isDragging;
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d3) {
        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);
        if (mode == 1 && mouseX >= 30 && mouseY >= 30 && mouseX <= 30 + 175 && mouseY <= 30 + 175) {
            ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;
            int dWheel = (int) (d3 * 10);
            if (dWheel != 0) {
                scale += dWheel > 0 ? 0.1F : -0.1F;
                scale = Mth.clamp(scale, 0.2F, 10.0F);
            }
            if (scrollDirection == ScrollDirection.stopped) {
                if (!isDragging) {
                    isDragging = true;
                } else {
                    int dx = (int) (d - lastMouseX);
                    int dy = (int) (d2 - lastMouseY);
                    offsetX += dx / 8;
                    offsetY -= dy / 8;

                }
                lastMouseX = (int) d;
                lastMouseY = (int) d2;

            } else {
                isDragging = false;
            }
            return true;
        } else if (mode == 1 && mouseX >= 207 && mouseX <= 207 + 60 && mouseY >= 28 + 12 && mouseY <= 28 + 12 + 9 * 19 * 0.65) {
            ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;
            if (scrollDirection != ScrollDirection.stopped) {
                int prevValue = valuePage;
                valuePage += scrollDirection == ScrollDirection.down ? 1 : -1;
                if (valuePage < 0)
                    valuePage = 0;
                if (maxValuePage - valuePage < 9) {
                    valuePage = maxValuePage - 9;
                }
                if (prevValue != valuePage)
                    container.player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
                return true;
            }
        } else if (mode == 2) {
            for (int ii = 0; ii < defaultResearchGuis.size(); ii++) {
                ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;
                ScreenDefaultResearchTable defaultResearchTable = defaultResearchGuis.get(ii);
                if (defaultResearchTable.mouseScrolled(mouseX, mouseY, scrollDirection))
                    return true;
            }
        }
        return super.mouseScrolled(d, d2, d3);
    }

    private void renderPlanet(PoseStack poseStack, IBody planet) {
        double time = container.base.getWorld().getGameTime();
        double angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
        float planetX = (float) (planet.getDistance() * Math.cos(angle));
        float planetY = (float) (planet.getDistance() * Math.sin(angle));
        final double size = planet.getSize() * 2;
        bindTexture(planet.getLocation());
        PoseStack pose = poseStack;
        pose.pushPose();
        pose.translate(3 - 1.2, -0.2125, 0);
        pose.translate(planetX, planetY, 0);
        pose.scale((float) ((0.25 / 128D) * size), (float) ((0.25 / 128D) * size), 1);
        drawTexturedModalRect(poseStack, -128, -128, 0, 0, 256, 256);
        pose.popPose();
    }

    private void renderSatellite(PoseStack poseStack, IBody planet, IBody satellite) {
        double time = container.base.getWorld().getGameTime();
        double angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
        double angle1 = 2 * Math.PI * (time * satellite.getOrbitPeriod()) / 400D;
        float planetX =
                (float) ((float) (planet.getDistance() * Math.cos(angle)) + (satellite.getDistance() * 0.8 * Math.cos(angle1)));
        float planetY =
                (float) ((float) (planet.getDistance() * Math.sin(angle)) + (satellite.getDistance() * 0.8 * Math.sin(angle1)));
        final double size = satellite.getSize() * 2;
        bindTexture(satellite.getLocation());
        PoseStack pose = poseStack;
        pose.pushPose();
        pose.translate(3 - 1.2, -0.2125, 0);
        pose.translate(planetX, planetY, 0); // Без лишнего смещения на size1
        pose.scale((float) ((0.25 / 128D) * size), (float) ((0.25 / 128D) * size), 1);
        drawTexturedModalRect(poseStack, -128, -128, 0, 0, 256, 256); // Центр текстуры в точке (0,0)
        pose.popPose();
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        imageHeight = 255;
        imageWidth = 255;
        boolean isClickedButton = false;
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - xMin;
        int y = j - yMin;
        if (mode == 0) {
            if (x >= 255 / 2 - 80 && x <= 255 / 2 - 80 + 90 && y >= 30 + 30 && y <= 120 + 30) {
                this.mode = 1;
                this.star = this.systems.get(systemId).getStarList().get(0);
                valuePage = 0;
                maxValuePage = 0;
                container.player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
                return;
            }
            if (!animating) {
                if (hoverNextStar) {
                    isClickedButton = true;
                    startAnimation(true);
                    systemId++;
                    if (systemId >= systems.size()) {
                        systemId = 0;
                    }
                }
                if (hoverBackStar) {

                    isClickedButton = true;
                    startAnimation(false);
                    systemId--;
                    if (systemId < 0) {
                        systemId = systems.size() - 1;
                    }
                }
            }

        } else if (mode == 1) {

            if (hoverBack) {
                this.mode = 0;
                this.star = null;
                this.systems = Collections.emptyList();
                this.focusedPlanet = null;
                container.player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
                defaultResearchGuis.clear();
                return;
            }
            if (hoverOpen && focusedPlanet != null) {
                mode = 2;
                int seconds = 0;
                EnumLevels levels = EnumLevels.FIRST;
                if (focusedPlanet instanceof IPlanet) {
                    seconds = (int) ((Math.abs(focusedPlanet.getDistance() - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance())) * (12 * 60 * 0.8));
                    levels = ((IPlanet) focusedPlanet).getLevels();
                    seconds+=focusedPlanet.getSystem().getDistanceFromSolar()*60*60;
                }
                if (focusedPlanet instanceof ISatellite) {
                    ISatellite planet = (ISatellite) focusedPlanet;
                    levels = planet.getLevels();
                    double distancePlanetToPlanet = (planet
                            .getPlanet()
                            .getDistance() - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance());
                    double distanceSatellite = Math.abs(SpaceInit.moon.getDistanceFromPlanet() - planet.getDistanceFromPlanet()) / SpaceInit.moon.getDistanceFromPlanet();
                    if (planet.getPlanet() == SpaceInit.earth) {
                        distanceSatellite = 1;
                    }
                    seconds = (int) (Math.abs(distanceSatellite * 2.5 * 60 * 0.8 + distancePlanetToPlanet * (12 * 60 * 0.8)));
                    seconds+=focusedPlanet.getSystem().getDistanceFromSolar()*60*60;
                }
                if (focusedPlanet instanceof IAsteroid) {
                    IAsteroid planet = (IAsteroid) focusedPlanet;
                    seconds = (int) ((Math.abs(((planet.getMaxDistance() - planet.getMinDistance()) / 2 + planet.getMinDistance()) - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance())) * (12 * 60 * 0.8));
                    levels = planet.getLevels();
                    seconds+=focusedPlanet.getSystem().getDistanceFromSolar()*60*60;
                }
                this.minimumLimit = findOptimalUpgradeDistribution(seconds * 2, levels.ordinal() + 1);

                new PacketUpdateBody(this.container.base, focusedPlanet);
                container.player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
                return;
            }
            int dopX = 0;
            int dopY = 0;
            int jj = 0;
            List<IBody> list = new ArrayList<>(star.getPlanetList());
            list.addAll(star.getAsteroidList());
            list = list.stream()
                    .sorted(Comparator.comparingDouble(IBody::getDistance))
                    .collect(Collectors.toList());
            if (hoverUp || hoverDown) {
                isClickedButton = true;
                valuePage += hoverDown ? 1 : -1;
                if (valuePage < 0)
                    valuePage = 0;
                if (maxValuePage - valuePage < 9) {
                    valuePage = maxValuePage - 9;
                }
            }


            int tempMaxValuePage = Math.min(9, maxValuePage - valuePage);
            for (int ii = valuePage; ii < tempMaxValuePage + valuePage; ii++) {
                IBody body = list.get(ii);
                int dop = 0;
                if (focusedPlanet == body)
                    dop = 15;
                if (focusedPlanet instanceof ISatellite)
                    if (((ISatellite) focusedPlanet).getPlanet() == body)
                        dop = 15;
                int temp = this.addPoint;
                if (ii <= valueBody)
                    temp = 0;
                if (x > 207 + dop && x <= 103 * 0.65 + 207 + dop && y > 28 + (jj + 1) * 19 * 0.65 + temp && y < temp + 28 + (jj + 1) * 19 * 0.65 + 19 * 0.65) {
                    if (focusedPlanet != body)
                        this.focusedPlanet = body;
                    else
                        this.focusedPlanet = null;
                    isClickedButton = true;
                    break;

                }
                if (dop != 0) {
                    List<ISatellite> satelliteList = new ArrayList<>();
                    if (focusedPlanet instanceof IPlanet)
                        satelliteList = ((IPlanet) focusedPlanet).getSatelliteList();
                    if (focusedPlanet instanceof ISatellite)
                        satelliteList = ((ISatellite) focusedPlanet).getPlanet().getSatelliteList();
                    for (int iii = 0; iii < satelliteList.size(); iii++) {
                        if (x > 207 + dop * 2 && x <= 103 * 0.65 + 207 + dop * 2 && y > 28 + 12 + jj * 19 * 0.65 + temp + (iii + 1) * 19 * 0.65 && y < 28 + 12 + jj * 19 * 0.65 + temp + (iii + 2) * 19 * 0.65) {
                            if (focusedPlanet != satelliteList.get(iii))
                                this.focusedPlanet = satelliteList.get(iii);
                            else
                                this.focusedPlanet = null;
                            isClickedButton = true;
                            break;

                        }
                    }
                }
                jj++;

            }

            if (x >= 30 && x <= 30 + 175 && y >= 30 && y <= 30 + 175) {
                if (focusedPlanet != null) {
                    if (focusedPlanet instanceof IPlanet) {
                        double time = container.base.getWorld().getGameTime();
                        double angle = 2 * Math.PI * (time * focusedPlanet.getOrbitPeriod()) / 400D;
                        dopX = (int) ((focusedPlanet.getDistance() * Math.cos(angle)) * scale * 16);
                        dopY = (int) ((focusedPlanet.getDistance() * Math.sin(angle)) * scale * 16);
                        focusedPlanet = null;
                        isClickedButton = true;
                    } else if (focusedPlanet instanceof IAsteroid) {
                        double time = container.base.getWorld().getGameTime();
                        double angle = 2 * Math.PI * (time * focusedPlanet.getOrbitPeriod()) / 400D;
                        dopX = (int) ((focusedPlanet.getDistance() * Math.cos(angle)) * scale * 16);
                        dopY = (int) ((focusedPlanet.getDistance() * Math.sin(angle)) * scale * 16);
                        focusedPlanet = null;
                        isClickedButton = true;
                    } else {
                        ISatellite satellite1 = (ISatellite) focusedPlanet;
                        double time = container.base.getWorld().getGameTime();
                        double angle = 2 * Math.PI * (time * satellite1.getPlanet().getOrbitPeriod()) / 400D;
                        double angle1 = 2 * Math.PI * (time * satellite1.getOrbitPeriod()) / 400D;
                        dopX =
                                (int) ((float) ((float) (satellite1
                                        .getPlanet()
                                        .getDistance() * Math.cos(angle)) + (satellite1.getDistance() * 0.8 * Math.cos(
                                        angle1))) * scale * 16);
                        dopY =
                                (int) (((float) (satellite1
                                        .getPlanet()
                                        .getDistance() * Math.sin(angle)) + (satellite1.getDistance() * 0.8 * Math.sin(
                                        angle1))) * scale * 16);

                        focusedPlanet = null;
                        isClickedButton = true;
                    }
                }
                for (IPlanet planet : star.getPlanetList()) {


                    double time = container.base.getWorld().getGameTime();
                    double angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
                    float planetX = (float) (planet.getDistance() * Math.cos(angle)) * scale * 16;
                    float planetY = (float) (planet.getDistance() * Math.sin(angle)) * scale * 16;
                    double size = planet.getSize() * 2 * scale * 16;

                    if (x >= planetX - size / 4 + 7 + 162 / 2D + offsetX - dopX + 29 * scale && x <= planetX + size / 4 + 7 + 162 / 2D + offsetX - dopX + 29 * scale &&
                            y >= planetY - size / 4 + 80 + 82 / 2D + offsetY - dopY - 4 * scale && y <= planetY + size / 4 + 80 + 82 / 2D + offsetY - dopY - 4 * scale) {
                        focusedPlanet = planet;
                        isClickedButton = true;
                        textIndex = 0;
                        break;
                    }
                    if (planet.getLevels() == EnumLevels.NONE || container.base.dataMap.get(planet).getPercent() >= 2) {
                        for (ISatellite satellite1 : planet.getSatelliteList()) {
                            if (satellite1.getLevels() != EnumLevels.NONE && satellite1
                                    .getLevels()
                                    .ordinal() > container.base.level.ordinal()) {
                                continue;
                            }
                            time = container.base.getWorld().getGameTime();
                            angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
                            final double angle1 = 2 * Math.PI * (time * satellite1.getOrbitPeriod()) / 400D;
                            planetX =
                                    (float) ((float) (planet.getDistance() * Math.cos(angle)) + (satellite1.getDistance() * 0.8 * Math.cos(
                                            angle1))) * scale * 16;
                            planetY =
                                    (float) ((float) (planet.getDistance() * Math.sin(angle)) + (satellite1.getDistance() * 0.8 * Math.sin(
                                            angle1))) * scale * 16;
                            size = satellite1.getSize() * 2 * scale * 16;
                            if (x >= planetX - size / 4 + 7 + 162 / 2D + offsetX - dopX + 29 * scale && x <= planetX + size / 4 + 7 + 162 / 2D + offsetX - dopX + 29 * scale &&
                                    y >= planetY - size / 4 + 80 + 82 / 2D + offsetY - dopY - 4 * scale && y <= planetY + size / 4 + 80 + 82 / 2D + offsetY - dopY - 4 * scale) {
                                focusedPlanet = satellite1;
                                textIndex = 0;
                                isClickedButton = true;
                                break;
                            }
                        }
                    }
                }
            }
        } else if (mode == 2) {
            for (int ii = 0; ii < defaultResearchGuis.size(); ii++) {
                ScreenDefaultResearchTable defaultResearchTable = defaultResearchGuis.get(ii);
                if (defaultResearchTable.mouseClicked(x, y))
                    return;
            }
            for (int ii = 0; ii < defaultResearchGuis.size(); ii++) {
                ScreenDefaultResearchTable defaultResearchTable = defaultResearchGuis.get(ii);
                if (defaultResearchTable.is(x, y))
                    return;
            }
            if (hoverResource && defaultResearchGuis.isEmpty()) {
                boolean find = false;
                for (ScreenDefaultResearchTable defaultResearchTable : defaultResearchGuis) {
                    if (defaultResearchTable instanceof ScreenResourceBody) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    defaultResearchGuis.add(new ScreenResourceBody(this));
                }
            }
            if (hoverColonies && defaultResearchGuis.isEmpty() && ((focusedPlanet instanceof IPlanet && ((IPlanet) focusedPlanet).canHaveColonies()) || (focusedPlanet instanceof ISatellite && ((ISatellite) focusedPlanet).canHaveColonies()) || !(focusedPlanet instanceof IAsteroid))) {

                boolean find = false;
                for (ScreenDefaultResearchTable defaultResearchTable : defaultResearchGuis) {
                    if (defaultResearchTable instanceof ScreenColony) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    defaultResearchGuis.add(new ScreenColony(this));
                }
            }
            if (hoverExpedition && defaultResearchGuis.isEmpty()) {
                boolean find = false;
                for (ScreenDefaultResearchTable defaultResearchTable : defaultResearchGuis) {
                    if (defaultResearchTable instanceof ScreenExpeditionTableSpace) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    defaultResearchGuis.add(new ScreenExpeditionTableSpace(this));
                }
            }
            if (hoverBack) {
                this.mode = 1;
                defaultResearchGuis.clear();
                new PacketUpdateBody(this.container.base, null);
                container.player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
                return;
            }
        } else if (mode == 5) {
            if (x > 130 && x <= 130 + 16 && y > 16 && y < 16 + 16) {
                new PacketAddBuildingToColony(this.container.base.colony);
            }
        }
        if (isClickedButton)
            container.player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
    }

    private void renderPlanet1(PoseStack poseStack,
                               float radius,
                               ResourceLocation texture,
                               float x,
                               float y,
                               float z,
                               float rotation,
                               float rotationAngle, float rotationAngleX) {
        Minecraft mc = Minecraft.getInstance();

        poseStack.pushPose();

        poseStack.translate(x, y, z);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotationAngle));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(rotationAngleX));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation));

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableCull();
        renderCube(poseStack, radius);

        poseStack.popPose();
    }

    private void renderCube(PoseStack poseStack, float radius) {
        float halfSize = radius / 2.0f;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        Matrix4f matrix = poseStack.last().pose();

        // Front
        buffer.vertex(matrix, -halfSize, -halfSize, -halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, -halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, -halfSize).uv(0.0F, 1.0F).endVertex();

        // Back
        buffer.vertex(matrix, -halfSize, -halfSize, halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        // Left
        buffer.vertex(matrix, -halfSize, -halfSize, halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, -halfSize, -halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, -halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        // Right
        buffer.vertex(matrix, halfSize, -halfSize, halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, -halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        // Top
        buffer.vertex(matrix, -halfSize, halfSize, -halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, halfSize, halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        // Bottom
        buffer.vertex(matrix, -halfSize, -halfSize, -halfSize).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, -halfSize).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, halfSize, -halfSize, halfSize).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, -halfSize, -halfSize, halfSize).uv(0.0F, 1.0F).endVertex();

        tessellator.end();

        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {

    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);
        if (mode == 0) {
            if (star == null) {
                foregroundMainMenu(poseStack, par1, par2);
            }
        } else if (mode == 1) {
            hoverUp = false;
            hoverDown = false;
            hoverBack = false;
            hoverOpen = false;
            int tempMaxValuePage = Math.min(9, maxValuePage - valuePage);
            if (valuePage > 0)
                if (par1 >= 230 && par1 <= 230 + 16 && par2 >= 30 && par2 <= 30 + 19 * 0.65) {
                    hoverUp = true;
                }

            if (tempMaxValuePage > 0 && (maxValuePage - valuePage) > 9)
                if (par1 >= 230 && par1 <= 230 + 16 && par2 >= (tempMaxValuePage - 1) * 19 + this.addPoint && par2 < (tempMaxValuePage - 1) * 19 + 19 * 0.65 + this.addPoint) {
                    hoverDown = true;
                }
            if (par1 >= 9 && par1 <= 9 + 15 && par2 >= 28 && par2 <= 30 + 15) {
                hoverBack = true;
            }
            if (focusedPlanet != null && par1 >= 30 + 175 / 2 - 102 / 2 && par1 <= 30 + 175 / 2 - 102 / 2 + 102 && par2 >= 210 && par2 <= 210 + 22) {
                hoverOpen = true;
            }
            if (focusedPlanet != null) {
                EnumLevels level = EnumLevels.NONE;

                if (focusedPlanet instanceof IPlanet) {
                    level = ((IPlanet) focusedPlanet).getLevels();
                }
                if (focusedPlanet instanceof ISatellite) {
                    level = ((ISatellite) focusedPlanet).getLevels();
                }
                if (focusedPlanet instanceof IAsteroid) {
                    level = ((IAsteroid) focusedPlanet).getLevels();
                }
                if (container.base.level != null && container.base.level.ordinal() >= level.ordinal() && level != EnumLevels.NONE && container.base.level != EnumLevels.NONE) {

                    drawCenteredText(poseStack, Localization.translate("iu.space.open"), 30 + 175 / 2, 210 + 5, 0, 1, 0xFFFFFFFF);
                }
            }
        } else if (mode == 2) {
            if (defaultResearchGuis.isEmpty()) {
                hoverBack = false;
                hoverResource = false;
                hoverColonies = false;
                hoverExpedition = false;

                if (par1 >= 9 && par1 <= 9 + 15 && par2 >= 28 && par2 <= 30 + 15) {
                    hoverBack = true;
                }
                if (par1 >= 30 && par1 <= 30 + 103 * 0.63f && par2 >= 115 && par2 <= 115 + 20 * 0.63f) {
                    hoverResource = true;
                }
                Data data = this.container.base.dataMap.get(focusedPlanet);
                if (data.getPercent() >= 100 && par1 >= 30 && par1 <= 30 + 103 * 0.63f && par2 >= 155 && par2 <= 155 + 20 * 0.63f) {
                    hoverColonies = true;
                }
                if (par1 >= 30 && par1 <= 30 + 103 * 0.63f && par2 >= 135 && par2 <= 135 + 20 * 0.63f) {
                    hoverExpedition = true;
                }
            }
            for (int i = 0; i < defaultResearchGuis.size(); i++) {
                ScreenDefaultResearchTable defaultResearchTable = defaultResearchGuis.get(i);
                defaultResearchTable.drawForegroundLayer(poseStack, par1, par2);
            }
            if (defaultResearchGuis.isEmpty()) {
                Result result = minimumLimit;
                new TooltipWidget(this, 34, 173, 16, 16).withTooltip(Localization.translate("iu.space.fuel_level") + " " + result.allocations.fuelLevel).drawForeground(poseStack, par1, par2);

                new TooltipWidget(this, 34 + 20, 175, 16, 16).withTooltip(Localization.translate("iu.space.rocket_level") + " " + result.allocations.rocketLevel).drawForeground(poseStack, par1, par2);
                new TooltipWidget(this, 34 + 40, 175, 16, 16).withTooltip(Localization.translate("iu.space.fuel_used") + " " + result.allocations.fuelUsed + " mb").drawForeground(poseStack, par1, par2);
                new TooltipWidget(this, 34 + 40, 175 + 16, 16, 16).withTooltip(Localization.translate("iu.space.time_remaining") + " " + (new Timer(result.allocations.remaining).getDisplay())).drawForeground(poseStack, par1, par2);


                int temperature = focusedPlanet.getTemperature();
                if (temperature > 150) {
                    int count = (int) Math.ceil((temperature - 150) / 350D);
                    new TooltipWidget(this, 34, 175 + 16, 16, 16).withTooltip(Localization.translate("iu.space.heat_module") + " " + (count)).drawForeground(poseStack, par1, par2);

                } else if (temperature < -125) {
                    int count = (int) Math.ceil((Math.abs(temperature) - 125) / 37D);
                    new TooltipWidget(this, 34, 175 + 16, 16, 16).withTooltip(Localization.translate("iu.space.cold_module") + " " + (count)).drawForeground(poseStack, par1, par2);
                }

                if ((focusedPlanet instanceof IPlanet p && p.getPressure()) || (focusedPlanet instanceof ISatellite s && s.getPressure())) {
                    new TooltipWidget(this, 34 + 20, 175 + 16, 16, 16).withTooltip(Localization.translate("iu.space.pressure_module") + " ").drawForeground(poseStack, par1, par2);

                }
            }

        }
    }

    private void foregroundMainMenu(PoseStack poseStack, int par1, int par2) {
        if (systems != null) {
            IStar star = systems.get(systemId).getStarList().get(0);
            String systemName = systems.get(systemId).getName();
            String starName = star.getName();
            systemName = systemName.substring(0, 1).toUpperCase() + systemName
                    .substring(1)
                    .toLowerCase()
                    .replace("system", "");
            starName = Localization.translate("iu.body." + starName);

            String result = Localization.translate("iu.space_solar_system") +
                    " " + systemName + "\n" + Localization.translate("iu.space_solar") + " " + starName + "\n" + Localization.translate(
                    "iu.space_solar_planets") + " " + star
                    .getPlanetList()
                    .size() + "\n" + Localization.translate("iu.space_solar_asteroid") + " " + (star
                    .getAsteroidList()
                    .isEmpty() ? Localization.translate("iu.space_no") : Localization.translate("iu.space_yes"));
            new TooltipWidget(this, imageWidth / 2 - 80 + 30, 30 + 30, 90, 90).withTooltip(
                    result
            ).drawForeground(poseStack, par1, par2);
        }
        hoverNextStar = false;
        hoverBackStar = false;
        if (par1 >= imageWidth / 2 - 110 + 30 && par1 <= imageWidth / 2 - 110 + 32 + 30 && par2 >= 75 - 20 + 75 + 30 && par2 <= 75 - 20 + 32 + 75 + 30) {
            int tempid = systemId - 1;
            if (tempid < 0)
                tempid = systems.size() - 1;
            IStar star = systems.get(tempid).getStarList().get(0);
            String systemName = systems.get(tempid).getName();
            String starName = star.getName();
            systemName = systemName.substring(0, 1).toUpperCase() + systemName
                    .substring(1)
                    .toLowerCase()
                    .replace("system", "");
            starName = Localization.translate("iu.body." + starName);

            String result = Localization.translate("iu.space_solar_system") +
                    " " + systemName + "\n" + Localization.translate("iu.space_solar") + " " + starName + "\n" + Localization.translate(
                    "iu.space_solar_planets") + " " + star
                    .getPlanetList()
                    .size() + "\n" + Localization.translate("iu.space_solar_asteroid") + " " + (star
                    .getAsteroidList()
                    .isEmpty() ? Localization.translate("iu.space_no") : Localization.translate("iu.space_yes"));
            new TooltipWidget(this, imageWidth / 2 - 110 + 30, 75 - 20 + 75 + 30, 32, 32).withTooltip(
                    result
            ).drawForeground(poseStack, par1, par2);
            hoverBackStar = true;
        }
        if (par1 >= imageWidth / 2 + 20 + 30 && par1 <= imageWidth / 2 + 20 + 32 + 30 && par2 >= 75 + 30 - 20 + 75 && par2 <= 75 + 30 - 20 + 32 + 75) {
            IStar star = systems.get((systemId + 1) % systems.size()).getStarList().get(0);
            String systemName = systems.get((systemId + 1) % systems.size()).getName();
            String starName = star.getName();
            systemName = systemName.substring(0, 1).toUpperCase() + systemName
                    .substring(1)
                    .toLowerCase()
                    .replace("system", "");
            starName = Localization.translate("iu.body." + starName);

            String result = Localization.translate("iu.space_solar_system") +
                    " " + systemName + "\n" + Localization.translate("iu.space_solar") + " " + starName + "\n" + Localization.translate(
                    "iu.space_solar_planets") + " " + star
                    .getPlanetList()
                    .size() + "\n" + Localization.translate("iu.space_solar_asteroid") + " " + (star
                    .getAsteroidList()
                    .isEmpty() ? Localization.translate("iu.space_no") : Localization.translate("iu.space_yes"));
            new TooltipWidget(this, imageWidth / 2 + 20 + 30, 75 - 20 + 75 + 30, 32, 32).withTooltip(
                    result
            ).drawForeground(poseStack, par1, par2);
            hoverNextStar = true;
        }
    }

    public String getInformationFromBody(IBody body, double data) {
        if (body instanceof IPlanet) {
            IPlanet focusedPlanet = (IPlanet) body;

            return
                    Localization.translate("iu.space_size") + " " + (ModUtils.getString(focusedPlanet.getSize() * 12756D / SpaceInit.earth.getSize())) + "\n" +
                            Localization.translate("iu.space_has_pressure") + " " + ((!focusedPlanet.getPressure() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes"))) + "\n" +
                            Localization.translate("iu.space_distance") + " " + (ModUtils.getString(focusedPlanet.getDistance() * 149200000D / SpaceInit.earth.getDistance())) + "\n" +
                            Localization.translate("iu.space_orbit_time") + " " + (ModUtils.getString(focusedPlanet.getOrbitPeriod() * 365 / SpaceInit.earth.getOrbitPeriod())) + "\n" +
                            Localization.translate("iu.space_temperature") + " " + (focusedPlanet.getTemperature() + "C°") + "\n" +
                            Localization.translate("iu.space_has_oxygen") + " " + ((!focusedPlanet.hasOxygen() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes"))) + "\n" +
                            Localization.translate("iu.space_colonies") + " " + ((!focusedPlanet.canHaveColonies() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes"))) + "\n" +
                            Localization.translate("iu.space_research") + " " + ModUtils.getString(data) + "%";
        } else if (body instanceof ISatellite) {
            ISatellite focusedPlanet = (ISatellite) body;


            return
                    Localization.translate("iu.space_size") + " " + (ModUtils.getString(focusedPlanet.getSize() * 3474D / SpaceInit.moon.getSize())) + "\n" +
                            Localization.translate("iu.space_has_pressure") + " " + ((!focusedPlanet.getPressure() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes"))) + "\n" +
                            Localization.translate("iu.space_distance_from_planet") + " " + (ModUtils.getString(focusedPlanet.getDistance() * 384400 / SpaceInit.moon.getDistance())) + "\n" +
                            Localization.translate("iu.space_orbit_time") + " " + (ModUtils.getString(focusedPlanet.getOrbitPeriod() * 27 / SpaceInit.moon.getOrbitPeriod())) + "\n" +
                            Localization.translate("iu.space_temperature") + " " + (focusedPlanet.getTemperature() + "C°") + "\n" +
                            Localization.translate("iu.space_has_oxygen") + " " + ((!focusedPlanet.hasOxygen() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes"))) + "\n" +
                            Localization.translate("iu.space_colonies") + " " + ((!focusedPlanet.canHaveColonies() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes"))) + "\n" +
                            Localization.translate("iu.space_research") + " " + ModUtils.getString(data) + "%";
        } else if (body instanceof IAsteroid) {
            IAsteroid focusedPlanet = (IAsteroid) body;
            return Localization.translate("iu.space_size") + " " + (ModUtils.getString(focusedPlanet.getSize() * 3474D / SpaceInit.moon.getSize())) + "\n" +
                    Localization.translate("iu.space_minimum_distance") + " " + (ModUtils.getString(focusedPlanet.getMinDistance() * 149200000D / SpaceInit.earth.getDistance())) + "\n" +
                    Localization.translate("iu.space_maximum_distance") + " " + (ModUtils.getString(focusedPlanet.getMinDistance() * 149200000D / SpaceInit.earth.getDistance())) + "\n" +
                    Localization.translate("iu.space_temperature") + " " + (focusedPlanet.getTemperature() + "C°") + "\n" +
                    Localization.translate("iu.space_has_oxygen") + " " + ((Localization.translate("iu.space_no"))) + "\n" +
                    Localization.translate("iu.space_colonies") + " " + ((!focusedPlanet.canHaveColonies() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                    ".space_yes"))) + "\n" +
                    Localization.translate("iu.space_research") + " " + ModUtils.getString(data) + "%";

        }
        return "";
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiresearch_table.png");
    }

    private Result computeUncoveredSeconds(List<FuelAllocation> allocations,
                                           Map<Integer, Double> fuelEfficiency,
                                           int fuelPerSecond,
                                           int totalSeconds) {

        for (FuelAllocation fa : allocations) {
            double seconds = 0;
            double fuelCoef = fuelEfficiency.get(fa.fuelLevel);
            double rocketCoef = 1.0 + fa.upgrades * 0.125;
            seconds += (fa.fuelUsed / (double) fuelPerSecond) * fuelCoef * rocketCoef;
            if (totalSeconds - seconds <= 0) {
                fa.remaining = (fa.fuelUsed / fuelPerSecond);
                return new Result(fa, fa.upgrades);
            }
        }
        return new Result(null, -1);
    }

    private List<FuelAllocation> allocateFuel(
            int totalSeconds,
            Map<Integer, Double> fuelEfficiency,
            Map<Integer, Integer> tankCapacities,
            Map<Integer, Integer> rocketUpgrades,
            int fuelPerSecond, int planetTier
    ) {
        List<FuelAllocation> result = new ArrayList<>();


        List<Integer> rocketLevels = new ArrayList<>(tankCapacities.keySet());
        rocketLevels.sort(Comparator.reverseOrder());

        List<Map.Entry<Integer, Double>> sortedFuel = new ArrayList<>(fuelEfficiency.entrySet());
        sortedFuel.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (int rocketLevel = 1; rocketLevel < 7; rocketLevel++) {

            int capacity = tankCapacities.getOrDefault(rocketLevel, 0);
            int upgrades = rocketUpgrades.getOrDefault(rocketLevel, 0);
            double rocketMultiplier = 1.0 + upgrades * 0.125;

            for (Map.Entry<Integer, Double> entry : sortedFuel) {
                double remaining = totalSeconds;
                int fuelLevel = entry.getKey();
                int requiredLevel = fuelPlanetRequirements.getOrDefault(fuelLevel, Integer.MAX_VALUE);
                if (planetTier < requiredLevel)
                    continue;
                double fuelMultiplier = entry.getValue();

                if (fuelLevel > rocketLevel) continue;

                double totalMultiplier = fuelMultiplier * rocketMultiplier;
                double maxSeconds = (capacity / (double) fuelPerSecond) * totalMultiplier;

                if (maxSeconds <= 0) continue;
                if (remaining > maxSeconds) continue;
                double secondsToCover = remaining;
                int fuelNeeded = (int) Math.ceil((secondsToCover / totalMultiplier) * fuelPerSecond);
                fuelNeeded = Math.min(fuelNeeded, capacity);
                if (fuelNeeded * 2 <= capacity) {
                    result.add(new FuelAllocation(rocketLevel, fuelLevel, fuelNeeded, upgrades, remaining));
                    remaining -= (fuelNeeded / (double) fuelPerSecond) * totalMultiplier;
                    if (remaining <= 0)
                        break;
                }

            }


        }

        return result;
    }

    public Result findOptimalUpgradeDistribution(
            int totalSeconds,
            int planetLevel
    ) {

        Map<Integer, Integer> usableTanks = new HashMap<>();
        for (var entry : rocketFuel.entrySet()) {
            int rocketLevel = entry.getKey();
            int requiredPlanet = rocketPlanetRequirements.getOrDefault(rocketLevel, Integer.MAX_VALUE);
            if (planetLevel >= requiredPlanet) {
                usableTanks.put(rocketLevel, entry.getValue());
            }
        }

        List<Result> results = new ArrayList<>();
        for (int upgrades = 0; upgrades <= 4; upgrades++) {
            Map<Integer, Integer> upgradeMap = new HashMap<>();
            if (upgrades > 0 && planetLevel < 4)
                continue;
            for (int level : usableTanks.keySet()) {
                upgradeMap.put(level, upgrades);
            }

            Map<Integer, Integer> capacityCopy = new HashMap<>(usableTanks);

            List<FuelAllocation> allocations = allocateFuel(
                    totalSeconds, rocketFuelCoef, capacityCopy, upgradeMap, 2, planetLevel
            );
            Result result = computeUncoveredSeconds(allocations, rocketFuelCoef, 2, totalSeconds);
            if (result.allocations != null)
                results.add(result);
        }

        return results.isEmpty() ? new Result(null, -1) : results.get(0);
    }
}

class Result {
    FuelAllocation allocations;
    int totalUpgrades;

    Result(FuelAllocation allocations, int totalUpgrades) {
        this.allocations = allocations;
        this.totalUpgrades = totalUpgrades;
    }


}

class FuelAllocation {
    int remaining;
    int rocketLevel;
    int fuelLevel;
    int fuelUsed;
    int upgrades;

    FuelAllocation(int rocketLevel, int fuelLevel, int fuelUsed, int upgrades, double remaining) {
        this.rocketLevel = rocketLevel;
        this.fuelLevel = fuelLevel;
        this.fuelUsed = fuelUsed;
        this.upgrades = upgrades;
        this.remaining = (int) remaining;
    }

    @Override
    public String toString() {
        return "RocketLevel " + rocketLevel + " (+" + upgrades + " upgrades)"
                + " -> FuelLevel " + fuelLevel + ": " + fuelUsed + " units" + " seconds: " + remaining;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public int getFuelUsed() {
        return fuelUsed;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getRocketLevel() {
        return rocketLevel;
    }

    public int getUpgrades() {
        return upgrades;
    }
}