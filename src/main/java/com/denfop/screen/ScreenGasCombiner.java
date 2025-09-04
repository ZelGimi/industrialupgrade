package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuGasCombiner;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class ScreenGasCombiner<T extends ContainerMenuGasCombiner> extends ScreenMain<ContainerMenuGasCombiner> {

    public final ContainerMenuGasCombiner container;

    public ScreenGasCombiner(ContainerMenuGasCombiner container1) {
        super(container1);
        this.container = container1;
        this.imageHeight += 40;
        this.inventory.setY(this.inventory.getY() + 40);
        this.addComponent(new ScreenWidget(this, 5, 30, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addWidget(TankWidget.createNormal(this, 43, 21, container.base.fluidTank1));
        this.addWidget(TankWidget.createNormal(this, 66, 21, container.base.fluidTank2));
        this.addWidget(TankWidget.createNormal(this, 120, 21, container.base.fluidTank3));
        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 92, 45, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(this.container.base, 1,
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
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(getTexture());

        int progress = (int) (32 * this.container.base.getProgress());
        int xoffset = guiLeft;
        int yoffset = guiTop;

        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 88, yoffset + 40, 177, 41, progress, 19);
        }
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2 + 15, 5, net.minecraft.network.chat.Component.nullToEmpty(name), 4210752, false);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
