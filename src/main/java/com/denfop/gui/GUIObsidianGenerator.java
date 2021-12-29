package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerObsidianGenerator;
import ic2.core.GuiIC2;
import ic2.core.gui.TankGauge;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIObsidianGenerator extends GuiIC2<ContainerObsidianGenerator> {

    public final ContainerObsidianGenerator container;

    public GUIObsidianGenerator(ContainerObsidianGenerator container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

        TankGauge.createNormal(this, 40, 8, container.base.fluidTank1).drawForeground(par1, par2);
        TankGauge.createNormal(this, 64, 8, container.base.fluidTank2).drawForeground(par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (16 * this.container.base.getProgress());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 25, yoffset + 57 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 101, yoffset + 34, 176, 32, progress, 16);
        }
        TankGauge.createNormal(this, 40, 8, container.base.fluidTank1).drawBackground(xoffset, yoffset);
        TankGauge.createNormal(this, 64, 8, container.base.fluidTank2).drawBackground(xoffset, yoffset);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 6, name, 4210752, false);

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIObsidianGenerator.png");
    }

}
