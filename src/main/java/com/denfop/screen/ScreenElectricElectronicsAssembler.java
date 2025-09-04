package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuElectricElectronicsAssembler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenElectricElectronicsAssembler<T extends ContainerMenuElectricElectronicsAssembler> extends ScreenMain<ContainerMenuElectricElectronicsAssembler> {

    public final ContainerMenuElectricElectronicsAssembler container;

    public ScreenElectricElectronicsAssembler(
            ContainerMenuElectricElectronicsAssembler container1
    ) {
        super(container1);
        this.container = container1;
        this.addComponent(new ScreenWidget(this, 127, 52, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 7, 62, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));

        this.addComponent(new ScreenWidget(this, 94, 19, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(this.container.base, 1, 300) {
                    @Override
                    public double getBar() {
                        return container.base.componentProgress.getBar();
                    }
                })
        ));
    }


    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        this.drawTexturedModalRect(poseStack, this.guiLeft - 22, this.guiTop + 82, 8, 7, 20, 20);
    }


    public String getName() {
        return "";
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
