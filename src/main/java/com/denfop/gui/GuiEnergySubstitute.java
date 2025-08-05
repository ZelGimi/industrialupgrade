package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerSubstitute;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public class GuiEnergySubstitute<T extends ContainerSubstitute> extends GuiIU<ContainerSubstitute> {

    public final ContainerSubstitute container;

    public GuiEnergySubstitute(ContainerSubstitute container1) {
        super(container1);
        this.container = container1;
        this.imageHeight = 200;
        this.inventory.setX(7);
        this.inventory.setY(119);
        this.addElement(new CustomButton(
                this,
                83,
                21,
                88,
                15,
                container1.base,
                0,
                Localization.translate("button.find_energypaths")
        ));
        this.addElement(new CustomButton(this, 83, 40, 88, 15, container1.base, 1, Localization.translate("button" +
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
