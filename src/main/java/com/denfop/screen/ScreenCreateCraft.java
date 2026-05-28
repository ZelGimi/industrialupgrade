package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.containermenu.ContainerMonitor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenCreateCraft extends ScreenMain<ContainerMonitor> {

    private final ScreenMonitor<ContainerMonitor> monitor;
    private final SameStack sameStack;
    int count = 1;

    public ScreenCreateCraft(ScreenMonitor<ContainerMonitor> monitor, SameStack sameStack) {
        super(monitor.container);
        this.monitor = monitor;
        this.sameStack = sameStack;
        this.count = sameStack.getAmount();
    }

    protected void mouseClicked(int i, int j, int k) {
        int xMin = (monitor.width - monitor.getXSize()) / 2;
        int yMin = (monitor.height - monitor.getYSize()) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    public void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {

    }

    public void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");

    }
}
