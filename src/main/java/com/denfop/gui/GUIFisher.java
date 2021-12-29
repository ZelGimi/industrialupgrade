package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerFisher;
import com.denfop.utils.ListInformation;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GUIFisher extends GuiIC2<ContainerFisher> {

    public final ContainerFisher container;

    public GUIFisher(ContainerFisher container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString(
                this.getName(),
                (this.xSize - this.fontRenderer.getStringWidth(this.getName())) / 2,
                6,
                4210752
        );

        handleUpgradeTooltip(par1, par2);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.fisherinformation"));
            List<String> compatibleUpgrades = ListInformation.fisherinform;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

        this.mc.getTextureManager().bindTexture(getTexture());
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft + 3, this.guiTop + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (48.0F * this.container.base.energy.getEnergy()
                / this.container.base.energy.getCapacity());
        int progress = (15 * this.container.base.progress / 100);
        if (chargeLevel > 0) {
            drawTexturedModalRect(this.guiLeft + 140 + 1 + 5, this.guiTop + 28 + 48 - chargeLevel, 176,
                    48 - chargeLevel, 48, chargeLevel
            );
        }

        if (progress > 0) {
            drawTexturedModalRect(this.guiLeft + 42, this.guiTop + 46, 177, 48, progress + 1, 13);
        }


    }

    public String getName() {
        return Localization.translate("iu.blockFisher.name");
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIFisher.png");
    }

}
