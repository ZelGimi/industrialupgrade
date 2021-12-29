package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerCombinerMatter;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.gui.TankGauge;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GUICombinerMatter extends GuiIC2<ContainerCombinerMatter> {

    public final ContainerCombinerMatter container;


    public GUICombinerMatter(ContainerCombinerMatter container1) {
        super(container1);
        this.container = container1;
    }


    private void handleUpgradeTooltip(int x, int y, int minX, int minY, int maxX, int maxY) {
        if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.combMatterinformation"));
            List<String> compatibleUpgrades = getInformation();
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(x, y, text);
        }
    }

    private static List<String> getInformation() {
        List<String> ret = new ArrayList();
        ret.add(Localization.translate("iu.combMatterinformation1"));
        ret.add(Localization.translate("iu.combMatterinformation2"));


        return ret;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


        TankGauge.createNormal(this, 96, 22, container.base.fluidTank).drawForeground(par1, par2);
        handleUpgradeTooltip(par1 - this.guiLeft, par2 - this.guiTop, 165, 0, 175, 12);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xoffset + 165, yoffset, 0, 0, 10, 10);

        this.mc.getTextureManager().bindTexture(getTexture());
        TankGauge.createNormal(this, 96, 22, container.base.fluidTank).drawBackground(xoffset, yoffset);

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUICombineMatter.png");

    }

}
