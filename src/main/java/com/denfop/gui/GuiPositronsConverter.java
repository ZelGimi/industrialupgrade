package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerPositronsConverter;
import net.minecraft.util.ResourceLocation;

public class GuiPositronsConverter extends GuiIU<ContainerPositronsConverter> {

    public GuiPositronsConverter(ContainerPositronsConverter guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 27, 48, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.qe)
        ));
        this.addComponent(new GuiComponent(this, 7, 25, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 80, 25, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
        this.addComponent(new GuiComponent(this, 105, 22, EnumTypeComponent.POSITRONS,
                new Component<>(this.container.base.positrons)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
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

}
