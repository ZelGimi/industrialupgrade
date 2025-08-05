package com.denfop.gui;

import com.denfop.api.gui.ImageSpaceInterface1;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.container.ContainerResearchTableSpace;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

public class GuiDefaultResearchTable {
    public final GuiResearchTableSpace<ContainerResearchTableSpace> tile;
    public final int x;
    public int width;
    public final int y;
    public int height;
    public int offsetX1 = 0, offsetY1 = 0;
    public int lastMouseX1, lastMouseY1;
    public GuiDefaultResearchTable(GuiResearchTableSpace<ContainerResearchTableSpace> tileEntityResearchTableSpace, int x, int y, int width, int height){
        this.tile = tileEntityResearchTableSpace;
        this.x = x;
        this.y=y;
        this.width=width;
        this.height=height;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public GuiResearchTableSpace<ContainerResearchTableSpace> getTile() {
        return tile;
    }
    public void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {

    }
    public void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {

        RenderSystem.enableBlend();
        new ImageSpaceInterface1(tile, x + offsetX1, y+ offsetY1, width, height).drawBackground(poseStack, tile.guiLeft, tile.guiTop);

        RenderSystem.disableBlend();

    }
    public boolean is(int x, int y) {
        int tempX = this.x + offsetX1;
        int tempy = this.y + offsetY1;
        return tempX <= x && tempy <= y && y <= tempy + height && x <= tempX + width;
    }
    public boolean mouseDragged(double mouseX, double mouseY, double dragX, double dragY) {
        if (is((int) mouseX, (int) mouseY)){
            this.setOffsetX1((int) (this.getOffsetX1() + dragX));
            this.setOffsetY1((int) (this.getOffsetY1() + dragY));
            return  true;
        }
        return false;
    }
    public void setOffsetX1(final int offsetX1) {
        this.offsetX1 = offsetX1;
    }

    public void setOffsetY1(final int offsetY1) {
        this.offsetY1 = offsetY1;
    }
    public int getOffsetX1() {
        return offsetX1;
    }

    public int getOffsetY1() {
        return offsetY1;
    }
    public boolean mouseScrolled(double mouseX, double mouseY, ScrollDirection direction) {
        return false;
    }
    public boolean mouseClicked(final int mouseX, final int mouseY) {
        return false;
    }
}