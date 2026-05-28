package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.storage.InterfaceSide;
import com.denfop.containermenu.ContainerInterface;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScreenInterface<T extends ContainerInterface> extends ScreenMain<ContainerInterface> {

    private final boolean[] hover = new boolean[]{false, false, false, false};

    public ScreenInterface(ContainerInterface guiContainer) {
        super(guiContainer);
        this.elements.clear();
        this.componentList.clear();
        this.imageHeight = 203;
        this.imageWidth = 200;
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);

        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;

        for (int ii = 0; ii < 4; ii++) {
            int x1 = 173;
            int x2 = 192;
            int y1 = 13 + 27 * ii;
            int y2 = 34 + 27 * ii;

            if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                new PacketUpdateServerTile(container.base, ii * 10);
                return;
            }
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        int hoveredIndex = -1;

        for (int ii = 0; ii < 4; ii++) {
            hover[ii] = false;

            int x1 = 173;
            int x2 = 192;
            int y1 = 13 + 27 * ii;
            int y2 = 34 + 27 * ii;

            if (par1 >= x1 && par1 <= x2 && par2 >= y1 && par2 <= y2) {
                hover[ii] = true;
                hoveredIndex = ii;
            }
        }

        if (hoveredIndex != -1) {
            InterfaceSide side = this.container.base.getSides(hoveredIndex);
            poseStack.renderTooltip(
                    this.font,
                    buildSideTooltip(side),
                    Optional.empty(),
                    par1,
                    par2
            );
        }
    }

    private List<Component> buildSideTooltip(InterfaceSide side) {
        List<Component> lines = new ArrayList<>();

        lines.add(Component.literal(getSideTooltipText(side)));
        lines.add(Component.literal(Localization.translate("iu.interface.tooltip.change")));

        return lines;
    }

    private String getSideTooltipText(InterfaceSide side) {
        if (side == null || side == InterfaceSide.ANY) {
            return Localization.translate("iu.interface.tooltip.any");
        }

        return switch (side) {
            case UP -> Localization.translate("iu.interface.tooltip.prefix")
                    + Localization.translate("iu.dir.top")
                    + Localization.translate("iu.interface.tooltip.suffix");
            case DOWN -> Localization.translate("iu.interface.tooltip.prefix")
                    + Localization.translate("iu.dir.bottom")
                    + Localization.translate("iu.interface.tooltip.suffix");
            case NORTH -> Localization.translate("iu.interface.tooltip.prefix")
                    + Localization.translate("iu.dir.north")
                    + Localization.translate("iu.interface.tooltip.suffix");
            case EAST -> Localization.translate("iu.interface.tooltip.prefix")
                    + Localization.translate("iu.dir.east")
                    + Localization.translate("iu.interface.tooltip.suffix");
            case SOUTH -> Localization.translate("iu.interface.tooltip.prefix")
                    + Localization.translate("iu.dir.south")
                    + Localization.translate("iu.interface.tooltip.suffix");
            case WEST -> Localization.translate("iu.interface.tooltip.prefix")
                    + Localization.translate("iu.dir.west")
                    + Localization.translate("iu.interface.tooltip.suffix");
            case ANY -> Localization.translate("iu.interface.tooltip.any");
        };
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);

        for (int ii = 0; ii < 4; ii++) {
            int x1 = 173;
            int x2 = 192;
            int y1 = 13 + 27 * ii;
            int y2 = 34 + 27 * ii;

            InterfaceSide interfaceSide = this.container.base.getSides(ii);

            if (interfaceSide != null && interfaceSide.ordinal() != 0) {
                drawTexturedModalRect(
                        poseStack,
                        this.guiLeft + x1,
                        this.guiTop + y1,
                        235,
                        1 + 23 * (interfaceSide.ordinal() - 1),
                        1 + x2 - x1,
                        1 + y2 - y1
                );
            }

            if (hover[ii]) {
                drawTexturedModalRect(
                        poseStack,
                        this.guiLeft + x1,
                        this.guiTop + y1,
                        235,
                        139,
                        1 + x2 - x1,
                        1 + y2 - y1
                );
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiinterface.png");
    }
}