package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.container.ContainerInoculator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiInoculator<T extends ContainerInoculator> extends GuiIU<ContainerInoculator> {

    public GuiInoculator(ContainerInoculator guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 71, 34, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
    }



    @Override
protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
        if (this.container.base instanceof IUpgradableBlock) {
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack,3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle( poseStack,partialTicks, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
