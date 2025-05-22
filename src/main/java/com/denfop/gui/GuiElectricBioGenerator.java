package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerElectricBioGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElectricBioGenerator extends GuiIU<ContainerElectricBioGenerator> {

    public final ContainerElectricBioGenerator container;

    public GuiElectricBioGenerator(ContainerElectricBioGenerator container1) {
        super(container1);
        this.container = container1;


        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        this.addElement(TankGauge.createNormal(this, 100, 21, container.base.fluidTank1));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.steam)
        ));

        this.addComponent(new GuiComponent(this, 70, 40, EnumTypeComponent.PROCESS2,
                new Component<>(new ComponentProgress(this.container.base, 1,
                        (short) 0
                ) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());


        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;


        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 + 15, 5, name, 4210752, false);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
