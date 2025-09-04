package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuRodFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenRodFactory<T extends ContainerMenuRodFactory> extends ScreenMain<ContainerMenuRodFactory> {

    public ScreenRodFactory(ContainerMenuRodFactory guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 70, 36, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        this.addComponent(new ScreenWidget(this, 80, 60, EnumTypeComponent.LEFT,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "-1") {
                    @Override
                    public void ClickEvent() {

                        super.ClickEvent();

                    }
                })
        ));
        this.addComponent(new ScreenWidget(this, 81 + EnumTypeComponent.RIGHT.getWeight() + 10, 60, EnumTypeComponent.RIGHT,
                new WidgetDefault<>(new ComponentButton(this.container.base, 1, "+1") {
                    @Override
                    public void ClickEvent() {

                        super.ClickEvent();

                    }
                })
        ));
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        draw(poseStack, String.valueOf(this.container.base.type), 92, 65, 4210752);
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
