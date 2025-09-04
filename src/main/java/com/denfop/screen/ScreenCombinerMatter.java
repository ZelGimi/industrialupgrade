package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.containermenu.ContainerMenuCombinerMatter;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ScreenCombinerMatter<T extends ContainerMenuCombinerMatter> extends ScreenMain<ContainerMenuCombinerMatter> {

    public final ContainerMenuCombinerMatter container;


    public ScreenCombinerMatter(ContainerMenuCombinerMatter container1) {
        super(container1);
        this.container = container1;
        this.addWidget(TankWidget.createNormal(this, 96, 22, container.base.fluidTank));
    }

    private static List<String> getInformation() {
        List<String> ret = new ArrayList<>();
        ret.add(Localization.translate("iu.combMatterinformation1"));
        ret.add(Localization.translate("iu.combMatterinformation2"));


        return ret;
    }

    private void handleUpgradeTooltip(int x, int y) {
        if (x >= 165 && x <= 175 && y >= 0 && y <= 12) {
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

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1 - this.guiLeft(), par2 - this.guiTop());
    }


    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");

    }

}
