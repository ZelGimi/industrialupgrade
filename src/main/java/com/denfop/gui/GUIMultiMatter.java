package com.denfop.gui;


import com.denfop.Constants;
import com.denfop.container.ContainerMultiMatter;
import ic2.core.GuiIC2;
import ic2.core.gui.Area;
import ic2.core.gui.TankGauge;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIMultiMatter extends GuiIC2<ContainerMultiMatter> {

    public final ContainerMultiMatter container;
    public final String progressLabel;
    public final String amplifierLabel;

    public GUIMultiMatter(ContainerMultiMatter container1) {
        super(container1);
        this.container = container1;
        this.progressLabel = Localization.translate("ic2.Matter.gui.info.progress");
        this.amplifierLabel = Localization.translate("ic2.Matter.gui.info.amplifier");
        addElement(TankGauge.createNormal(this, 96, 22, container.base.fluidTank));
    }


    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.progressLabel, 8, 22, 4210752);
        this.fontRenderer.drawString(this.container.base.getProgressAsString(), 18, 31, 4210752);
        if ((this.container.base).scrap > 0) {
            this.fontRenderer.drawString(this.amplifierLabel, 8, 46, 4210752);
            this.fontRenderer.drawString("" + (this.container.base).scrap, 8, 58, 4210752);
        }
        FluidStack fluidstack = (this.container.base).fluidTank.getFluid();
        if (fluidstack != null) {
            String tooltip = Localization.translate("ic2.uumatter") + ": " + fluidstack.amount
                    + Localization.translate("ic2.generic.text.mb");
            new Area(this, 99, 25, 112 - 99, 73 - 25).withTooltip(tooltip).drawForeground(par1, par2);

        }
    }


    public String getName() {
        return this.container.base.getName();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIMatter.png");


    }

}
