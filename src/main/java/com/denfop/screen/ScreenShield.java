package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuShield;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenShield<T extends ContainerMenuShield> extends ScreenMain<ContainerMenuShield> {

    public ScreenShield(ContainerMenuShield guiContainer) {
        super(guiContainer);
        this.componentList.add(new ScreenWidget(this, 30, 50, EnumTypeComponent.ACCEPT,
                new WidgetDefault<>(new ComponentButton(guiContainer.base, 1, ""))
        ));

        this.componentList.add(new ScreenWidget(this, 60, 50, EnumTypeComponent.CANCEL,
                new WidgetDefault<>(new ComponentButton(guiContainer.base, -2, ""))
        ));
        this.componentList.add(new ScreenWidget(this, 100, 50, EnumTypeComponent.ACCEPT,
                new WidgetDefault<>(new ComponentButton(guiContainer.base, 0, ""))
        ));

        this.componentList.add(new ScreenWidget(this, 130, 50, EnumTypeComponent.CANCEL,
                new WidgetDefault<>(new ComponentButton(guiContainer.base, -1, ""))
        ));

        this.componentList.add(new ScreenWidget(this, 10, 66, EnumTypeComponent.WHITE,
                new WidgetDefault<>(new ComponentButton(guiContainer.base, 3, ""))
        ));
        this.componentList.add(new ScreenWidget(this, 10, 50, EnumTypeComponent.BLACK,
                new WidgetDefault<>(new ComponentButton(guiContainer.base, 2, ""))
        ));
    }


    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        draw(poseStack, Localization.translate("iu.shield" + (container.base.visibleShield ? ".visible" :
                        ".invisible")),
                10, 42, 0
        );
        draw(poseStack, Localization.translate("iu.laser" + (container.base.visibleLaser ? ".visible" :
                        ".invisible")),
                93, 42, 0
        );
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
