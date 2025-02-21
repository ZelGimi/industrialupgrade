package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerCapacitor;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiCapacitor extends GuiIU<ContainerCapacitor> {

    public GuiCapacitor(ContainerCapacitor guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 187;
        this.ySize = 212;
        this.addComponent(new GuiComponent(this, 81, 57 - 26, 22, 22,
                new Component<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 81, 57 + 22, 22, 22,
                new Component<>(new ComponentButton(this.container.base, 1) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getX()), 52, 57 + 4,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                )
        );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 81, this.guiTop + 55, 188, 3, 23, 23);

        this.drawTexturedModalRect(this.guiLeft + 81, this.guiTop + 57 - 26, 188, 43, 22, 22);
        this.drawTexturedModalRect(this.guiLeft + 40, this.guiTop + 57, 197, 26, 30, 15);
        this.drawTexturedModalRect(this.guiLeft + 81, this.guiTop + 57 + 22, 211, 43, 22, 22);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigraphite4.png");
    }

}
