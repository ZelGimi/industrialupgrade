package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerGasSocket;
import net.minecraft.util.ResourceLocation;

public class GuiGasSocket extends GuiIU<ContainerGasSocket> {

    public GuiGasSocket(ContainerGasSocket guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 72, 40, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.getEnergy())
        ));
        this.xSize = 186;
        this.ySize = 211;
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigasreactor5.png");
    }

}
