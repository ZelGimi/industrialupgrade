package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerElectricElectronicsAssembler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiElectricElectronicsAssembler<T extends ContainerElectricElectronicsAssembler> extends GuiIU<ContainerElectricElectronicsAssembler> {

    public final ContainerElectricElectronicsAssembler container;

    public GuiElectricElectronicsAssembler(
            ContainerElectricElectronicsAssembler container1
    ) {
        super(container1);
        this.container = container1;
        this.addComponent(new GuiComponent(this, 127, 52, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 7, 62, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));

        this.addComponent(new GuiComponent(this, 94, 19, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container.base, 1, 300) {
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
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
