package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerSiliconCrystalHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiSiliconCrystalHandler<T extends ContainerSiliconCrystalHandler> extends GuiIU<ContainerSiliconCrystalHandler> {

    public GuiSiliconCrystalHandler(ContainerSiliconCrystalHandler guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.timer)
        ));
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer( poseStack, par1, par2);
        poseStack.pushPose();
        poseStack.scale(0.7f, 0.7f, 0.7f);
        this.font.draw(poseStack,
                Localization.translate("iu.coal_concentration") + " " + (int) ((this.container.base.col / 90D) * 100) + "%",
                10,
                30,
                4210752
        );

        poseStack.scale(1 / 0.7f, 1 / 0.7f, 1 / 0.7f);
        poseStack.popPose();
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
