package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerQuantumQuarry;
import com.denfop.utils.ListInformation;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GUIQuantumQuarry extends GuiIC2<ContainerQuantumQuarry> {

    public final ContainerQuantumQuarry container;

    public GUIQuantumQuarry(ContainerQuantumQuarry container1) {
        super(container1);
        this.container = container1;
    }


    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.quarryinformation"));
            List<String> compatibleUpgrades = ListInformation.quarryinform;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        drawTexturedModalRect(h + 3, k + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (48.0F * this.container.base.energy.getEnergy()
                / this.container.base.energy.getCapacity());

        if (chargeLevel > 0) {
            drawTexturedModalRect(h + 140 + 1 + 5, k + 28 + 48 - chargeLevel, 176,
                    48 - chargeLevel, 48, chargeLevel
            );
        }

        this.drawString(fontRenderer,
                "" + ModUtils.getString(this.container.base.getblock),
                h + 151, k + 7, 4210752
        );

    }


    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIQuantumQuerry.png");
    }

}
