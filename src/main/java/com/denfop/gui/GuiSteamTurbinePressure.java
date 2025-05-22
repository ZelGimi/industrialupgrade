package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerSteamTurbinePressure;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiSteamTurbinePressure extends GuiIU<ContainerSteamTurbinePressure> {

    public GuiSteamTurbinePressure(ContainerSteamTurbinePressure guiContainer) {
        super(guiContainer);
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        this.addComponent(new GuiComponent(this, 83, 57 - 36, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 83, 54, EnumTypeComponent.MINUS_BUTTON,
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
        this.fontRenderer.drawString(String.valueOf(this.container.base.getPressure()), 87, 40, ModUtils.convertRGBcolorToInt(15,
                125, 205
        ));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
