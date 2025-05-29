package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.ImageScreen;
import com.denfop.container.ContainerController;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public class GuiEnergyController<T extends ContainerController> extends GuiIU<ContainerController> {

    public final ContainerController container;

    public GuiEnergyController(ContainerController container1) {
        super(container1);
        this.container = container1;
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
        this.addElement(new ImageScreen(this, 7, 32, 65, 15));
    }


    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack,this.imageWidth / 2, 4, Component.nullToEmpty(name), 4210752, false);
    }




    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer( poseStack, par1, par2);

       draw( poseStack, Localization.translate("iu.energy_controller_info") + (this.container.base.size), 11, 36,
                2157374
        );

    }




    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
