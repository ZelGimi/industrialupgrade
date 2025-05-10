package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSatelliteAssembler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiSatelliteAssembler<T extends ContainerSatelliteAssembler> extends GuiIU<ContainerSatelliteAssembler> {

    public GuiSatelliteAssembler(ContainerSatelliteAssembler guiContainer) {
        super(guiContainer, EnumTypeStyle.SPACE);
        this.imageWidth = 214;
        this.imageHeight = 226;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 173, 63, EnumTypeComponent.ENERGY_WEIGHT_2,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 143, 31, EnumTypeComponent.SPACE_PROGRESS,
                new Component<>(this.container.base.componentProgress)
        ));
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
      bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack,3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect( poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisatellite_assembler.png");
    }

}
