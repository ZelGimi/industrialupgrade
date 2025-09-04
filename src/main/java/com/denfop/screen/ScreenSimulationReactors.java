package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuSimulationReactors;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ScreenSimulationReactors<T extends ContainerMenuSimulationReactors> extends ScreenMain<ContainerMenuSimulationReactors> {

    private final String nameReactor;
    boolean hoverWork = false;
    boolean[] hoverLevel = new boolean[]{false, false, false, false};
    boolean[] hoverType = new boolean[]{false, false, false, false};

    public ScreenSimulationReactors(ContainerMenuSimulationReactors guiContainer) {

        super(guiContainer);
        this.imageWidth = 232;
        this.imageHeight = 226;
        componentList.clear();


        String nameReactor1;
        nameReactor1 = "";
        if (guiContainer.base.reactors != null) {
            nameReactor1 = Localization.translate("multiblock." + guiContainer.base.reactors.getNameReactor().toLowerCase());
        }

        this.nameReactor = nameReactor1;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - xMin;
        int y = j - yMin;

        for (int index = 0; index < 4; index++) {
            if (hoverLevel[index]) {
                new PacketUpdateServerTile(this.container.base, -(index + 1));
                this.container.base.updateTileServer(Minecraft.getInstance().player, -(index + 1));
            }
        }
        for (int index = 0; index < 4; index++) {
            if (hoverType[index]) {
                new PacketUpdateServerTile(this.container.base, (index + 1));
                this.container.base.updateTileServer(Minecraft.getInstance().player, (index + 1));
            }
        }
        if (hoverWork) {
            this.container.base.updateTileServer(Minecraft.getInstance().player, 0);
        }
    }

    private void handleUpgradeTooltip2(
            int x,
            int y
    ) {
        if (this.container.base.work && this.container.base.reactors != null && this.container.base.logicReactor != null) {
            if (x >= 6 && x <= 26 && y >= 95 && y <= 115) {


                List<String> compatibleUpgrades = new ArrayList<>();
                compatibleUpgrades.add("Output: " +
                        ModUtils.getString(this.container.base.output) + " EF"
                );
                compatibleUpgrades.add("Radiation: " +
                        ModUtils.getString(this.container.base.rad) + " ☢"
                );
                compatibleUpgrades.add("Heat: " +
                        (int) this.container.base.heat + " °C"
                );
                compatibleUpgrades.add("Stable: " +
                        (int) this.container.base.reactor.getStableMaxHeat() + " °C"
                );
                compatibleUpgrades.add("Max Heat: " +
                        (int) this.container.base.reactor.getMaxHeat() + " °C"
                );
                compatibleUpgrades.add(Localization.translate(
                        "waterreactor.security." + this.container.base.security.name().toLowerCase())
                );

                this.drawTooltip(x, y, compatibleUpgrades);
            }
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip2(par1, par2);
        hoverWork = false;
        hoverLevel = new boolean[]{false, false, false, false};
        hoverType = new boolean[]{false, false, false, false};
        if (par1 >= 205 && par2 >= 119 && par1 <= 224 && par2 <= 138) {
            hoverWork = true;
        }
        for (int i = 0; i < 4; i++) {
            if (par1 >= 7 && par2 >= 12 + i * 20 && par1 <= 24 && par2 <= 30 + i * 20) {
                hoverLevel[i] = true;
            }
        }
        for (int i = 0; i < 4; i++) {
            if (par1 >= 207 && par2 >= 12 + i * 20 && par1 <= 224 && par2 <= 30 + i * 20) {
                hoverType[i] = true;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        for (int i = 0; i < 4; i++) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 7, this.guiTop + 12 + i * 20, 237, 1, 18, 18);
            if (this.container.base.levelReactor != -1 && this.container.base.levelReactor == i + 1) {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 7, this.guiTop + 12 + i * 20, 237, 21, 18, 18);

            }
            if (hoverLevel[i]) {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 7, this.guiTop + 12 + i * 20, 237, 41, 18, 19);

            }
            poseStack.pose().translate(0.5, 0.5, 0);
            this.drawTexturedModalRect(poseStack, this.guiLeft + 10, this.guiTop + 15 + i * 20, 232, 103 + 13 * i, 11, 11);
            poseStack.pose().translate(-0.5, -0.5, 0);
        }
        for (int i = 0; i < 4; i++) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 207, this.guiTop + 12 + i * 20, 237, 1, 18, 18);
            if (this.container.base.type != -1 && this.container.base.type == i + 1) {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 207, this.guiTop + 12 + i * 20, 237, 21, 18, 18);

            }
            poseStack.pose().translate(0.5, 0.5, 0);
            this.drawTexturedModalRect(poseStack, this.guiLeft + 210, this.guiTop + 15 + i * 20, 244, 103 + 13 * i, 11, 11);
            poseStack.pose().translate(-0.5, -0.5, 0);
            if (hoverType[i]) {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 207, this.guiTop + 12 + i * 20, 237, 41, 18, 19);

            }
        }
        if (hoverWork) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 205, this.guiTop + 119, 235, 61, 20, 20);

        }
        if (this.container.base.work) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 205, this.guiTop + 119, 235, 82, 20, 21);

        }
        if (this.container.base.type != -1 && this.container.base.levelReactor != -1 && this.container.base.reactors != null) {
            int minX = (int) (240 / 2 - (this.container.base.reactors.getWidth() / 2D) * 18);
            int minY = (int) (150 / 2 - (this.container.base.reactors.getHeight() / 2D) * 18);

            for (int y = 0; y < this.container.base.reactors.getHeight(); y++) {
                for (int x = 0; x < this.container.base.reactors.getWidth(); x++) {
                    final int finalY = y;
                    final int finalX = x;
                    this.drawTexturedModalRect(poseStack, this.guiLeft + minX + finalX * 18 - 1, this.guiTop + minY + finalY * 18 - 1, 238, 177, 18, 18);
                }
            }
        }


        if (!this.isBlack) {
            this.drawXCenteredString(poseStack, this.imageWidth / 2, -9, this.nameReactor, ModUtils.convertRGBcolorToInt(255, 255, 255), false);
        } else {
            this.drawXCenteredString(poseStack, this.imageWidth / 2, 4, this.nameReactor, ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));

    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guireactorsimulator.png");
    }

}
