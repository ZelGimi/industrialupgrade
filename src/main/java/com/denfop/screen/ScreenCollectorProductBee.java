package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuCollectorProductBee;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenCollectorProductBee<T extends ContainerMenuCollectorProductBee> extends ScreenMain<ContainerMenuCollectorProductBee> {

    public ScreenCollectorProductBee(ContainerMenuCollectorProductBee guiContainer) {
        super(guiContainer);
        this.imageHeight += 40;
        this.inventory.setY(this.inventory.getY() + 40);
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addWidget(TankWidget.createNormal(this, 23, 21, container.base.tank));
        this.addWidget(TankWidget.createNormal(this, 120, 21, container.base.tank1));

        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
