package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerPump;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPump extends GuiIU<ContainerPump> {

    public final ContainerPump container;

    public GuiPump(ContainerPump container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        TankGauge.createNormal(this, 70, 16, container.base.fluidTank).drawForeground(par1, par2);

        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 10, 28, 17, 40)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (24.0F * this.container.base.guiProgress);
        if (chargeLevel > 0) {
            this.drawTexturedModalRect(xoffset + 7, yoffset + 42 - chargeLevel, 176, 14 - chargeLevel, 14, chargeLevel);
        }

        if (progress > 0) {
            this.drawTexturedModalRect(xoffset + 36, yoffset + 34, 176, 14, progress + 1, 16);
        }
        TankGauge.createNormal(this, 70, 16, container.base.fluidTank).drawBackground(xoffset, yoffset);


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiPump.png");
    }

}
