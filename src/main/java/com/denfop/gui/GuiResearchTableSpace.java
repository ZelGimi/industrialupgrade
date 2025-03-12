package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiVerticalSliderList;
import com.denfop.api.gui.ImageResearchTableInterface;
import com.denfop.api.gui.ImageScreen;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.api.gui.MouseButton;
import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.EnumRing;
import com.denfop.api.space.IAsteroid;
import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.ISatellite;
import com.denfop.api.space.IStar;
import com.denfop.api.space.ISystem;
import com.denfop.api.space.MiniAsteroid;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.rovers.enums.EnumRoversLevel;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerResearchTableSpace;
import com.denfop.network.packet.PacketAddBuildingToColony;
import com.denfop.network.packet.PacketChangeSpaceOperation;
import com.denfop.network.packet.PacketCreateAutoSends;
import com.denfop.network.packet.PacketCreateColony;
import com.denfop.network.packet.PacketDeleteColony;
import com.denfop.network.packet.PacketReturnRoversToPlanet;
import com.denfop.network.packet.PacketSendResourceToEarth;
import com.denfop.network.packet.PacketSendRoversToPlanet;
import com.denfop.network.packet.PacketUpdateBody;
import com.denfop.tiles.bee.EnumProblem;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.denfop.api.gui.GuiElement.bindBlockTexture;
import static com.denfop.api.gui.GuiElement.getBlockTextureMap;
import static com.denfop.events.client.SolarSystemRenderer.ASTEROID_TEXTURE;

public class GuiResearchTableSpace extends GuiIU<ContainerResearchTableSpace> implements GuiPageButtonList.GuiResponder,
        GuiVerticalSliderList.FormatHelper {

    private GuiVerticalSliderList slider;
    private GuiVerticalSliderList slider1;
    IAsteroid asteroid;
    private boolean starsGenerated = false;
    private GuiVerticalSliderList slider2;
    private GuiVerticalSliderList slider3;
    private List<ItemStack> itemList;
    private List<FluidStack> fluidList;

    public GuiResearchTableSpace(ContainerResearchTableSpace guiContainer) {
        super(guiContainer, EnumTypeStyle.PERFECT);
        ySize = 250;
        xSize = 240;
        this.inventory.addY(84);
        this.elements.add(new ImageResearchTableInterface(this, 0, 0, xSize, ySize));
        this.elements.add(new CustomButton(this, 167, 20 + 10 + 10, 68, 18, guiContainer.base, 0, "") {
            @Override
            public boolean visible() {
                IFakeBody fakeBody = guiContainer.base.fakeBody;
                boolean can = true;
                if (planet != null) {
                    can = planet.getLevels() != EnumLevels.NONE;
                }
                if (satellite != null) {
                    can = satellite.getLevels() != EnumLevels.NONE;
                }
                if (asteroid != null) {
                    can = asteroid.getLevels() != EnumLevels.NONE;
                }
                return (mode == 2 || mode == 3 || mode == 4) && fakeBody == null && can;
            }

            @Override
            public String getText() {
                return Localization.translate("iu.space_send");
            }

            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    new PacketSendRoversToPlanet(
                            guiContainer.base,
                            container.player,
                            satellite != null ? satellite : planet != null ?
                                    planet :
                                    asteroid != null ? asteroid : null
                    );
                }
                return true;
            }
        });

        this.elements.add(new CustomButton(this, 167, 20 + 10 - 20 + 10, 68, 18, guiContainer.base, 0, "") {
            @Override
            public boolean visible() {
                IFakeBody fakeBody = guiContainer.base.fakeBody;
                return (mode == 2 || mode == 3 || mode == 4) && fakeBody != null;
            }

            @Override
            public String getText() {
                if (planet != null) {
                    return Localization.translate("iu.space.auto_" + guiContainer.base.fakeBody.getSpaceOperation().getAuto());
                }
                if (satellite != null) {
                    return Localization.translate("iu.space.auto_" + guiContainer.base.fakeBody
                            .getSpaceOperation()
                            .getAuto());

                }
                if (asteroid != null) {
                    return Localization.translate("iu.space.auto_" + guiContainer.base.fakeBody
                            .getSpaceOperation()
                            .getAuto());
                }
                return Localization.translate("iu.space.auto_" + false);
            }

            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    new PacketChangeSpaceOperation(
                            container.player,
                            satellite != null ? satellite : planet != null ?
                                    planet :
                                    asteroid != null ? asteroid : null
                    );
                }
                return true;
            }
        });
        this.elements.add(new CustomButton(this, 167, 20 + 30 + 10, 68, 18, guiContainer.base, 0, "") {
            @Override
            public boolean visible() {
                boolean can = true;
                if (planet != null) {

                    can = planet.getLevels() != EnumLevels.NONE && planet.canHaveColonies();
                }
                if (satellite != null) {

                    can = satellite.getLevels() != EnumLevels.NONE && satellite.canHaveColonies();
                }
                if (asteroid != null) {

                    can = asteroid.getLevels() != EnumLevels.NONE && asteroid.canHaveColonies();
                }
                Data data = container.base.dataMap.get(container.base.body);
                return (mode == 2 || mode == 3 || mode == 4) && can && container.base.colony == null && data.getPercent() >= 100;
            }

            @Override
            public String getText() {
                return Localization.translate("iu.space.createcolony");
            }

            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    if (container.player.getUniqueID().equals(container.base.getPlayer())) {
                        new PacketCreateColony(
                                container.player,
                                container.base.body
                        );
                    }
                }
                return true;
            }
        });
        this.elements.add(new CustomButton(this,  167, 20 + 30 + 10, 68, 18, guiContainer.base, 0, "") {
            @Override
            public boolean visible() {
                boolean can = true;
                if (planet != null) {

                    can = planet.getLevels() != EnumLevels.NONE && planet.canHaveColonies();
                }
                if (satellite != null) {

                    can = satellite.getLevels() != EnumLevels.NONE && satellite.canHaveColonies();
                }
                if (asteroid != null) {

                    can = asteroid.getLevels() != EnumLevels.NONE && asteroid.canHaveColonies();
                }
                return (mode == 2 || mode == 3 || mode == 4) && can && container.base.colony != null;
            }

            @Override
            public String getText() {
                return Localization.translate("iu.space.open_colony");
            }

            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    mode = 5;
                }
                return true;
            }
        });
        this.elements.add(new CustomButton(this, 167, 20 + 10 + 10, 68, 18, guiContainer.base, 0, "") {
            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    new PacketReturnRoversToPlanet(guiContainer.base, container.player, satellite != null ? satellite :
                            planet != null ?
                                    planet :
                                    asteroid != null ? asteroid : null);
                }
                return true;
            }

            @Override
            public String getText() {
                return Localization.translate("iu.space.return");
            }

            @Override
            public boolean visible() {
                IFakeBody fakeBody = guiContainer.base.fakeBody;
                return (mode == 2 || mode == 3 || mode == 4) && fakeBody != null;
            }
        });
        this.elements.add(new CustomButton(this, 172, 20 + 30 + 115, 65, 18, guiContainer.base, 0, "") {
            @Override
            public boolean visible() {
                return (mode == 5) && container.base.colony != null;
            }

            @Override
            public String getText() {
                return Localization.translate("iu.space.send_item");
            }

            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    if (container.player.getUniqueID().equals(container.base.getPlayer())) {
                        new PacketSendResourceToEarth(
                                container.player,
                                container.base.body
                        );
                    }
                }
                return true;
            }
        });
        this.elements.add(new CustomButton(this, 172, 20 + 30 + 20 + 115, 65, 18, guiContainer.base, 0, "") {
            @Override
            public boolean visible() {
                return (mode == 5) && container.base.colony != null;
            }

            @Override
            public String getText() {
                return Localization.translate("iu.space.auto_" + container.base.colony.isAuto());
            }

            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    if (container.player.getUniqueID().equals(container.base.getPlayer())) {
                        new PacketCreateAutoSends(
                                container.player,
                                container.base.body
                        );
                    }
                }
                return true;
            }
        });
        this.elements.add(new CustomButton(this, 172, 20 + 30 + 20 + 20 + 115, 65, 18, guiContainer.base, 0, "") {
            @Override
            public boolean visible() {
                return (mode == 5) && container.base.colony != null;
            }

            @Override
            public String getText() {
                return Localization.translate("iu.space.deletecolony");
            }

            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    if (container.player.getUniqueID().equals(container.base.getPlayer())) {
                        new PacketDeleteColony(
                                container.player,
                                container.base.body
                        );
                        if (satellite != null) {
                            mode = 3;
                            prevText = 0;
                            textIndex = 0;
                            focusedPlanet = null;
                            new PacketUpdateBody(container.base, satellite);
                        } else if (planet != null) {
                            mode = 2;
                            prevText = 0;
                            textIndex = 0;
                            focusedPlanet = null;
                            new PacketUpdateBody(container.base, planet);
                        } else if (asteroid != null) {
                            mode = 4;
                            prevText = 0;
                            textIndex = 0;
                            focusedPlanet = null;
                            new PacketUpdateBody(container.base, asteroid);
                        }
                    }
                }
                return true;
            }
        });
        this.componentList.add(new GuiComponent(this, 3, 3, EnumTypeComponent.PREV,
                new Component<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public void ClickEvent() {
                        if (mode == 1) {
                            mode = 0;
                            star = null;
                            focusedPlanet = null;
                            prevText = 0;
                            textIndex = 0;
                        } else if (mode == 2) {
                            mode = 1;
                            focusedPlanet = planet;
                            planet = null;
                            prevText = 0;
                            textIndex = 0;
                            new PacketUpdateBody(container.base, null);
                        } else if (mode == 3) {
                            mode = 2;
                            satellite = null;
                            prevText = 0;
                            textIndex = 0;
                            new PacketUpdateBody(container.base, planet);
                        } else if (mode == 4) {
                            mode = 1;
                            focusedPlanet = null;
                            planet = null;
                            asteroid = null;
                            prevText = 0;
                            textIndex = 0;
                            new PacketUpdateBody(container.base, null);
                        } else if (mode == 5) {
                            if (satellite != null) {
                                mode = 3;
                                prevText = 0;
                                textIndex = 0;
                                focusedPlanet = null;
                                new PacketUpdateBody(container.base, satellite);
                            } else if (planet != null) {
                                mode = 2;
                                prevText = 0;
                                textIndex = 0;
                                focusedPlanet = null;
                                new PacketUpdateBody(container.base, planet);
                            } else if (asteroid != null) {
                                mode = 4;
                                prevText = 0;
                                textIndex = 0;
                                focusedPlanet = null;
                                new PacketUpdateBody(container.base, asteroid);
                            }


                        }
                    }

                    @Override
                    public String getText() {
                        return Localization.translate("iu.prev");
                    }
                })
        ) {
            @Override
            public boolean visible() {
                return mode >= 1;
            }
        });
    }

    int mode = 0;
    int value1 = 0;
    int value2 = 0;
    int value3 = 0;
    int value4 = 0;
    IStar star;
    IPlanet planet;
    ISatellite satellite;

    IBody focusedPlanet = null;

    public void initGui() {
        super.initGui();


        slider = new GuiVerticalSliderList(this, 2, (this.width - this.xSize) / 2 + 227,
                (this.height - this.ySize) / 2 + 12,
                "",
                0, 0, 0,
                this, 210
        );
        slider1 = new GuiVerticalSliderList(this, 3, (this.width - this.xSize) / 2 + 227,
                (this.height - this.ySize) / 2 + 12 + 155,
                "",
                0, 9, 0,
                this, 70
        );
        slider2 = new GuiVerticalSliderList(this, 4, (this.width - this.xSize) / 2 + 150 + 18 * 4 + 4 + 4,
                (this.height - this.ySize) / 2 + 16 + 2,
                "",
                0, 9, 0,
                this, 18 * 4 + 4 + 2
        );
        slider3 = new GuiVerticalSliderList(this, 5, (this.width - this.xSize) / 2 + 150 + 18 * 4 + 4 + 4,
                (this.height - this.ySize) / 2 + 22 + 18 * 4 + 4 + 2,
                "",
                0, 9, 0,
                this, 18 * 3 + 4 + 2
        );
        this.slider.visible = false;
        this.slider1.visible = false;
        this.slider2.visible = false;
        this.slider3.visible = false;
        this.buttonList.add(slider);
        this.buttonList.add(slider1);
        this.buttonList.add(slider2);
        this.buttonList.add(slider3);
    }

    @Override
    public String getText(final int var1, final String var2, final float var3) {
        return "";
    }

    @Override
    public void setEntryValue(final int i, final boolean b) {

    }

    @Override
    public void setEntryValue(final int i, final float v) {
        if (i == 2) {
            value1 = (int) v;
        }
        if (i == 3) {
            value2 = (int) v;
        }
        if (i == 4) {
            value3 = (int) v;
        }
        if (i == 5) {
            value4 = (int) v;
        }
    }

    @Override
    public void setEntryValue(final int i, final String s) {

    }

    @Override
    public void updateScreen() {
        if (mode == 5 && container.base.colony != null) {
            this.itemList = container.base.colony.getStacksFromStorage();
            this.fluidList = container.base.colony.getFluidsFromStorage();
        } else {
            itemList = Collections.emptyList();
            fluidList = Collections.emptyList();
        }
        super.updateScreen();

        slider.visible =
                star != null && this.star.getPlanetList().size() + this.star
                        .getAsteroidList()
                        .size() > 7 && planet == null && asteroid == null;
        if (star != null) {
            slider.setMax(this.star.getPlanetList().size() + this.star.getAsteroidList().size() - 7);
        }
        slider1.visible = planet != null && this.planet.getSatelliteList().size() > 2 && satellite == null;
        if (planet != null) {
            slider1.setMax(this.planet.getSatelliteList().size() - 2);
        }
        if (this.container.base.colony != null && mode == 5) {
            slider2.visible = itemList.size() / 16 > 0;
            slider3.visible = fluidList.size() / 12 > 0;
            slider2.setMax(itemList.size() / 16);
            slider3.setMax(fluidList.size() / 12);
        } else {
            slider2.visible = false;
            slider3.visible = false;
            value3 = 0;
            value4 = 0;
        }
    }

    private void renderRings(IPlanet planet) {
        if (planet.getRing() == null) {
            return;
        }
        final boolean isSaturn = planet.getRing() == EnumRing.HORIZONTAL;
        GlStateManager.pushMatrix();
        double time = container.base.getWorld().getWorldTime();
        double angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
        float planetX = (float) (planet.getDistance() * Math.cos(angle));
        float planetY = (float) (planet.getDistance() * Math.sin(angle));
        final double size = planet.getSize() * 2;
        GlStateManager.translate(planetX, planetY, 0);
        if (!isSaturn) {
            GlStateManager.rotate(270, 0.0F, 0.0F, 1.0F);
        }
        GlStateManager.scale((0.25 / 128D) * size, (0.25 / 128D) * size / 32, 1);

        GlStateManager.color(0, 0, 1, 1);
        mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
        drawTexturedModalRect(-128, -128, 0, 0, 256, 256);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }


    public void renderStars(int x, int y, int width, int height, int starCount) {
        if (!starsGenerated) {
            generateStars(x, y, width, height, starCount);
            starsGenerated = true;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION);
        for (float[] star : cachedStars) {
            buffer.pos(star[0], star[1], 0).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void renderStars1(int x, int y, int width, int height, int starCount) {
        if (!starsGenerated1) {
            generateStars1(x, y, width, height, starCount);
            starsGenerated1 = true;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION);
        for (float[] star : cachedStars1) {
            buffer.pos(star[0], star[1], 0).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void generateStars(int x, int y, int width, int height, int starCount) {
        Random random = new Random();
        cachedStars.clear();

        for (int i = 0; i < starCount; i++) {
            float starX = x + random.nextFloat() * width;
            float starY = y + random.nextFloat() * height;
            cachedStars.add(new float[]{starX, starY});
        }
    }

    boolean starsGenerated1 = false;

    private void generateStars1(int x, int y, int width, int height, int starCount) {
        Random random = new Random();
        cachedStars1.clear();

        for (int i = 0; i < starCount; i++) {
            float starX = x + random.nextFloat() * width;
            float starY = y + random.nextFloat() * height;
            cachedStars1.add(new float[]{starX, starY});
        }
    }

    private void drawLine(BufferBuilder buffer, double x1, double y1, double x2, double y2) {
        buffer.pos(x1, y1, 0).color(255, 255, 255, 255).endVertex();
        buffer.pos(x2, y2, 0).color(255, 255, 255, 255).endVertex();
    }

    private final List<float[]> cachedStars = new ArrayList<>();
    private final List<float[]> cachedStars1 = new ArrayList<>();

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if (mode == 0) {
            List<ISystem> systems = SpaceNet.instance.getSystem();
            systems = systems.stream().filter(iSystem -> !iSystem.getStarList().isEmpty()).collect(Collectors.toList());
            for (int i = 0; i < systems.size(); i++) {
                new ImageResearchTableInterface(this, 10, 6 + 32 * i, 30, 30).drawBackground(this.guiLeft, guiTop);
            }

            for (int i = 0; i < systems.size(); i++) {
                IStar star = systems.get(i).getStarList().get(0);
                renderPlanet(16f, star.getLocation(), (float) this.guiLeft + 25, (float) this.guiTop + 20 + i * 32, 0,
                        (float) star.getRotation(this.container.base.getWorld().getWorldTime()),
                        star.getRotationAngle()
                );
            }


        } else if (mode == 5) {
            new ImageScreen(this, 6, 16, 120, 145).drawBackground(this.guiLeft, this.guiTop);
            new ImageScreen(this, 130, 16, 16, 16).drawBackground(this.guiLeft, this.guiTop);
            new ImageScreen(this, 150, 16, 18 * 4 + 5, 18 * 4 + 4).drawBackground(this.guiLeft, this.guiTop);
            new ImageScreen(this, 150, 22 + 18 * 4 + 4, 18 * 4 + 5, 18 * 3 + 4).drawBackground(this.guiLeft, this.guiTop);
            for (int i = value3 * 4, j = 0; i < Math.min(this.itemList.size(), 16 + value3 * 4); j++, i++) {
                final int finalI = i;
                new ItemStackImage(
                        this,
                        150 + 4 + (j % 4) * 18,
                        20 + (j / 4) * 18,
                        () -> this.itemList.get(finalI)
                ).drawBackground(this.guiLeft, guiTop);
            }
            for (int i = value4 * 4, j = 0; i < Math.min(this.fluidList.size(), 12 + value4 * 4); j++, i++) {
                new FluidItem(this, 150 + 3 + (j % 4) * 18, 25 + 18 * 4 + 4 + (j / 4) * 18, this.fluidList.get(i)).drawBackground(
                        this.guiLeft,
                        guiTop
                );
            }
            renderIconWithProgressBar(this.guiLeft + 129, this.guiTop + 35, 19, 122,
                    0f
            );
            GlStateManager.color(1.0F, 0.2F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
            this.drawTexturedModalRect(guiLeft + 129, guiTop + 35, 2, 2, 19, 122);
            GlStateManager.color(1.0F, 1F, 1.0F, 1.0F);
            this.renderStars1(this.guiLeft + 130, this.guiTop + 44 + 9, 17, 85, 400);

            renderPlanet(15f, SpaceInit.earth.getLocation(), (float) this.guiLeft + 139,
                    (float) this.guiTop + 44
                    , 0,
                    (float) 0,
                    0
            );
            renderPlanet(15f, container.base.colony.getBody().getLocation(), (float) this.guiLeft + 139,
                    (float) this.guiTop + 147
                    , 0,
                    (float) 0,
                    0
            );
            List<Timer> timers = this.container.base.getSends().getTimers();
            if (timers != null && !timers.isEmpty()) {
                renderProgressBarSends(this.guiLeft + 130, this.guiTop + 44 + 9, 0, 85, (float) timers.get(0).getProgressBar());
            }
        } else if (mode == 3) {
            drawInfoBody(satellite);

            if (this.container.base.fakeBody != null && container.base.fakeBody.matched(satellite)) {
                renderFakeBody(this.container.base.fakeBody);
            }
            renderDistance(satellite);
        } else if (mode == 4) {
            drawInfoBody(asteroid);
            renderPlanet(4f, asteroid.getLocation(), (float) this.guiLeft + 20,
                    (float) this.guiTop + 28, 0,
                    (float) asteroid.getRotation(this.container.base.getWorld().getWorldTime() / 4D),
                    0
            );
            renderPlanet(4f, asteroid.getLocation(), (float) this.guiLeft + 35,
                    (float) this.guiTop + 35, 0,
                    (float) asteroid.getRotation(this.container.base.getWorld().getWorldTime() / 4D),
                    0
            );
            renderPlanet(4f, asteroid.getLocation(), (float) this.guiLeft + 50,
                    (float) this.guiTop + 25, 0,
                    (float) asteroid.getRotation(this.container.base.getWorld().getWorldTime() / 4D),
                    0
            );
            renderPlanet(4f, asteroid.getLocation(), (float) this.guiLeft + 31,
                    (float) this.guiTop + 45, 0,
                    (float) asteroid.getRotation(this.container.base.getWorld().getWorldTime() / 4D),
                    0
            );
            renderPlanet(4f, asteroid.getLocation(), (float) this.guiLeft + 43,
                    (float) this.guiTop + 61, 0,
                    (float) asteroid.getRotation(this.container.base.getWorld().getWorldTime() / 4D),
                    0
            );
            if (this.container.base.fakeBody != null && container.base.fakeBody.matched(asteroid)) {
                renderFakeBody(this.container.base.fakeBody);
            }
            renderDistance(asteroid);
        } else if (mode == 2) {
            drawInfoBody(planet);


            for (int i = value2; i < Math.min(planet.getSatelliteList().size(), value2 + 2); i++) {
                new ImageResearchTableInterface(this, 190, 6 + 32 * (i % 2) + 5 * 31, 30, 30).drawBackground(
                        this.guiLeft,
                        guiTop
                );
                GlStateManager.color(1.0F, 0.2F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
                this.drawTexturedModalRect(guiLeft + 192, guiTop + 8 + 32 * (i % 2) + 5 * 31, 2, 2, 26, 26);
                GlStateManager.color(1.0F, 1, 1.0F, 1.0F);
            }
            if (this.container.base.fakeBody != null && container.base.fakeBody.matched(planet)) {
                renderFakeBody(this.container.base.fakeBody);
            }

            for (int i = value2, j = 0; i < Math.min(planet.getSatelliteList().size(), value2 + 2); i++, j++) {
                ISatellite planet1 = planet.getSatelliteList().get(i);
                renderPlanet(16f, planet1.getLocation(), (float) this.guiLeft + 205,
                        (float) this.guiTop + 20 + (j % 2) * 32 + 5 * 31
                        , 0,
                        (float) planet1.getRotation(this.container.base.getWorld().getWorldTime() / 8D),
                        0
                );
                final int progressWidth = 20;
                final int progressHeight = 2;
                final Data data = container.base.dataMap.get(planet1);
                float progress = (float) (data.getPercent() / 100D);
                renderIconWithProgressBar(
                        this.guiLeft + 195,
                        this.guiTop + 32 + (j % 2) * 32 + 5 * 31,
                        progressWidth,
                        progressHeight,
                        progress
                );
            }

            renderDistance(planet);
        } else if (mode == 1) {
            new ImageResearchTableInterface(this, 10, 20, 50, 50).drawBackground(this.guiLeft, guiTop);
            new ImageResearchTableInterface(this, 65, 10, 120, 60).drawBackground(this.guiLeft, guiTop);
            GlStateManager.color(1.0F, 0.5F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
            this.drawTexturedModalRect(guiLeft + 67, guiTop + 12, 2, 2, 116, 56);
            GlStateManager.color(1, 1, 1.0F, 1.0F);
            new ImageResearchTableInterface(this, 7, 76, 162, 90).drawBackground(this.guiLeft, guiTop);
            GlStateManager.color(1.0F, 0.2F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
            this.drawTexturedModalRect(guiLeft + 9, guiTop + 78, 2, 2, 158, 86);
            GlStateManager.color(1, 1, 1.0F, 1.0F);
            enableScissor(this.guiLeft + 11, this.guiTop + 80, 155, 84);
            mc.getTextureManager().bindTexture(star.getLocation());
            GlStateManager.pushMatrix();
            float planetX = 0;
            float planetY = 0;
            if (focusedPlanet != null) {
                offsetX = 0;
                offsetY = 0;
                if (focusedPlanet instanceof IPlanet) {
                    double time = container.base.getWorld().getWorldTime();
                    double angle = 2 * Math.PI * (time * focusedPlanet.getOrbitPeriod()) / 400D;
                    planetX = (float) (focusedPlanet.getDistance() * Math.cos(angle)) * scale * 16;
                    planetY = (float) (focusedPlanet.getDistance() * Math.sin(angle)) * scale * 16;
                } else if (focusedPlanet instanceof ISatellite) {
                    ISatellite satellite1 = (ISatellite) focusedPlanet;

                    double time = container.base.getWorld().getWorldTime();
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
                }
                double centerX = this.guiLeft + 7 + 162 / 2D;
                double centerY = this.guiTop + 80 + 82 / 2D;
                GlStateManager.pushMatrix();
                GlStateManager.glLineWidth(2);
                double planetSize = focusedPlanet.getSize() * 2 * scale * 16;
                if (scale > 2D / (focusedPlanet.getSize() * scale)) {
                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.translate(centerX - planetX + offsetX, centerY - planetY + offsetY, 0);
                    int squareSize = (int) (planetSize * 1.2);
                    GlStateManager.translate(planetX, planetY, 0);
                    GlStateManager.scale(scale, scale, scale);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();

                    buffer.begin(GL11.GL_LINES, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);
                    drawLine(buffer, (-squareSize / 2), -squareSize / 2, -squareSize / 4D, -squareSize / 2);
                    drawLine(buffer, (squareSize / 2), -squareSize / 2, squareSize / 4D, -squareSize / 2);

                    drawLine(buffer, squareSize / 2, -squareSize / 2, squareSize / 2, -squareSize / 4);
                    drawLine(buffer, squareSize / 2, squareSize / 2, squareSize / 2, squareSize / 4);

                    drawLine(buffer, (-squareSize / 2), squareSize / 2, -squareSize / 4D, squareSize / 2);
                    drawLine(buffer, (squareSize / 2), squareSize / 2, squareSize / 4D, squareSize / 2);

                    drawLine(buffer, -squareSize / 2, -squareSize / 2, -squareSize / 2, -squareSize / 4);
                    drawLine(buffer, -squareSize / 2, squareSize / 2, -squareSize / 2, squareSize / 4);
                    tessellator.draw();

                    GlStateManager.popMatrix();
                }
                GlStateManager.translate(centerX - planetX + offsetX, centerY - planetY + offsetY, 0);
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.glLineWidth(2);

            } else {
                GlStateManager.translate(this.guiLeft + 7 + 162 / 2D + offsetX - planetX,
                        this.guiTop + 80 + 82 / 2D + offsetY - planetY, 0
                );
            }

            GlStateManager.scale(scale * 16, scale * 16, 1.0F);

            mc.getTextureManager().bindTexture(star.getLocation());
            final double size = star.getSize();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 0);
            GlStateManager.scale((0.25 / 128D) * size, (0.25 / 128D) * size, 1);
            drawTexturedModalRect(-128, -128, 0, 0, 256, 256);
            GlStateManager.popMatrix();
            for (IPlanet planet : star.getPlanetList()) {
                if (planet != SpaceInit.ceres) {
                    renderOrbit(planet.getDistance());
                }
            }
            for (IPlanet planet : star.getPlanetList()) {
                renderPlanet(planet);
                renderRings(planet);
                planet.getSatelliteList().forEach(satellite -> renderSatellite(planet, satellite));
            }
            star.getAsteroidList().forEach(iAsteroid -> iAsteroid
                    .getMiniAsteroid()
                    .forEach(this::renderAsteroid));
            GlStateManager.popMatrix();
            disableScissor();


            GlStateManager.color(1, 1, 1, 1);
            for (int i = value1; i < Math.min(
                    star.getPlanetList().size() + this.star.getAsteroidList().size(),
                    value1 + 7
            ); i++) {
                new ImageResearchTableInterface(this, 190, 6 + 32 * (i % 7), 30, 30).drawBackground(this.guiLeft, guiTop);
                GlStateManager.color(1.0F, 0.2F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
                this.drawTexturedModalRect(guiLeft + 192, guiTop + 8 + 32 * (i % 7), 2, 2, 26, 26);
                GlStateManager.color(1.0F, 1, 1.0F, 1.0F);
            }
            renderPlanet(32f, star.getLocation(), (float) this.guiLeft + 36, (float) this.guiTop + 45, 0,
                    (float) star.getRotation(this.container.base.getWorld().getWorldTime() / 8D),
                    0
            );

            for (int i = value1, j = 0; i < Math.min(
                    star.getPlanetList().size() + this.star.getAsteroidList().size(),
                    value1 + 7
            ); i++, j++) {
                if (i < star.getPlanetList().size()) {
                    IPlanet planet1 = star.getPlanetList().get(i);
                    renderPlanet(16f, planet1.getLocation(), (float) this.guiLeft + 205,
                            (float) this.guiTop + 20 + (j % 7) * 32
                            , 0,
                            (float) planet1.getRotation(this.container.base.getWorld().getWorldTime() / 8D),
                            0
                    );
                    int progressWidth = 20;
                    int progressHeight = 2;
                    float progress;
                    Data data = container.base.dataMap.get(planet1);
                    progress = (float) (data.getPercent() / 100D);
                    if (planet1 == SpaceInit.earth) {
                        progress = 1F;
                    }
                    renderIconWithProgressBar(this.guiLeft + 195, this.guiTop + 32 + (j % 7) * 32, progressWidth, progressHeight,
                            progress
                    );
                } else {
                    IAsteroid asteroid1 = star.getAsteroidList().get(i - star.getPlanetList().size());
                    renderPlanet(4f, asteroid1.getLocation(), (float) this.guiLeft + 205,
                            (float) this.guiTop + 20 + (j % 7) * 32
                            , 0,
                            (float) asteroid1.getRotation(this.container.base.getWorld().getWorldTime() / 8D),
                            0
                    );
                    int progressWidth = 20;
                    int progressHeight = 2;
                    float progress;
                    Data data = container.base.dataMap.get(asteroid1);
                    progress = (float) (data.getPercent() / 100D);
                    renderIconWithProgressBar(this.guiLeft + 195, this.guiTop + 32 + (j % 7) * 32, progressWidth, progressHeight,
                            progress
                    );
                }
            }
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft + xSize - 10, this.guiTop, 0, 0, 10, 10);
    }

    private void renderFakeBody(IFakeBody fakeBody) {
        if (fakeBody.getTimerTo().canWork()) {
            renderProgressBar(this.guiLeft + 30, this.guiTop + 140, 120, 10,
                    (float) fakeBody.getTimerTo().getProgressBar(),
                    fakeBody.getRover().getItem().getLevel()
            );
        }
        if (fakeBody.getTimerFrom().canWork()) {
            renderProgressBackBar(this.guiLeft + 30, this.guiTop + 140, 120, 10,
                    (float) fakeBody.getTimerFrom().getProgressBar(),
                    fakeBody.getRover().getItem().getLevel()
            );
        }
    }

    private void renderDistance(IBody body) {
        if (body != SpaceInit.earth) {
            renderPlanet(20f, body.getLocation(), (float) this.guiLeft + 159,
                    (float) this.guiTop + 150
                    , 0,
                    (float) 0,
                    0
            );
            renderPlanet(20f, SpaceInit.earth.getLocation(), (float) this.guiLeft + 21,
                    (float) this.guiTop + 150
                    , 0,
                    (float) 0,
                    0
            );
        }
    }

    private void drawInfoBody(IBody planet) {

        new ImageResearchTableInterface(this, 10, 20, 50, 50).drawBackground(this.guiLeft, guiTop);
        GlStateManager.color(1.0F, 0.2F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
        this.drawTexturedModalRect(guiLeft + 12, guiTop + 22, 2, 2, 46, 46);
        GlStateManager.color(1.0F, 1, 1.0F, 1.0F);
        if (!(planet instanceof IAsteroid)) {
            renderPlanet(32f, planet.getLocation(), (float) this.guiLeft + 36,
                    (float) this.guiTop + 45, 0,
                    (float) planet.getRotation(this.container.base.getWorld().getWorldTime() / 4D),
                    0
            );
        }

        int progressWidth = 50;
        int progressHeight = 8;
        float progress;
        Data data = container.base.dataMap.get(planet);
        progress = (float) (data.getPercent() / 100D);
        if (planet == SpaceInit.earth) {
            progress = 1F;
        }
        renderIconWithProgressBar(this.guiLeft + 10, this.guiTop + 20 + 55, progressWidth, progressHeight,
                progress
        );
        new ImageResearchTableInterface(this, 65, 20, 100, 65).drawBackground(this.guiLeft, guiTop);
        GlStateManager.color(1.0F, 0.5F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
        this.drawTexturedModalRect(guiLeft + 67, guiTop + 22, 2, 2, 96, 61);
        GlStateManager.color(1, 1, 1.0F, 1.0F);
        new ImageScreen(this, 10, 22 + 65, 116 + 37, 36).drawBackground(this.guiLeft, this.guiTop);
        if (planet != SpaceInit.earth) {
            renderIconWithProgressBar(this.guiLeft + 8, this.guiTop + 136, 164, 28,
                    0f
            );
            GlStateManager.color(1.0F, 0.2F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common2.png"));
            this.drawTexturedModalRect(guiLeft + 8, guiTop + 136, 2, 2, 164, 28);
            this.renderStars(this.guiLeft + 32, this.guiTop + 140, 114, 20, 400);
            GlStateManager.color(1, 1, 1.0F, 1.0F);
        }
        for (int i = 0; i < planet.getResources().size(); i++) {
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.translate(guiLeft + (int) ((15 + (i % 11) * 13)), guiTop + (int) ((27 + 66 + 13 * (i / 11))), 0);
            GlStateManager.scale(0.75, 0.75, 1);
            IBaseResource baseResource = planet.getResources().get(i);
            if (baseResource.getPercentPanel() > data.getPercent()) {
                GlStateManager.disableTexture2D();
                RenderHelper.disableStandardItemLighting();
            }
            if (baseResource.getItemStack() != null || baseResource.getFluidStack() != null) {
                if (baseResource.getPercentPanel() <= data.getPercent() && baseResource.getItemStack() != null) {
                    itemRender.renderItemAndEffectIntoGUI(baseResource.getItemStack(), 0, 0);
                    String s = String.valueOf(baseResource.getItemStack().getCount());
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    GlStateManager.disableBlend();
                    fontRenderer.drawStringWithShadow(s, 10f, 6f / 0.75f,
                            16777215
                    );
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    GlStateManager.enableBlend();
                } else if (baseResource.getPercentPanel() <= data.getPercent() && baseResource.getFluidStack() != null) {
                    FluidStack fs = baseResource.getFluidStack();
                    int fluidX = 0;
                    int fluidY = 0;
                    int fluidWidth = 16;
                    int fluidHeight = 16;
                    Fluid fluid = fs.getFluid();
                    TextureAtlasSprite sprite = fluid != null
                            ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString())
                            : null;
                    int color = fluid != null ? fluid.getColor(fs) : -1;
                    bindBlockTexture();
                    this.drawSprite(
                            fluidX - guiLeft,
                            fluidY - guiTop,
                            fluidWidth,
                            fluidHeight,
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                } else {
                    itemRender.renderItemAndEffectIntoGUI(IUItem.bronzeBlock, 0, 0);
                }
            }
            if (baseResource.getPercentPanel() >= data.getPercent()) {
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableTexture2D();
            }
            GlStateManager.popMatrix();
        }
        RenderHelper.disableStandardItemLighting();

    }

    private void renderAsteroid(MiniAsteroid miniAsteroid) {
        double time = container.base.getWorld().getWorldTime();
        double angle = 2 * Math.PI * (time * miniAsteroid.getRotationSpeed()) / 800D;
        float planetX = (float) (miniAsteroid.getX() * Math.cos(angle));
        float planetY = (float) (miniAsteroid.getX() * Math.sin(angle));
        final double size = miniAsteroid.getSize() * 2;
        mc.getTextureManager().bindTexture(ASTEROID_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.translate(planetX, planetY, 0);
        GlStateManager.scale((0.25 / 128D) * size, (0.25 / 128D) * size, 1);
        drawTexturedModalRect(-128, -128, 0, 0, 256, 256);
        GlStateManager.popMatrix();
    }

    private float scale = 1.0F;
    private int offsetX = 0, offsetY = 0;
    private boolean isDragging = false;
    private int lastMouseX, lastMouseY;

    private void renderOrbit(double radius) {
        GlStateManager.disableTexture2D();
        GlStateManager.color(0.0F, 0.0F, 1.0F, 0.4F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        int max = 32;
        if (radius > 3) {
            max = (int) (max * radius / 3D);
        }
        for (int i = 0; i < max; i++) {
            float theta = (float) (2 * Math.PI * i / max);
            buffer.pos(radius * Math.cos(theta), radius * Math.sin(theta), 0).endVertex();
        }

        tessellator.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth - this.guiLeft;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1 - this.guiTop;
        if (mode == 1 && mouseX >= 7 && mouseY >= 76 && mouseX <= 7 + 162 && mouseY <= 76 + 90) {
            int dWheel = Mouse.getEventDWheel();
            if (dWheel != 0) {
                scale += dWheel > 0 ? 0.1F : -0.1F;
                scale = MathHelper.clamp(scale, 0.2F, 10.0F);
            }
            if (Mouse.isButtonDown(0)) {
                if (!isDragging) {
                    isDragging = true;
                } else {
                    int dx = Mouse.getEventX() - lastMouseX;
                    int dy = Mouse.getEventY() - lastMouseY;
                    offsetX += dx / 8;
                    offsetY -= dy / 8;

                }
                lastMouseX = Mouse.getEventX();
                lastMouseY = Mouse.getEventY();

            } else {
                isDragging = false;
            }

        }
    }

    private void renderPlanet(IBody planet) {
        double time = container.base.getWorld().getWorldTime();
        double angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
        float planetX = (float) (planet.getDistance() * Math.cos(angle));
        float planetY = (float) (planet.getDistance() * Math.sin(angle));
        final double size = planet.getSize() * 2;
        mc.getTextureManager().bindTexture(planet.getLocation());
        GlStateManager.pushMatrix();
        GlStateManager.translate(planetX, planetY, 0);
        GlStateManager.scale((0.25 / 128D) * size, (0.25 / 128D) * size, 1);
        drawTexturedModalRect(-128, -128, 0, 0, 256, 256);
        GlStateManager.popMatrix();
    }

    private void renderSatellite(IBody planet, IBody satellite) {
        double time = container.base.getWorld().getWorldTime();
        double angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
        double angle1 = 2 * Math.PI * (time * satellite.getOrbitPeriod()) / 400D;
        float planetX =
                (float) ((float) (planet.getDistance() * Math.cos(angle)) + (satellite.getDistance() * 0.8 * Math.cos(angle1)));
        float planetY =
                (float) ((float) (planet.getDistance() * Math.sin(angle)) + (satellite.getDistance() * 0.8 * Math.sin(angle1)));
        final double size = satellite.getSize() * 2;
        mc.getTextureManager().bindTexture(satellite.getLocation());
        GlStateManager.pushMatrix();
        GlStateManager.translate(planetX, planetY, 0); // Без лишнего смещения на size1
        GlStateManager.scale((0.25 / 128D) * size, (0.25 / 128D) * size, 1);
        drawTexturedModalRect(-128, -128, 0, 0, 256, 256); // Центр текстуры в точке (0,0)
        GlStateManager.popMatrix();
    }

    private void enableScissor(int x, int y, int width, int height) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();
        int scaledHeight = scaledResolution.getScaledHeight();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);


        GL11.glScissor(
                x * scaleFactor,
                (scaledHeight - (y + height)) * scaleFactor,
                width * scaleFactor,
                height * scaleFactor
        );
    }

    private void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void renderIconWithProgressBar(int x, int y, int width, int height, float progress) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buffer.pos(x, y + height, 0).endVertex();
        buffer.pos(x + width, y + height, 0).endVertex();
        buffer.pos(x + width, y, 0).endVertex();
        buffer.pos(x, y, 0).endVertex();
        tessellator.draw();
        float filledWidth = width * MathHelper.clamp(progress, 0.0F, 1.0F);
        GlStateManager.color(0.0F, 1.0F, 0.0F, 1.0F);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buffer.pos(x, y + height, 0).endVertex();
        buffer.pos(x + filledWidth, y + height, 0).endVertex();
        buffer.pos(x + filledWidth, y, 0).endVertex();
        buffer.pos(x, y, 0).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        buffer.pos(x, y + height, 0).endVertex();
        buffer.pos(x + width, y + height, 0).endVertex();
        buffer.pos(x + width, y, 0).endVertex();
        buffer.pos(x, y, 0).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public void renderProgressBar(int x, int y, int width, int height, float progress, final EnumRoversLevel level) {
        ItemStack stack = null;
        switch (level) {
            case ONE:
                stack = new ItemStack(IUItem.rocket);
                break;
            case TWO:
                stack = new ItemStack(IUItem.adv_rocket);
                break;
            case THREE:
                stack = new ItemStack(IUItem.imp_rocket);
                break;
            case FOUR:
                stack = new ItemStack(IUItem.per_rocket);
                break;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        float filledWidth = width * MathHelper.clamp(progress, 0.0F, 1.0F);
        GlStateManager.translate(x + 10 + filledWidth, y + 2, 0);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public void renderProgressBarSends(int x, int y, int width, int height, float progress) {
        ItemStack stack;
        stack = new ItemStack(IUItem.rocket);
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        float filledWidth = height * MathHelper.clamp(progress, 0.0F, 1.0F);
        GlStateManager.translate(x + 5, y + height - filledWidth, 0);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public void renderProgressBackBar(int x, int y, int width, int height, float progress, final EnumRoversLevel level) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        ItemStack stack = null;
        switch (level) {
            case ONE:
                stack = new ItemStack(IUItem.rocket);
                break;
            case TWO:
                stack = new ItemStack(IUItem.adv_rocket);
                break;
            case THREE:
                stack = new ItemStack(IUItem.imp_rocket);
                break;
            case FOUR:
                stack = new ItemStack(IUItem.per_rocket);
                break;
        }
        GlStateManager.pushMatrix();

        RenderHelper.enableGUIStandardItemLighting();
        float filledWidth = width * MathHelper.clamp(progress, 0.0F, 1.0F);
        GlStateManager.translate(x - 10 + width - filledWidth, y + 20, 0);
        GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (mode == 0) {
            List<ISystem> systems = SpaceNet.instance.getSystem();
            systems = systems.stream().filter(iSystem -> !iSystem.getStarList().isEmpty()).collect(Collectors.toList());
            for (int index = 0; index < systems.size(); index++) {
                if (x >= 10 && x <= 40 && y >= 6 + 32 * index && y <= 36 + 32 * index) {
                    this.star = systems.get(index).getStarList().get(0);
                    this.mode = 1;
                }
            }
        } else if (mode == 5) {
            if (x > 130 && x <= 130 + 16 && y > 16 && y < 16 + 16) {
                new PacketAddBuildingToColony(this.container.base.colony);
            }
        } else if (mode == 1) {

            int dopX = 0;
            int dopY = 0;
            if (focusedPlanet != null) {
                if (focusedPlanet instanceof IPlanet) {
                    double time = container.base.getWorld().getWorldTime();
                    double angle = 2 * Math.PI * (time * focusedPlanet.getOrbitPeriod()) / 400D;
                    dopX = (int) ((focusedPlanet.getDistance() * Math.cos(angle)) * scale * 16);
                    dopY = (int) ((focusedPlanet.getDistance() * Math.sin(angle)) * scale * 16);
                    focusedPlanet = null;
                } else {
                    ISatellite satellite1 = (ISatellite) focusedPlanet;
                    double time = container.base.getWorld().getWorldTime();
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
                }
            }
            for (int index = 0; index < Math.min(star.getPlanetList().size() + star.getAsteroidList().size(), 7); index++) {
                if (x >= 190 && x <= 190 + 30 && y >= 6 + 32 * (index % 7) && y <= 31 + 6 + 32 * (index % 7)) {
                    if (index + value1 < star.getPlanetList().size()) {
                        IPlanet planet1 = star.getPlanetList().get(index + value1);
                        if (container.base.level == EnumLevels.NONE) {
                            continue;
                        }
                        if (planet1.getLevels() != EnumLevels.NONE && planet1
                                .getLevels()
                                .ordinal() > container.base.level.ordinal()) {
                            continue;
                        }
                        planet = planet1;
                        new PacketUpdateBody(container.base, planet);
                        mode = 2;
                    } else {
                        IAsteroid asteroid1 = star.getAsteroidList().get(index + value1 - star.getPlanetList().size());
                        if (container.base.level == EnumLevels.NONE) {
                            continue;
                        }
                        if (asteroid1.getLevels() != EnumLevels.NONE && asteroid1
                                .getLevels()
                                .ordinal() > container.base.level.ordinal()) {
                            continue;
                        }
                        asteroid = asteroid1;
                        new PacketUpdateBody(container.base, asteroid);
                        mode = 4;
                    }
                }
            }
            if (x >= 7 && x <= 169 && y >= 76 && y <= 90 + 76) {
                for (IPlanet planet : star.getPlanetList()) {
                    if (container.base.level == EnumLevels.NONE) {
                        continue;
                    }
                    if (planet.getLevels() != EnumLevels.NONE && planet.getLevels().ordinal() > container.base.level.ordinal()) {
                        continue;
                    }
                    double time = container.base.getWorld().getWorldTime();
                    double angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
                    float planetX = (float) (planet.getDistance() * Math.cos(angle)) * scale * 16;
                    float planetY = (float) (planet.getDistance() * Math.sin(angle)) * scale * 16;
                    double size = planet.getSize() * 2 * scale * 16;
                    if (x >= planetX - size / 4 + 7 + 162 / 2D + offsetX - dopX && x <= planetX + size / 4 + 7 + 162 / 2D + offsetX - dopX &&
                            y >= planetY - size / 4 + 80 + 82 / 2D + offsetY - dopY && y <= planetY + size / 4 + 80 + 82 / 2D + offsetY - dopY) {
                        focusedPlanet = planet;
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
                            time = container.base.getWorld().getWorldTime();
                            angle = 2 * Math.PI * (time * planet.getOrbitPeriod()) / 400D;
                            final double angle1 = 2 * Math.PI * (time * satellite1.getOrbitPeriod()) / 400D;
                            planetX =
                                    (float) ((float) (planet.getDistance() * Math.cos(angle)) + (satellite1.getDistance() * 0.8 * Math.cos(
                                            angle1))) * scale * 16;
                            planetY =
                                    (float) ((float) (planet.getDistance() * Math.sin(angle)) + (satellite1.getDistance() * 0.8 * Math.sin(
                                            angle1))) * scale * 16;
                            size = satellite1.getSize() * 2 * scale * 16;
                            if (x >= planetX - size / 4 + 7 + 162 / 2D + offsetX - dopX && x <= planetX + size / 4 + 7 + 162 / 2D + offsetX - dopX &&
                                    y >= planetY - size / 4 + 80 + 82 / 2D + offsetY - dopY && y <= planetY + size / 4 + 80 + 82 / 2D + offsetY - dopY) {
                                focusedPlanet = satellite1;
                                textIndex = 0;
                                break;
                            }
                        }
                    }
                }
            }
        } else if (mode == 2) {
            for (int index = 0; index < Math.min(planet.getSatelliteList().size(), 2); index++) {
                if (x >= 190 && x <= 190 + 30 && y >= 6 + 32 * (index % 2) + 5 * 31 && y < 6 + 32 * (index % 2) + 5 * 31 + 30) {
                    ISatellite satellite1 = planet.getSatelliteList().get(index + value2);
                    if (container.base.level == EnumLevels.NONE) {
                        continue;
                    }
                    if (planet.getLevels() != EnumLevels.NONE && planet
                            .getLevels()
                            .ordinal() > container.base.level.ordinal()) {
                        continue;
                    }
                    Data data = container.base.dataMap.get(planet);
                    if (planet.getLevels() != EnumLevels.NONE && data.getPercent() < 2) {
                        continue;
                    }
                    satellite = satellite1;
                    new PacketUpdateBody(container.base, satellite);
                    mode = 3;
                }
            }
        }

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
        mc.getTextureManager().bindTexture(texture);
        Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();

        renderCube(buffer, radius);
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
        GlStateManager.popMatrix();
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mode == 0) {
            if (mouseX >= xSize - 12 && mouseX <= xSize && mouseY >= 0 && mouseY <= 12) {
                List<String> text = new ArrayList<>();
                text.add(Localization.translate("iu.research1.info"));
                List<String> compatibleUpgrades = new ArrayList<>();
                for (int i = 1; i < 17; i++) {
                    compatibleUpgrades.add(Localization.translate("iu.research1.info" + i));
                }
                Iterator<String> var5 = compatibleUpgrades.iterator();
                String itemstack;
                while (var5.hasNext()) {
                    itemstack = var5.next();
                    text.add(itemstack);
                }

                this.drawTooltip(mouseX - 360, mouseY, text);
            }
        }
        if (mode == 1) {
            if (mouseX >= xSize - 12 && mouseX <= xSize && mouseY >= 0 && mouseY <= 12) {
                List<String> text = new ArrayList<>();
                text.add(Localization.translate("iu.research2.info"));
                List<String> compatibleUpgrades = new ArrayList<>();
                for (int i = 1; i < 14; i++) {
                    if (i == 12)
                        continue;
                    compatibleUpgrades.add(Localization.translate("iu.research2.info" + i));
                }
                Iterator<String> var5 = compatibleUpgrades.iterator();
                String itemstack;
                while (var5.hasNext()) {
                    itemstack = var5.next();
                    text.add(itemstack);
                }

                this.drawTooltip(mouseX - 360, mouseY, text);
            }
        }
        if (mode == 2 || mode == 3 || mode == 4) {
            if (mouseX >= xSize - 12 && mouseX <= xSize && mouseY >= 0 && mouseY <= 12) {
                List<String> text = new ArrayList<>();
                text.add(Localization.translate("iu.research3.info"));
                List<String> compatibleUpgrades = new ArrayList<>();
                for (int i = 1; i < 17; i++) {
                    compatibleUpgrades.add(Localization.translate("iu.research3.info" + i));
                }
                Iterator<String> var5 = compatibleUpgrades.iterator();
                String itemstack;
                while (var5.hasNext()) {
                    itemstack = var5.next();
                    text.add(itemstack);
                }

                this.drawTooltip(mouseX - 360, mouseY, text);
            }
        }
        if (mode == 5) {
            if (mouseX >= xSize - 12 && mouseX <= xSize && mouseY >= 0 && mouseY <= 12) {
                List<String> text = new ArrayList<>();
                text.add(Localization.translate("iu.research4.info"));
                List<String> compatibleUpgrades = new ArrayList<>();
                for (int i = 1; i < 20; i++) {
                    compatibleUpgrades.add(Localization.translate("iu.research4.info" + i));
                }
                Iterator<String> var5 = compatibleUpgrades.iterator();
                String itemstack;
                while (var5.hasNext()) {
                    itemstack = var5.next();
                    text.add(itemstack);
                }

                this.drawTooltip(mouseX - 360, mouseY, text);
            }
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        if (mode == 0) {
            if (star == null) {
                List<ISystem> systems = SpaceNet.instance.getSystem();
                systems = systems.stream().filter(iSystem -> !iSystem.getStarList().isEmpty()).collect(Collectors.toList());


                for (int i = 0; i < systems.size(); i++) {
                    IStar star = systems.get(i).getStarList().get(0);
                    String systemName = systems.get(i).getName();
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
                    new Area(this, 10, 6 + i * 32, 30, 30).withTooltip(
                            result
                    ).drawForeground(par1, par2);
                }
            }
        } else if (mode == 5) {
            IColony colony = this.container.base.colony;
            String text =
                    "Level: " + colony.getLevel()
                            + "\n" + Localization.translate("iu.space_colony_experience") + colony.getExperience() + "/" + colony.getMaxExperience()
                            + "\n" + Localization.translate("iu.space_colony_people") + (colony.getFreeWorkers() + colony.getWorkers())
                            + "\n" + Localization.translate("iu.space_colony_workers") + colony.getWorkers()
                            + "\n" + Localization.translate("iu.space_colony_need") + colony.getNeedWorkers()
                            + "\n" + Localization.translate("iu.space_colony_free") + colony.getFreeWorkers()
                            + "\n" + Localization.translate("iu.space_colony_energy") + ModUtils.getString(colony.getEnergy()) + "/" + ModUtils.getString(
                            colony.getMaxEnergy()) + " EF"
                            + "\n" + Localization.translate("iu.space_colony_oxygen") + ModUtils.getString(colony.getOxygen()) + "/" + ModUtils.getString(
                            colony.getMaxOxygen())
                            + "\n" + Localization.translate("iu.space_colony_food") + ModUtils.getString(colony.getFood()) +
                            "/" + ModUtils.getString((colony.getFreeWorkers() + colony.getWorkers()) * 15)
                            + "\n" + Localization.translate("iu.space_colony_using_energy") + ModUtils.getString(colony.getUsingEnergy()) + " EF/t"
                            + "\n" + Localization.translate("iu.space_colony_generation_energy") + ModUtils.getString(colony.getGenerationEnergy()) + " EF/t"
                            + "\n" + Localization.translate("iu.space_colony_using_oxygen") + ModUtils.getString(colony.getUsingOxygen()) + "t"
                            + "\n" + Localization.translate("iu.space_colony_generation_oxygen") + ModUtils.getString(colony.getGenerationOxygen()) + "t"
                            + "\n" + Localization.translate("iu.space_colony_using_food") + ModUtils.getString(colony.getUsingFood()) + "t"
                            + "\n" + Localization.translate("iu.space_colony_generation_food") + ModUtils.getString(colony.getGenerationFood()) + "t"
                            + "\n" + Localization.translate("iu.space_colony_protection") + colony.getProtection()
                            + "\n" + Localization.translate("iu.space_colony_need_protection") + colony
                            .getBuildingList()
                            .size() * 2
                            + "\n" + Localization.translate("iu.space_colony_entertainment") + ModUtils.getString(colony.getPercentEntertainment() * 100) + "%"
                            + "\n" + Localization.translate("iu.space_colony_available_building") + colony.getAvailableBuilding()
                            + "\n" + Localization.translate("iu.space_colony_houses") + colony.getBuildingHouseList().size()
                            + "\n" + Localization.translate("iu.space_colony_factories") + colony.getFactories().size()
                            + "\n" + Localization.translate("iu.space_colony_generators") + colony.getGenerators().size()
                            + "\n" + Localization.translate("iu.space_colony_oxygen_stations") + colony
                            .getOxygenFactoriesList()
                            .size()
                            + "\n" + Localization.translate("iu.space_colony_mining_factory") + colony
                            .getBuildingMiningList()
                            .size()
                            + "\n" + Localization.translate("iu.space_colony_storages") + colony.getStorageList().size()
                            + "\n" + Localization.translate("iu.space_colony_to_auto_delete") + (colony.getToDelete() != 30 ?
                            colony.getToDelete() + "s" : Localization.translate("iu.space_no"))
                            + "\n" + Localization.translate("iu.space_colony_has_problem") + (colony.getProblems().isEmpty()
                            ? Localization.translate("iu.space_no") : Localization.translate("iu.space_yes"))
                            + "\n" + Localization.translate("iu.space_colony_has_problem") + getProblem(colony);


            ;
            int canvasX = 10;
            int canvasY = 20;
            int canvasWidth = 114;
            int canvasHeight = 138;
            float scale = (float) (2D / new ScaledResolution(mc).getScaleFactor());
            if (prevText != text.length()) {
                scaled = -1;
                prevText = text.length();
            }
            if (scaled == -1) {
                scale = adjustTextScale(text, canvasWidth, canvasHeight, scale, 0.8F);
                scaled = scale;
            } else {
                scale = scaled;
            }
            if (this.container.base.getWorld().getWorldTime() % 2 == 0) {
                if (textIndex < text.length()) {
                    textIndex++;
                }
            }
            if (textIndex > text.length()) {
                textIndex = text.length();
            }
            String visibleText = text.substring(0, textIndex);
            drawTextInCanvas(visibleText, canvasX, canvasY, canvasWidth, canvasHeight, scale * 1.1f);
            for (int i = value3 * 4, j = 0; i < Math.min(this.itemList.size(), 16 + value3 * 4); j++, i++) {
                final int finalI = i;
                new ItemStackImage(
                        this,
                        150 + 4 + (j % 4) * 18,
                        20 + (j / 4) * 18,
                        () -> this.itemList.get(finalI)
                ).drawForeground(
                        par1,
                        par2
                );
            }
            for (int i = value4 * 4, j = 0; i < Math.min(this.fluidList.size(), 12 + value4 * 4); j++, i++) {
                new FluidItem(
                        this,
                        150 + 3 + (j % 4) * 18,
                        25 + 18 * 4 + 4 + (j / 4) * 18,
                        this.fluidList.get(i)
                ).drawForeground(
                        par1,
                        par2
                );
            }
            List<Timer> timers = this.container.base.getSends().getTimers();
            if (timers != null && !timers.isEmpty()) {
                new AdvArea(this, 129, 35, 19, 122).withTooltip(timers.get(0).getDisplay()).drawForeground(par1, par2);
            }
        } else if (mode == 4) {
            Data data = container.base.dataMap.get(asteroid);
            String text = getInformationFromBody(asteroid, data);
            int canvasX = 69;
            int canvasY = 26;
            int canvasWidth = 94;
            int canvasHeight = 55;
            float scale = (float) (2D / new ScaledResolution(mc).getScaleFactor());
            if (prevText != text.length()) {
                scaled = -1;
                prevText = text.length();
            }
            if (scaled == -1) {
                scale = adjustTextScale(text, canvasWidth, canvasHeight, scale, 0.8F);
                scaled = scale;
            } else {
                scale = scaled;
            }
            if (this.container.base.getWorld().getWorldTime() % 2 == 0) {
                if (textIndex < text.length()) {
                    textIndex++;
                }
            }
            if (textIndex > text.length()) {
                textIndex = text.length();
            }
            String visibleText = text.substring(0, textIndex);
            drawTextInCanvas(visibleText, canvasX, canvasY, canvasWidth, canvasHeight, scale);
            renderTooltipResource(asteroid, data, par1, par2);
            if (this.container.base.fakeBody != null && container.base.fakeBody.matched(asteroid)) {
                renderTooltipFakeBody(container.base.fakeBody, par1, par2);
            }
        } else if (mode == 3) {
            Data data = container.base.dataMap.get(satellite);
            String text = getInformationFromBody(satellite, data);
            int canvasX = 69;
            int canvasY = 26;
            int canvasWidth = 94;
            int canvasHeight = 55;
            float scale = (float) (2D / new ScaledResolution(mc).getScaleFactor());
            if (prevText != text.length()) {
                scaled = -1;
                prevText = text.length();
            }
            if (scaled == -1) {
                scale = adjustTextScale(text, canvasWidth, canvasHeight, scale, 0.8F);
                scaled = scale;
            } else {
                scale = scaled;
            }
            if (this.container.base.getWorld().getWorldTime() % 2 == 0) {
                if (textIndex < text.length()) {
                    textIndex++;
                }
            }
            if (textIndex > text.length()) {
                textIndex = text.length();
            }
            String visibleText = text.substring(0, textIndex);
            drawTextInCanvas(visibleText, canvasX, canvasY, canvasWidth, canvasHeight, scale);

            renderTooltipResource(satellite, data, par1, par2);

            if (this.container.base.fakeBody != null && container.base.fakeBody.matched(satellite)) {
                renderTooltipFakeBody(container.base.fakeBody, par1, par2);
            }
        } else if (mode == 2) {
            Data data = container.base.dataMap.get(planet);
            IPlanet focusedPlanet = planet;
            for (int i = value2, j = 0; i < Math.min(
                    planet.getSatelliteList().size(),
                    value2 + 2
            ); i++, j++) {
                if (i < planet.getSatelliteList().size()) {
                    ISatellite planet1 = planet.getSatelliteList().get(i);
                    double progress;
                    Data data1 = container.base.dataMap.get(planet1);
                    progress = data1.getPercent() / 100D;
                    new Area(this, 192, 8 + 32 * (i % 2) + 5 * 31, 30, 30)
                            .withTooltip(Localization.translate("iu.space_research") + " " + (int) (progress * 100) + "%")
                            .drawForeground(par1, par2);
                }


            }
            String text = getInformationFromBody(focusedPlanet, data);
            int canvasX = 69;
            int canvasY = 26;
            int canvasWidth = 94;
            int canvasHeight = 55;
            float scale = (float) (2D / new ScaledResolution(mc).getScaleFactor());
            if (prevText != text.length()) {
                scaled = -1;
                prevText = text.length();
            }
            if (scaled == -1) {
                scale = adjustTextScale(text, canvasWidth, canvasHeight, scale, 0.8F);
                scaled = scale;
            } else {
                scale = scaled;
            }
            if (this.container.base.getWorld().getWorldTime() % 2 == 0) {
                if (textIndex < text.length()) {
                    textIndex++;
                }
            }
            if (textIndex > text.length()) {
                textIndex = text.length();
            }
            String visibleText = text.substring(0, textIndex);
            drawTextInCanvas(visibleText, canvasX, canvasY, canvasWidth, canvasHeight, scale);
            renderTooltipResource(planet, data, par1, par2);
            if (this.container.base.fakeBody != null && container.base.fakeBody.matched(planet)) {
                renderTooltipFakeBody(container.base.fakeBody, par1, par2);
            }
        } else if (mode == 1) {
            if (focusedPlanet == null) {
                String text = "The Sun is approximately 4.6 billion years old and is at the midpoint of its life cycle, which will " +
                        "last about 10 billion years. The temperature on its surface is around 5,500°C, while at its core, it reaches approximately 15 million degrees Celsius.";
                int canvasX = 69;
                int canvasY = 14;
                int canvasWidth = 114;
                int canvasHeight = 55;
                float scale = (float) (2D / new ScaledResolution(mc).getScaleFactor());
                if (prevText != text.length()) {
                    prevText = text.length();
                    scaled = -1;
                }
                if (scaled == -1) {
                    scale = adjustTextScale(text, canvasWidth, canvasHeight, scale, 0.8F);
                    scaled = scale;
                } else {
                    scale = scaled;
                }
                if (this.container.base.getWorld().getWorldTime() % 2 == 0) {
                    if (textIndex < text.length()) {
                        textIndex++;
                    }
                }
                if (textIndex > text.length()) {
                    textIndex = text.length();
                }
                String visibleText = text.substring(0, textIndex);
                drawTextInCanvas(visibleText, canvasX, canvasY, canvasWidth, canvasHeight, scale);
            } else {
                Data data = container.base.dataMap.get(focusedPlanet);
                String text = "";
                if (focusedPlanet instanceof IPlanet) {
                    text = getInformationFromBody(focusedPlanet, data);
                } else if (focusedPlanet instanceof ISatellite) {
                    text = getInformationFromBody(focusedPlanet, data);
                }
                int canvasX = 69;
                int canvasY = 14;
                int canvasWidth = 114;
                int canvasHeight = 55;
                float scale = (float) (2D / new ScaledResolution(mc).getScaleFactor());
                if (prevText != text.length()) {
                    scaled = -1;
                    prevText = text.length();
                }
                if (scaled == -1) {
                    scale = adjustTextScale(text, canvasWidth, canvasHeight, scale, 0.8F);
                    scaled = scale;
                } else {
                    scale = scaled;
                }
                if (this.container.base.getWorld().getWorldTime() % 2 == 0) {
                    if (textIndex < text.length()) {
                        textIndex++;
                    }
                }
                if (textIndex > text.length()) {
                    textIndex = text.length();
                }
                String visibleText = text.substring(0, textIndex);
                drawTextInCanvas(visibleText, canvasX, canvasY, canvasWidth, canvasHeight, scale);
            }
            for (int i = value1, j = 0; i < Math.min(
                    star.getPlanetList().size() + this.star.getAsteroidList().size(),
                    value1 + 7
            ); i++, j++) {
                if (i < star.getPlanetList().size()) {
                    IPlanet planet1 = star.getPlanetList().get(i);
                    double progress;
                    Data data = container.base.dataMap.get(planet1);
                    progress = data.getPercent() / 100D;
                    if (planet1 == SpaceInit.earth) {
                        progress = 1F;
                    }
                    new Area(this, 190, 6 + 32 * (i % 7), 30, 30)
                            .withTooltip(Localization.translate("iu.space_research") + " " + (int) (progress * 100) + "%")
                            .drawForeground(par1, par2);
                } else {
                    IAsteroid planet1 = star.getAsteroidList().get(i - star.getPlanetList().size());
                    double progress;
                    Data data = container.base.dataMap.get(planet1);
                    progress = data.getPercent() / 100D;
                    new Area(this, 190, 6 + 32 * (i % 7), 30, 30)
                            .withTooltip(Localization.translate("iu.space_research") + " " + (int) (progress * 100) + "%")
                            .drawForeground(par1, par2);
                }

            }
        }
    }

    private String getProblem(IColony colony) {
        if (!colony.getProblems().isEmpty()){
            StringBuilder problem = new StringBuilder();
            for (EnumProblems problems : colony.getProblems()){
                problem.append(problems.name()).append("\n");
            }
            return problem.toString();
        }
        return "";
    }

    private void renderTooltipResource(IBody asteroid, Data data, int par1, int par2) {
        for (int i = 0; i < asteroid.getResources().size(); i++) {
            IBaseResource resource = asteroid.getResources().get(i);
            if (resource.getItemStack() != null && !(resource.getPercentPanel() > data.getPercent())) {
                new Area(this, ((15 + (i % 11) * 13)), (int) ((27 + 66 + 13 * (i / 11))), 13, 13).withTooltip(
                        resource
                                .getItemStack()
                                .getDisplayName() + "\n" + Localization.translate("iu.space_chance") + " " + ModUtils.getString(
                                resource.getChance() * 100D / resource.getMaxChance()) + "%" +
                                "\n" + Localization.translate("iu.space_rover") + " " + Localization.translate("iu" +
                                ".space_rover_" + resource.getTypeRovers().name().toLowerCase())
                ).drawForeground(par1, par2);
            }
            if (resource.getFluidStack() != null && !(resource.getPercentPanel() > data.getPercent())) {
                new Area(this, ((15 + (i % 11) * 13)), (int) ((27 + 66 + 13 * (i / 11))), 13, 13).withTooltip(
                        resource
                                .getFluidStack()
                                .getLocalizedName() + "\n" + Localization.translate("iu.space_amount") + " " + resource.getFluidStack().amount + "mb" + "\n" +
                                Localization.translate("iu.space_chance") + " " + ModUtils.getString(resource.getChance() * 100D / resource.getMaxChance()) + "%" +
                                "\n" + Localization.translate("iu.space_rover") + " " + Localization.translate("iu.space_rover_" + resource
                                .getTypeRovers()
                                .name()
                                .toLowerCase())
                ).drawForeground(par1, par2);
            }
        }
    }

    private void renderTooltipFakeBody(IFakeBody fakePlanet, int par1, int par2) {
        if (fakePlanet.getTimerTo().canWork()) {
            new Area(this, 8, 136, 164, 28)
                    .withTooltip(fakePlanet.getTimerTo().getDisplay())
                    .drawForeground(par1, par2);
        }
        if (fakePlanet.getTimerFrom().canWork()) {
            new Area(this, 8, 136, 164, 28)
                    .withTooltip(fakePlanet.getTimerFrom().getDisplay())
                    .drawForeground(par1, par2);
        }
    }

    float scaled = -1;
    int prevText;


    public String getInformationFromBody(IBody body, Data data) {
        if (body instanceof IPlanet) {
            if (body == SpaceInit.earth) {
                data.setInformation(100);
            }
            IPlanet focusedPlanet = (IPlanet) body;

            return Localization.translate("iu.space_level") + " " + focusedPlanet.getLevels().name() + "\n" +
                    Localization.translate("iu.space_planet") + " " + (data.getPercent() > 0
                    ?
                    Localization.translate("iu.body." + body.getName().toLowerCase())
                    : TextFormatting.OBFUSCATED + Localization.translate("iu.body." + body.getName().toLowerCase())) + "\n" +
                    Localization.translate("iu.space_amount_satellites") + " " + (data.getPercent() >= 2 ?
                    focusedPlanet.getSatelliteList().size() :
                    TextFormatting.OBFUSCATED + "" + focusedPlanet.getSatelliteList().size()) + "\n" +
                    Localization.translate("iu.space_size") + " " + (data.getPercent() >= 1 ?
                    ModUtils.getString(focusedPlanet.getSize() * 12756D / SpaceInit.earth.getSize()) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_has_pressure") + " " + (data.getPercent() >= 4 ?
                    (!focusedPlanet.getPressure() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes")) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_distance") + " " + (data.getPercent() >= 0.5 ?
                    ModUtils.getString(focusedPlanet.getDistance() * 149200000D / SpaceInit.earth.getDistance()) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_orbit_time") + " " + (data.getPercent() >= 6 ?
                    ModUtils.getString(focusedPlanet.getOrbitPeriod() * 365 / SpaceInit.earth.getOrbitPeriod()) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_temperature") + " " + (data.getPercent() >= 9 ?
                    focusedPlanet.getTemperature() + "C°" :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_has_oxygen") + " " + (data.getPercent() >= 11 ?
                    (!focusedPlanet.hasOxygen() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes")) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_colonies") + " " + (data.getPercent() >= 15 ?
                    (!focusedPlanet.canHaveColonies() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes")) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_research") + " " + ModUtils.getString(data.getPercent()) + "%";
        } else if (body instanceof ISatellite) {
            ISatellite focusedPlanet = (ISatellite) body;


            return Localization.translate("iu.space_level") + " " + focusedPlanet.getLevels().name() + "\n" +
                    Localization.translate("iu.space_satellite") + " " + (data.getPercent() > 0
                    ?
                    Localization.translate("iu.body." + body.getName().toLowerCase())
                    : TextFormatting.OBFUSCATED + Localization.translate("iu.body." + body.getName().toLowerCase())) + "\n" +
                    Localization.translate("iu.space_planet") + " " + Localization.translate("iu.body." + focusedPlanet
                    .getPlanet()
                    .getName()
                    .toLowerCase()) + "\n" +
                    Localization.translate("iu.space_size") + " " + (data.getPercent() >= 1 ?
                    ModUtils.getString(focusedPlanet.getSize() * 3474D / SpaceInit.moon.getSize()) :
                    TextFormatting.OBFUSCATED + ModUtils.getString(focusedPlanet.getSize() * 12756D / SpaceInit.earth.getSize())) + "\n" +
                    Localization.translate("iu.space_has_pressure") + " " + (data.getPercent() >= 4 ?
                    (!focusedPlanet.getPressure() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes")) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_distance_from_planet") + " " + (data.getPercent() >= 0.5 ?
                    ModUtils.getString(focusedPlanet.getDistance() * 384400 / SpaceInit.moon.getDistance()) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_orbit_time") + " " + (data.getPercent() >= 6 ?
                    ModUtils.getString(focusedPlanet.getOrbitPeriod() * 27 / SpaceInit.moon.getOrbitPeriod()) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_temperature") + " " + (data.getPercent() >= 9 ?
                    focusedPlanet.getTemperature() + "C°" :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_has_oxygen") + " " + (data.getPercent() >= 11 ?
                    (!focusedPlanet.hasOxygen() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes")) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_colonies") + " " + (data.getPercent() >= 15 ?
                    (!focusedPlanet.canHaveColonies() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes")) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_research") + " " + ModUtils.getString(data.getPercent()) + "%";
        } else if (body instanceof IAsteroid) {
            IAsteroid focusedPlanet = (IAsteroid) body;
            return Localization.translate("iu.space_level") + " " + focusedPlanet.getLevels().name() + "\n" +
                    Localization.translate("iu.space_asteroid") + " " + (data.getPercent() > 0
                    ?
                    Localization.translate("iu.body." + body.getName().toLowerCase())
                    : TextFormatting.OBFUSCATED + Localization.translate("iu.body." + body.getName().toLowerCase())) + "\n" +
                    Localization.translate("iu.space_amount_asteroids") + " " + (data.getPercent() >= 1 ?
                    focusedPlanet.getMiniAsteroid().size() :
                    TextFormatting.OBFUSCATED + "" + focusedPlanet.getMiniAsteroid().size()) + "\n" +
                    Localization.translate("iu.space_size") + " " + (data.getPercent() >= 1 ?
                    ModUtils.getString(focusedPlanet.getSize() * 3474D / SpaceInit.moon.getSize()) :
                    TextFormatting.OBFUSCATED + ModUtils.getString(focusedPlanet.getSize() * 12756D / SpaceInit.earth.getSize())) + "\n" +
                    Localization.translate("iu.space_minimum_distance") + " " + (data.getPercent() >= 4 ?
                    ModUtils.getString(focusedPlanet.getMinDistance() * 149200000D / SpaceInit.earth.getDistance()) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_maximum_distance") + " " + (data.getPercent() >= 4 ?
                    ModUtils.getString(focusedPlanet.getMinDistance() * 149200000D / SpaceInit.earth.getDistance()) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_temperature") + " " + (data.getPercent() >= 9 ?
                    focusedPlanet.getTemperature() + "C°" :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_has_oxygen") + " " + (data.getPercent() >= 11 ?
                    (Localization.translate("iu.space_no")) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_colonies") + " " + (data.getPercent() >= 15 ?
                    (!focusedPlanet.canHaveColonies() ? Localization.translate("iu.space_no") : Localization.translate("iu" +
                            ".space_yes")) :
                    TextFormatting.OBFUSCATED + "???") + "\n" +
                    Localization.translate("iu.space_research") + " " + ModUtils.getString(data.getPercent()) + "%";

        }
        return "";
    }


    int textIndex = 0;

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiresearch_table.png");
    }

}
