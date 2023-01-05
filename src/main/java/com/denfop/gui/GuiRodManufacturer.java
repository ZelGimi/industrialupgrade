package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerRodManufacturer;
import net.minecraft.util.ResourceLocation;

public class GuiRodManufacturer extends GuiIU<ContainerRodManufacturer> {

    private final GuiComponent component;
    ContainerRodManufacturer container;

    public GuiRodManufacturer(ContainerRodManufacturer guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.componentList.clear();
        this.component = new GuiComponent(this, 117, 59, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.component.drawForeground(par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (24.0F * this.container.base.getProgress());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        this.component.drawBackground(xoffset, yoffset);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotorrods.png");
    }

}
