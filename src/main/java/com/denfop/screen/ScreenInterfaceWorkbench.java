package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.StorageInterfaceWidget;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerInterfaceWorkbench;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenInterfaceWorkbench<T extends ContainerInterfaceWorkbench> extends ScreenMain<ContainerInterfaceWorkbench> {


    public ScreenInterfaceWorkbench(ContainerInterfaceWorkbench guiContainer) {
        super(guiContainer, EnumTypeStyle.STORAGE);
        this.inventory.addY(220 - 166);
        this.imageHeight += 220 - 166;
        this.addWidget(new StorageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
    }


    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);

    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");

    }

}
