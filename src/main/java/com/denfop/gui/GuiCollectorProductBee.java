package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.*;
import com.denfop.container.ContainerCollectorProductBee;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiCollectorProductBee<T extends ContainerCollectorProductBee> extends GuiIU<ContainerCollectorProductBee> {

    public GuiCollectorProductBee(ContainerCollectorProductBee guiContainer) {
        super(guiContainer);
        this.imageHeight += 40;
        this.inventory.setY(this.inventory.getY() + 40);
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addElement(TankGauge.createNormal(this, 23, 21, container.base.tank));
        this.addElement(TankGauge.createNormal(this, 120, 21, container.base.tank1));

        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
