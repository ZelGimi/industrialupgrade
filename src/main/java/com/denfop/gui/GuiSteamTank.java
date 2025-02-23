package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSteamTank;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSteamTank extends GuiIU<ContainerSteamTank> {

    public final ContainerSteamTank container;

    public GuiSteamTank(ContainerSteamTank container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;

    }

    protected void drawForegroundLayer(int par1, int par2) {

        super.drawForegroundLayer(par1, par2);
        TankGauge.createNormal(this, 96, 22, container.base.getFluidTank()).drawForeground(par1, par2);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        TankGauge.createNormal(this, 96, 22, container.base.getFluidTank()).drawBackground(xOffset, yOffset);


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guisteam_machine.png");
    }

}
