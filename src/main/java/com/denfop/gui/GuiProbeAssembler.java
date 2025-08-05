package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerProbeAssembler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiProbeAssembler<T extends ContainerProbeAssembler> extends GuiIU<ContainerProbeAssembler> {

    public GuiProbeAssembler(ContainerProbeAssembler guiContainer) {
        super(guiContainer, EnumTypeStyle.SPACE);
        this.imageWidth = 214;
        this.imageHeight = 225;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 180, 92, EnumTypeComponent.ENERGY_WEIGHT_2,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 152, 60, EnumTypeComponent.SPACE_PROGRESS,
                new Component<>(this.container.base.componentProgress)
        ));
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiprobe_assembler.png");
    }

}
