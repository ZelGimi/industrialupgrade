package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuRoverAssembler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenRoverAssembler<T extends ContainerMenuRoverAssembler> extends ScreenMain<ContainerMenuRoverAssembler> {

    public ScreenRoverAssembler(ContainerMenuRoverAssembler guiContainer) {
        super(guiContainer, EnumTypeStyle.SPACE);
        this.imageWidth = 232;
        this.imageHeight = 213;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 198, 86, EnumTypeComponent.ENERGY_WEIGHT_2,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 170, 53, EnumTypeComponent.SPACE_PROGRESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirover_assembler.png");
    }

}
