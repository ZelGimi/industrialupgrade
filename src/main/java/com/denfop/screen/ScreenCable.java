package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.containermenu.ContainerMenuCable;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenCable<T extends ContainerMenuCable> extends ScreenMain<ContainerMenuCable> {


    public ScreenCable(ContainerMenuCable guiContainer) {
        super(guiContainer);
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, imageWidth, imageHeight));

        this.addWidget(new TooltipWidget(this, 58, 20 + 18 - 2, 18, 14).withTooltip(Localization.translate("iu.dir.west")));
        this.addWidget(new TooltipWidget(this, 88, 20 + 18 - 2, 18, 14).withTooltip(Localization.translate("iu.dir.east")));
        this.addWidget(new TooltipWidget(this, 75, 20, 14, 18).withTooltip(Localization.translate("iu.dir.north")));
        this.addWidget(new TooltipWidget(this, 75, 48, 14, 18).withTooltip(Localization.translate("iu.dir.south")));
        this.addWidget(new TooltipWidget(this, 75, 6, 14, 14).withTooltip(Localization.translate("iu.dir.top")));
        this.addWidget(new TooltipWidget(this, 75, 66, 14, 14).withTooltip(Localization.translate("iu.dir.bottom")));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.limiter.info"));
            List<String> compatibleUpgrades = ListInformationUtils.limiter_info;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 58 && y >= 20 + 18 - 2 && x <= 58 + 18 && y <= 20 + 18 - 2 + 14) {
            new PacketUpdateServerTile(this.container.base, Direction.WEST.ordinal());
        }
        if (x >= 88 && y >= 20 + 18 - 2 && x <= 88 + 18 && y <= 20 + 18 - 2 + 14) {
            new PacketUpdateServerTile(this.container.base, Direction.EAST.ordinal());
        }
        if (x >= 75 && y >= 20 && x <= 75 + 14 && y <= 20 + 18) {
            new PacketUpdateServerTile(this.container.base, Direction.NORTH.ordinal());
        }
        if (x >= 75 && y >= 48 && x <= 75 + 14 && y <= 48 + 18) {
            new PacketUpdateServerTile(this.container.base, Direction.SOUTH.ordinal());
        }
        if (x >= 75 && y >= 6 && x <= 75 + 14 && y <= 6 + 14) {
            new PacketUpdateServerTile(this.container.base, Direction.UP.ordinal());
        }
        if (x >= 75 && y >= 66 && x <= 75 + 14 && y <= 66 + 14) {
            new PacketUpdateServerTile(this.container.base, Direction.DOWN.ordinal());
        }
    }


    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        int xoffset = guiLeft;
        int yoffset = guiTop;


        bindTexture(getTexture());
        for (Direction facing : Direction.values()) {
            if (facing.ordinal() == 0) {
                drawTexturedModalRect(poseStack, xoffset + 75, yoffset + 66, 62, 191, 14, 14);
            }
            if (facing.ordinal() == 1) {
                drawTexturedModalRect(poseStack, xoffset + 75, yoffset + 6, 62, 119, 14, 14);
            }
            if (facing.ordinal() == 2) {
                drawTexturedModalRect(poseStack, xoffset + 75, yoffset + 20, 62, 135, 14, 18);
            }
            if (facing.ordinal() == 3) {
                drawTexturedModalRect(poseStack, xoffset + 75, yoffset + 48, 62, 171, 14, 18);
            }
            if (facing.ordinal() == 4) {
                drawTexturedModalRect(poseStack, xoffset + 58, yoffset + 20 + 18 - 2, 42, 155, 18, 14);
            }
            if (facing.ordinal() == 5) {
                drawTexturedModalRect(poseStack, xoffset + 88, yoffset + 20 + 18 - 2, 78, 155, 18, 14);
            }
        }
        for (Direction facing : this.container.base.getBlackList()) {
            if (facing.ordinal() == 0) {
                drawTexturedModalRect(poseStack, xoffset + 75, yoffset + 66, 120, 191, 14, 14);
            }
            if (facing.ordinal() == 1) {
                drawTexturedModalRect(poseStack, xoffset + 75, yoffset + 6, 120, 119, 14, 14);
            }
            if (facing.ordinal() == 2) {
                drawTexturedModalRect(poseStack, xoffset + 75, yoffset + 20, 120, 135, 14, 18);
            }
            if (facing.ordinal() == 3) {
                drawTexturedModalRect(poseStack, xoffset + 75, yoffset + 48, 120, 171, 14, 18);
            }
            if (facing.ordinal() == 4) {
                drawTexturedModalRect(poseStack, xoffset + 58, yoffset + 20 + 18 - 2, 100, 155, 18, 14);
            }
            if (facing.ordinal() == 5) {
                drawTexturedModalRect(poseStack, xoffset + 88, yoffset + 20 + 18 - 2, 137, 155, 18, 14);
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars1.png");
    }

}
