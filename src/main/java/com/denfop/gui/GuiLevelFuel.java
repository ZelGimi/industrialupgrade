package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TanksGauge;
import com.denfop.container.ContainerLevelFuel;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiLevelFuel<T extends ContainerLevelFuel> extends GuiIU<ContainerLevelFuel> {

    public GuiLevelFuel(ContainerLevelFuel guiContainer) {
        super(guiContainer);
        this.imageWidth = 255;
        this.componentList.clear();
        this.elements.add(TanksGauge.createNormal(this, 7, 7, guiContainer.base.getFluidTankInputList()));
        this.elements.add(TanksGauge.createNormal(this, 30, 7, guiContainer.base.getFluidTankCoolantList()));

        this.elements.add(TanksGauge.createNormal(this, 55, 7, guiContainer.base.getFluidTankOutputList()));
        this.elements.add(TanksGauge.createNormal(this, 78, 7, guiContainer.base.getFluidTankHotCoolantList()));
        this.addComponent(new GuiComponent(this, 35, 63, EnumTypeComponent.RAD,
                new Component<>(this.container.base.getRad())
        ));
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        draw(poseStack, Localization.translate("effect.industrialupgrade.radiation") + ": " + ModUtils.getString(
                this.container.base.getReactor().getRadGeneration()) + "☢", 100, 7, ModUtils.convertRGBcolorToInt(19, 40, 73));
        draw(poseStack,Localization.translate("gui.iu.tier") + ": " + ModUtils.getString(
                this.container.base.getLevelReactor()), 100, 19, ModUtils.convertRGBcolorToInt(19, 40, 73));
        draw(poseStack,this.container.base.getLevelReactor() < this.container.base.getMaxLevelReactor() ?
                        Localization.translate("reactor.canupgrade") : Localization.translate("reactor.notcanupgrade"), 100, 31,
                ModUtils.convertRGBcolorToInt(19, 40, 73)
        );
        draw(poseStack,Localization.translate("iu.minipanel.output") + ModUtils.getString(
                        this.container.base.output), 100, 43,
                ModUtils.convertRGBcolorToInt(19, 40, 73)
        );
        draw(poseStack,Localization.translate("gui.SuperSolarPanel.generating") + ": " + ModUtils.getString(
                this.container.base.output) + " EF/t", 100, 56, ModUtils.convertRGBcolorToInt(19, 40, 73));

    }


    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor5.png");
    }

}
