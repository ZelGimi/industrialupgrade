package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerOilGetter;
import ic2.core.GuiIC2;
import ic2.core.gui.TankGauge;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIOilGetter extends GuiIC2<ContainerOilGetter> {

    public final ContainerOilGetter container;


    public GUIOilGetter(ContainerOilGetter container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawForegroundLayer(int par1, int par2) {


        super.drawForegroundLayer(par1, par2);
        TankGauge.createNormal(this, 96, 22, container.base.fluidTank).drawForeground(par1, par2);

        String tooltip;
        if (!this.container.base.notoil) {


            tooltip = Localization.translate("iu.fluidneft") + ": " + this.container.base.number + "/" + this.container.base.max
                    + Localization.translate("ic2.generic.text.mb");
        } else {
            tooltip = Localization.translate("iu.notfindoil");

        }
        new AdvArea(this, 43, 39, 52, 53).withTooltip(tooltip).drawForeground(par1, par2);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        TankGauge.createNormal(this, 96, 22, container.base.fluidTank).drawBackground(xOffset, yOffset);

        int temp = 0;
        if (this.container.base.max > 0) {
            temp = 14 * this.container.base.number / this.container.base.max;
        }
        temp = Math.min(14, temp);
        this.mc.getTextureManager().bindTexture(getTexture());
        if (temp > 0) {
            drawTexturedModalRect(xOffset + 43, yOffset + 39 + 14 - temp, 177, 130 - temp, 10, temp);

        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIOilGetter.png");
    }

}
