package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuSiliconCrystalHandler;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenSiliconCrystalHandler<T extends ContainerMenuSiliconCrystalHandler> extends ScreenMain<ContainerMenuSiliconCrystalHandler> {

    public ScreenSiliconCrystalHandler(ContainerMenuSiliconCrystalHandler guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 70, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.timer)
        ));
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
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
