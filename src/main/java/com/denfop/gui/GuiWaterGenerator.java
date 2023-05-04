package com.denfop.gui;


import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerWaterGenerator;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWaterGenerator extends GuiIC2<ContainerWaterGenerator> {

    public final ContainerWaterGenerator container;
    public final String progressLabel;
    public final String amplifierLabel;

    public GuiWaterGenerator(ContainerWaterGenerator container1) {
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


    }


    public String getName() {
        return this.container.base.getName();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI.png");


    }

}
