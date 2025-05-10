package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.radiationsystem.EnumCoefficient;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.container.ContainerRadiationPurifier;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.denfop.utils.ModUtils.getUnit;

public class GuiRadiationPurifier<T extends ContainerRadiationPurifier> extends GuiIU<ContainerRadiationPurifier> {

    public GuiRadiationPurifier(ContainerRadiationPurifier guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 8, 62, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        handleUpgradeTooltip(par1, par2);
        if (this.container.base.radiation != null) {
            new Area(this, 13, 11, 28, 10).withTooltip(Localization.translate("radiation.dose") + Math.max(
                    1,
                    (int) this.container.base.radiation.getRadiation()
            ) + " " + getUnit(this.container.base.radiation.getCoef()) + "Sv").drawForeground(poseStack,par1
                    , par2);
        } else {
            new Area(this, 13, 11, 28, 10).withTooltip(Localization.translate("radiation.dose") + Math.max(
                    0.5,
                    0
            ) + " " + getUnit(EnumCoefficient.NANO) + "Sv").drawForeground(poseStack,par1
                    , par2);
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.radiationpurifier.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 4; i++) {
                compatibleUpgrades.add(Localization.translate("iu.radiationpurifier.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
    bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack,this.guiLeft + 3, this.guiTop + 3, 0, 0, 10, 10);

      bindTexture(getTexture());
        this.bindTexture();
        if (this.container.base.radiation == null) {
            this.drawTexturedRect(poseStack,41, 12, 1, 3, 180, 19);
        } else {
            Radiation radiation = this.container.base.radiation;
            if (radiation.getLevel() == EnumLevelRadiation.LOW) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(poseStack,41 - translate, 12, 1, 3, 180, 19);
            } else if (radiation.getLevel() == EnumLevelRadiation.DEFAULT) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(poseStack,41 - 6 - translate, 12, 1, 3, 180, 19);
            } else if (radiation.getLevel() == EnumLevelRadiation.MEDIUM) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(poseStack,41 - 12 - translate, 12, 1, 3, 180, 19);
            } else if (radiation.getLevel() == EnumLevelRadiation.HIGH) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(poseStack,41 - 18 - translate, 12, 1, 3, 180, 19);
            } else if (radiation.getLevel() == EnumLevelRadiation.VERY_HIGH) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(poseStack,41 - 24 - translate, 12, 1, 3, 180, 19);
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guipurifier.png");
    }

}
