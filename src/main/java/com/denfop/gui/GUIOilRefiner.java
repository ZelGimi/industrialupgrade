package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerOilRefiner;
import ic2.core.GuiIC2;
import ic2.core.gui.TankGauge;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIOilRefiner extends GuiIC2<ContainerOilRefiner> {

    public ContainerOilRefiner container;

    private static final ResourceLocation background;

    public GUIOilRefiner(ContainerOilRefiner container1) {
        super(container1);
        this.container = container1;

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        TankGauge.createNormal(this, 12, 6, container.base.fluidTank[0]).drawForeground(par1, par2);
        TankGauge.createNormal(this, 74, 6, container.base.fluidTank[1]).drawForeground(par1, par2);
        TankGauge.createNormal(this, 106, 6, container.base.fluidTank[2]).drawForeground(par1, par2);


    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(background);
        int energy = (int) ((this.container.base.energy.getEnergy() / this.container.base.energy.getCapacity()) * 29);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        energy = Math.min(energy, 29);
        this.drawTexturedModalRect(xOffset + 39, yOffset + 69, 177, 104, energy, 9);
        TankGauge.createNormal(this, 12, 6, container.base.fluidTank[0]).drawBackground(xOffset, yOffset);
        TankGauge.createNormal(this, 74, 6, container.base.fluidTank[1]).drawBackground(xOffset, yOffset);
        TankGauge.createNormal(this, 106, 6, container.base.fluidTank[2]).drawBackground(xOffset, yOffset);


    }

    static {
        background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIOilRefiner1.png");
    }
}
