package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerTank;
import ic2.core.GuiIC2;
import ic2.core.gui.TankGauge;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTank extends GuiIC2<ContainerTank> {

    public final ContainerTank container;

    public GuiTank(ContainerTank container1) {
        super(container1);
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

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiTank.png");
    }

}
