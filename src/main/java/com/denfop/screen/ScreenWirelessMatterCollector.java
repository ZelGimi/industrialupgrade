package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentValue;
import com.denfop.containermenu.ContainerMenuWirelessMatterCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenWirelessMatterCollector<T extends ContainerMenuWirelessMatterCollector> extends ScreenMain<ContainerMenuWirelessMatterCollector> {

    public ScreenWirelessMatterCollector(ContainerMenuWirelessMatterCollector guiContainer) {
        super(guiContainer);
        this.addWidget(TankWidget.createNormal(this, 96, 23, guiContainer.base.getFluidTank()));
        this.addComponent(new ScreenWidget(this, 122, 40, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.getEnergy())
        ));
        this.addComponent(new ScreenWidget(this, 36, 34, EnumTypeComponent.CIRCLE_BAR,
                new WidgetDefault<>(new ComponentValue<Integer>().setValue(() -> guiContainer.base.getFilled(32))
                )
        ));
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        String percent = this.container.base.getIntegerPercentage() + "%";
        int nmPos2 = (this.imageWidth - this.getStringWidth(percent)) / 2;
        this.font.draw(poseStack, percent, nmPos2 - 35, 46, 4210752);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
