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
import com.denfop.container.ContainerRoverAssembler;
import net.minecraft.util.ResourceLocation;

public class GuiProbeAssembler extends GuiIU<ContainerProbeAssembler> {

    public GuiProbeAssembler(ContainerProbeAssembler guiContainer) {
        super(guiContainer, EnumTypeStyle.SPACE);
        this.xSize=214;
        this.ySize=225;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 180, 92, EnumTypeComponent.ENERGY_WEIGHT_2,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 152, 60, EnumTypeComponent.SPACE_PROGRESS,
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
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiprobe_assembler.png");
    }

}
