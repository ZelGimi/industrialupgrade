package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerCombinerSE;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.gui.Area;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GuiCombinerSE extends GuiIC2<ContainerCombinerSE> {

    public final ContainerCombinerSE container;

    public GuiCombinerSE(ContainerCombinerSE container1) {
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
        this.drawTexturedRect(163.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        String tooltip = "SE: " + ModUtils.getString(this.container.base.sunenergy.getEnergy());
        new Area(this, 123, 38, 146 - 123, 46 - 38).withTooltip(tooltip).drawForeground(mouseX, mouseY);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        fontRenderer.drawString(Localization.translate("gui.SuperSolarPanel.generating") +
                        ": " + (int) this.container.base.generation + Localization.translate("iu.machines_work_energy_type_se"),
                3, 71, ModUtils.convertRGBcolorToInt(0, 0, 0)
        );
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 162 && mouseX <= 172 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.solar_generator_info"));
            List<String> compatibleUpgrades = ListInformationUtils.solar;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicombinese.png");
    }

}
