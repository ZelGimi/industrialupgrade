package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerSafetyDoom;
import net.minecraft.util.ResourceLocation;

public class GuiSafetyDoom extends GuiIU<ContainerSafetyDoom> {

    public GuiSafetyDoom(ContainerSafetyDoom guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 58, 63, EnumTypeComponent.RAD_1,
                new Component<>(this.container.base.rad)
        ));
        this.addComponent(new GuiComponent(this, 8, 62, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisafety.png");
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        if (this.container.base.full) {
            this.drawTexturedModalRect(this.guiLeft + 81, this.guiTop + 33, 192, 64, 16, 16);
        } else {
            this.drawTexturedModalRect(this.guiLeft + 81, this.guiTop + 33, 192, 81, 16, 16);
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

}
