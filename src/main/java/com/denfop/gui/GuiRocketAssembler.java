package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageScreen;
import com.denfop.api.gui.ImageSpaceInterface;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBattery;
import com.denfop.container.ContainerProbeAssembler;
import com.denfop.container.ContainerRocketAssembler;
import com.denfop.container.ContainerRoverAssembler;
import com.denfop.container.ContainerSatelliteAssembler;
import net.minecraft.util.ResourceLocation;

public class GuiRocketAssembler extends GuiIU<ContainerRocketAssembler> {

    public GuiRocketAssembler(ContainerRocketAssembler guiContainer) {
        super(guiContainer, EnumTypeStyle.SPACE);
        this.xSize+=240-178;
        this.ySize=255;
        this.inventory.addY(50+18+30-8);
        this.inventory.setX(7+210-178-16);
        this.elements.add(new ImageSpaceInterface(this, 0, 0, this.xSize, this.ySize));
        this.addComponent(new GuiComponent(this, 176+18, 90, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 176-50, 64, EnumTypeComponent.PROCESS,
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
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
