package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerSteamTurbinePressure;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiSteamTurbinePressure<T extends ContainerSteamTurbinePressure> extends GuiIU<ContainerSteamTurbinePressure> {

    public GuiSteamTurbinePressure(ContainerSteamTurbinePressure guiContainer) {
        super(guiContainer);
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addComponent(new GuiComponent(this, 83, 57 - 36, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 83, 54, EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 1) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));
    }



    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        this.font.draw(poseStack,String.valueOf(this.container.base.getPressure()), 87, 40, ModUtils.convertRGBcolorToInt(15,
                125, 205
        ));
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
