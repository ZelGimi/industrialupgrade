package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerSteamTurbineCoolant;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

public class GuiSteamTurbineCoolant extends GuiIU<ContainerSteamTurbineCoolant> {

    public GuiSteamTurbineCoolant(ContainerSteamTurbineCoolant guiContainer) {
        super(guiContainer);
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        elements.add(TankGauge.createNormal(this, 80, 25, guiContainer.base.getCoolant()));
        this.addComponent(new GuiComponent(this, 13, 57 - 36, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 13, 54, EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 1) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getPressure()), 17, 40, ModUtils.convertRGBcolorToInt(15,
                125, 205
        ));
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
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
