package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerNeutronSeparator;
import net.minecraft.util.ResourceLocation;

public class GuiNeutronSeparator extends GuiIU<ContainerNeutronSeparator> {

    public GuiNeutronSeparator(ContainerNeutronSeparator guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 60, 55, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.qe)
        ));
        this.addComponent(new GuiComponent(this, 72, 36, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.getContainer().base, 1, 100) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
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
