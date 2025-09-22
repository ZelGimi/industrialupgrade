package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerNightConverter;
import net.minecraft.util.ResourceLocation;

public class GuiNightConverter extends GuiIU<ContainerNightConverter> {

    public GuiNightConverter(ContainerNightConverter guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 120, 35, EnumTypeComponent.NIGHT_ENERGY_WEIGHT,
                new Component<>(this.container.base.ne)
        ));
        this.addComponent(new GuiComponent(this, 71, 35, EnumTypeComponent.NIGHT_PROCESS,
                new Component<>(this.container.base.progress)
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
