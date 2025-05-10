package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerCentrifuge;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiCentrifuge<T extends ContainerCentrifuge> extends GuiIU<ContainerCentrifuge> {

    public GuiCentrifuge(ContainerCentrifuge guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 71, 34, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
    }

    @Override
protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        this.font.draw(poseStack, "RPM: " + this.container.base.rpm,
                68, 65,
                0
        );

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
