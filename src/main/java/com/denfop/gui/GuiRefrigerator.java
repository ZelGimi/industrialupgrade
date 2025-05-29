package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerRefrigerator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiRefrigerator<T extends ContainerRefrigerator> extends GuiIU<ContainerRefrigerator> {

    public GuiRefrigerator(ContainerRefrigerator guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer( poseStack,par1, par2);
        TankGauge.createNormal(this, 12, 20, container.base.tank).drawForeground( poseStack,par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer( poseStack,partialTicks, mouseX, mouseY);

        TankGauge.createNormal(this, 12, 20, container.base.tank).drawBackground( poseStack,guiLeft, guiTop);
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
