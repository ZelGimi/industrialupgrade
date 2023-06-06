package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerCombinerSolidMatter;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;


public class GuiCombinerSolidMatter extends GuiIC2<ContainerCombinerSolidMatter> {

    public final ContainerCombinerSolidMatter container;

    public GuiCombinerSolidMatter(ContainerCombinerSolidMatter container1) {
        super(container1);
        this.container = container1;
    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        this.mc.getTextureManager().bindTexture(getTexture());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xoffset, yoffset, 0, 0, this.xSize, this.ySize);
        if (this.container.base != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        this.mc.getTextureManager().bindTexture(getTexture());


    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        if (this.container.base.energy.getCapacity() != 0) {
            this.fontRenderer.drawString(
                    Localization.translate("gui.MolecularTransformer.progress") + ": " + ModUtils.getString(
                            this.container.base.energy.getEnergy() * 100D / this.container.base.energy.getCapacity()) + "%",
                    +58,
                    +70,
                    4210752
            );
        }
        new AdvArea(
                this,
                58,
                70,
                107,
                77
        ).withTooltip("EU: " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(
                this.container.base.energy.getCapacity())).drawForeground(par1, par2);
    }

    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICombineSolidMatter.png");
    }

}
