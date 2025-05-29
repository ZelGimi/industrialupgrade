package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentValue;
import com.denfop.container.ContainerWirelessMatterCollector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiWirelessMatterCollector<T extends ContainerWirelessMatterCollector> extends GuiIU<ContainerWirelessMatterCollector> {

    public GuiWirelessMatterCollector(ContainerWirelessMatterCollector guiContainer) {
        super(guiContainer);
        this.addElement(TankGauge.createNormal(this, 96, 23, guiContainer.base.getFluidTank()));
        this.addComponent(new GuiComponent(this, 122, 40, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.getEnergy())
        ));
        this.addComponent(new GuiComponent(this, 36, 34, EnumTypeComponent.CIRCLE_BAR,
                new Component<>(new ComponentValue<Integer>().setValue(() -> guiContainer.base.getFilled(32))
                )
        ));
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        String percent = this.container.base.getIntegerPercentage() + "%";
        int nmPos2 = (this.imageWidth - this.getStringWidth(percent)) / 2;
        draw(poseStack,percent, nmPos2 - 35, 46, 4210752);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }




}
