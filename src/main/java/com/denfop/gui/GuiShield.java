package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerShield;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiShield<T extends ContainerShield> extends GuiIU<ContainerShield> {

    public GuiShield(ContainerShield guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 50, EnumTypeComponent.ACCEPT,
                new Component<>(new ComponentButton(guiContainer.base, 1, ""))
        ));

        this.componentList.add(new GuiComponent(this, 60, 50, EnumTypeComponent.CANCEL,
                new Component<>(new ComponentButton(guiContainer.base, -2, ""))
        ));
        this.componentList.add(new GuiComponent(this, 100, 50, EnumTypeComponent.ACCEPT,
                new Component<>(new ComponentButton(guiContainer.base, 0, ""))
        ));

        this.componentList.add(new GuiComponent(this, 130, 50, EnumTypeComponent.CANCEL,
                new Component<>(new ComponentButton(guiContainer.base, -1, ""))
        ));

        this.componentList.add(new GuiComponent(this, 10, 66, EnumTypeComponent.WHITE,
                new Component<>(new ComponentButton(guiContainer.base, 3, ""))
        ));
        this.componentList.add(new GuiComponent(this, 10, 50, EnumTypeComponent.BLACK,
                new Component<>(new ComponentButton(guiContainer.base, 2, ""))
        ));
    }


    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
     draw(poseStack, Localization.translate("iu.shield" + (container.base.visibleShield ? ".visible" :
                        ".invisible")),
                10, 42, 0
        );
       draw(poseStack, Localization.translate("iu.laser" + (container.base.visibleLaser ? ".visible" :
                        ".invisible")),
                93, 42, 0
        );
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
