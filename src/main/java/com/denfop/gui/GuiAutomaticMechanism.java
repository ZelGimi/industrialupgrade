package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.container.ContainerAutomaticMechanism;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.base.Upgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class GuiAutomaticMechanism<T extends ContainerAutomaticMechanism> extends GuiIU<ContainerAutomaticMechanism> {

    public GuiAutomaticMechanism(ContainerAutomaticMechanism guiContainer) {
        super(guiContainer);
        this.imageWidth = 212;
        this.imageHeight = 243;
        this.inventory.setY(this.inventory.getY() + 77);
        this.inventory.setX(this.inventory.getX() + 18);
        this.elements.add(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - xMin;
        int y = j - yMin;
        for (int index = 0; index < 6; index++) {
            if (x >= 115 && x <= 132 && y >= 24 + 18 * index && y < 42 + 18 * index) {
                new PacketUpdateServerTile(this.container.base, index);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        for (int index = 0; index < 6; index++) {
            Direction facing = Direction.values()[index];
            final Upgrade upgrade = this.container.base.typeUpgradeMap.get(facing);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            bindTexture();
            switch (upgrade) {
                case EXTRACT:
                    new ItemStackImage(this, 115, 24 + 18 * index, () -> IUItem.ejectorUpgrade).drawBackground(poseStack,
                            this.guiLeft,
                            this.guiTop
                    );
                    break;
                case PULLING:
                case EXT_PUL:
                    new ItemStackImage(this, 115, 24 + 18 * index, () -> IUItem.pullingUpgrade).drawBackground(poseStack,
                            this.guiLeft,
                            this.guiTop
                    );
                    break;
                case NONE:

                    bindTexture(ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/gui_progressbars.png"
                    ));
                    drawTexturedModalRect(poseStack, this.guiLeft + 118, this.guiTop + 27 + 18 * index, 85, 60, 11, 11);
                    break;
            }
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        for (int index = 0; index < 6; index++) {
            Direction facing = Direction.values()[index];
            final Upgrade upgrade = this.container.base.typeUpgradeMap.get(facing);

            switch (facing) {
                case EAST:
                    new Area(this, 7, 24 + 18 * index, 18 * 6, 18)
                            .withTooltip(Localization.translate("iu.dir.east"))
                            .drawForeground(poseStack, par1, par2);
                    break;
                case WEST:
                    new Area(this, 7, 24 + 18 * index, 18 * 6, 18)
                            .withTooltip(Localization.translate("iu.dir.west"))
                            .drawForeground(poseStack, par1, par2);
                    break;

                case SOUTH:

                    new Area(this, 7, 24 + 18 * index, 18 * 6, 18)
                            .withTooltip(Localization.translate("iu.dir.south"))
                            .drawForeground(poseStack, par1, par2);
                    break;
                case NORTH:

                    new Area(this, 7, 24 + 18 * index, 18 * 6, 18)
                            .withTooltip(Localization.translate("iu.dir.north"))
                            .drawForeground(poseStack, par1, par2);
                    break;
                case UP:
                    new Area(this, 7, 24 + 18 * index, 18 * 6, 18)
                            .withTooltip(Localization.translate("iu.dir.top"))
                            .drawForeground(poseStack, par1, par2);
                    break;
                case DOWN:
                    new Area(this, 7, 24 + 18 * index, 18 * 6, 18)
                            .withTooltip(Localization.translate("iu.dir.bottom"))
                            .drawForeground(poseStack, par1, par2);
                    break;
            }
            switch (upgrade) {
                case EXTRACT:
                    new Area(this, 115, 24 + 18 * index, 18, 18)
                            .withTooltip(IUItem.ejectorUpgrade.getDisplayName().getString())
                            .drawForeground(poseStack, par1, par2);
                    break;
                case PULLING:
                    new Area(this, 115, 24 + 18 * index, 18, 18)
                            .withTooltip(IUItem.pullingUpgrade.getDisplayName().getString())
                            .drawForeground(poseStack, par1, par2);
                    break;
                case EXT_PUL:
                    new Area(this, 115, 24 + 18 * index, 18, 18)
                            .withTooltip(IUItem.ejectorUpgrade.getDisplayName().getString() + "\n" + IUItem.pullingUpgrade.getDisplayName().getString())
                            .drawForeground(poseStack, par1, par2);
                    break;
                case NONE:
                    new Area(this, 115, 24 + 18 * index, 18, 18).withTooltip("None").drawForeground(poseStack, par1, par2);
                    break;
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiautomaticmechanism.png");
    }

}
