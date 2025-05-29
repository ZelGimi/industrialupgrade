package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerElectricBioGenerator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class GuiElectricBioGenerator<T extends ContainerElectricBioGenerator> extends GuiIU<ContainerElectricBioGenerator> {

    public final ContainerElectricBioGenerator container;

    public GuiElectricBioGenerator(ContainerElectricBioGenerator container1) {
        super(container1);
        this.container = container1;


        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addElement(TankGauge.createNormal(this, 100, 21, container.base.fluidTank1));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.steam)
        ));

        this.addComponent(new GuiComponent(this, 70, 40, EnumTypeComponent.PROCESS2,
                new Component<>(new ComponentProgress(this.container.base, 1,
                        (short) 0
                ) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
    }



    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect( poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack,float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer( poseStack,f, x, y);
        bindTexture(getTexture());


        int xoffset = guiLeft;
        int yoffset =guiTop;


        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString( poseStack,this.imageWidth / 2 + 15, 5, name, 4210752, false);
       bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect( poseStack,3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
