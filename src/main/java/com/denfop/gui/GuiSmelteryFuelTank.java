package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerSmelteryCasting;
import com.denfop.container.ContainerSmelteryFuelTank;
import com.denfop.tiles.smeltery.TileEntitySmelteryController;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSmelteryFuelTank extends GuiIU<ContainerSmelteryFuelTank> {

    public GuiSmelteryFuelTank(ContainerSmelteryFuelTank guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.ySize = 181;

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        TankGauge.createNormal(this,this.xSize / 2 - 10,20,this.container.base.getFuelTank()).drawForeground(par1, par2);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        TankGauge.createNormal(this,this.xSize / 2 - 10,20,this.container.base.getFuelTank()).drawBackground(guiLeft, guiTop);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guismeltery.png");
    }

}
