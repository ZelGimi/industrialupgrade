package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ButtonWidget;
import com.denfop.containermenu.ContainerMenuSubstitute;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public class ScreenEnergySubstitute<T extends ContainerMenuSubstitute> extends ScreenMain<ContainerMenuSubstitute> {

    public final ContainerMenuSubstitute container;

    public ScreenEnergySubstitute(ContainerMenuSubstitute container1) {
        super(container1);
        this.container = container1;
        this.imageHeight = 200;
        this.inventory.setX(7);
        this.inventory.setY(119);
        this.addWidget(new ButtonWidget(
                this,
                83,
                21,
                88,
                15,
                container1.base,
                0,
                Localization.translate("button.find_energypaths")
        ));
        this.addWidget(new ButtonWidget(this, 83, 40, 88, 15, container1.base, 1, Localization.translate("button" +
                ".set_value_energypaths")));

    }


    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2, 4, Component.nullToEmpty(name), 4210752, false);
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
