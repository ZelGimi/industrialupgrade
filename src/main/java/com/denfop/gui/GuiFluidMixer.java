package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerFluidMixer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;


public class GuiFluidMixer<T extends ContainerFluidMixer> extends GuiIU<ContainerFluidMixer> {

    public final ContainerFluidMixer container;

    public GuiFluidMixer(ContainerFluidMixer container1) {
        super(container1);
        this.container = container1;
        this.imageHeight += 40;
        this.inventory.setY(this.inventory.getY() + 40);
        this.addComponent(new GuiComponent(this, 5, 30, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addElement(TankGauge.createNormal(this, 23, 21, container.base.fluidTank1));
        this.addElement(TankGauge.createNormal(this, 46, 21, container.base.fluidTank2));
        this.addElement(TankGauge.createNormal(this, 97, 21, container.base.fluidTank3));
        this.addElement(TankGauge.createNormal(this, 120, 21, container.base.fluidTank4));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 72, 45, EnumTypeComponent.PROCESS,
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


    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        if (this.container.base instanceof IUpgradableBlock) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
       bindTexture(getTexture());

        int progress = (int) (32 * this.container.base.getProgress());
        int xoffset = guiLeft;
        int yoffset =guiTop;

        if (progress > 0) {
            drawTexturedModalRect(poseStack,xoffset + 88, yoffset + 40, 177, 41, progress, 19);
        }
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack,this.imageWidth / 2 + 15, 5, name, 4210752, false);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
