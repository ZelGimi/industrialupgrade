package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerPalletGenerator;
import net.minecraft.util.ResourceLocation;

public class GuiPalletGenerator extends GuiIU<ContainerPalletGenerator> {

    public GuiPalletGenerator(ContainerPalletGenerator guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 58, 63, EnumTypeComponent.RAD_1,
                new Component<>(this.container.base.rad)
        ));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.ENERGY_WEIGHT_1,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft + 49, this.guiTop + 17, 179, 30, 18, 18);
        this.drawTexturedModalRect(this.guiLeft + 49, this.guiTop + 35, 179, 30, 18, 18);
        this.drawTexturedModalRect(this.guiLeft + 67, this.guiTop + 17, 179, 30, 18, 18);
        this.drawTexturedModalRect(this.guiLeft + 67, this.guiTop + 35, 179, 30, 18, 18);
        this.drawTexturedModalRect(this.guiLeft + 85, this.guiTop + 17, 179, 30, 18, 18);
        this.drawTexturedModalRect(this.guiLeft + 85, this.guiTop + 35, 179, 30, 18, 18);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guipallet.png");
    }

}
