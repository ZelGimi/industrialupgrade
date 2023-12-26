package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerSocket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiSocketFactory extends GuiIU<ContainerSocket> {

    public GuiSocketFactory(ContainerSocket guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 90, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
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
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }
    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }
}
