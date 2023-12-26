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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import static com.denfop.utils.ModUtils.getUnit;

public class GuiRadiationPurifier extends GuiIU<ContainerRadiationPurifier> {

    public GuiRadiationPurifier(ContainerRadiationPurifier guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 8, 62, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }
    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        if (this.container.base.radiation != null) {
            new Area(this, 13, 11, 28, 10).withTooltip(Localization.translate("radiation.dose") + Math.max(
                    1,
                    (int) this.container.base.radiation.getRadiation()
            ) + " " + getUnit(this.container.base.radiation.getCoef()) + "Sv").drawForeground(par1
                    , par2);
        }else{
            new Area(this,13, 11, 28, 10).withTooltip(Localization.translate("radiation.dose") + Math.max(
                    0.5,
                    0
            ) + " " + getUnit(EnumCoefficient.NANO) + "Sv").drawForeground(par1
                    , par2);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        if (this.container.base.radiation == null) {
            this.drawTexturedRect(41, 12, 1, 3, 180, 19);
        } else {
            Radiation radiation = this.container.base.radiation;
            if (radiation.getLevel() == EnumLevelRadiation.LOW) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(41 - translate, 12,1, 3, 180,  19);
            } else if (radiation.getLevel() == EnumLevelRadiation.DEFAULT) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(41 - 6 - translate, 12, 1, 3, 180, 19);
            } else if (radiation.getLevel() == EnumLevelRadiation.MEDIUM) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(41 - 12 - translate, 12, 1, 3, 180, 19);
            } else if (radiation.getLevel() == EnumLevelRadiation.HIGH) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(41 - 18 - translate, 12, 1, 3, 180, 19);
            } else if (radiation.getLevel() == EnumLevelRadiation.VERY_HIGH) {
                final double translate = (6 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(41 - 24 - translate, 12, 1, 3, 180, 19);
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guipurifier.png");
    }

}
