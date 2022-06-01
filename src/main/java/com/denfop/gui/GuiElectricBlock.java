package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerElectricBlock;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.gui.Area;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiElectricBlock extends GuiIC2<ContainerElectricBlock> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/GUIElectricBlockEuRf.png"
    );
    private final ContainerElectricBlock container;
    private final String armorInv;
    private final String name;

    public GuiElectricBlock(ContainerElectricBlock container1) {
        super(container1);
        this.ySize = 196;
        this.container = container1;
        this.armorInv = Localization.translate("ic2.EUStorage.gui.info.armor");

        this.name = Localization.translate(container.base.getName());
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 110 && x <= 130 && y >= 34 && y <= 50) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    @Override
    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6,
                4210752
        );
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.fontRenderer.drawString(this.armorInv, 8, this.ySize - 126 + 3, 4210752);
        String tooltip =
                "EU: " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(this.container.base.energy.getCapacity());
        String tooltip1 = "RF: " + ModUtils.getString(this.container.base.energy2) + "/" + ModUtils.getString(this.container.base.maxStorage2);
        new Area(this, 85 - 3, 38, 108 - 82, 46 - 38).withTooltip(tooltip).drawForeground(par1, par2);
        new Area(this, 133, 38, 158 - 133, 46 - 38).withTooltip(tooltip1).drawForeground(par1, par2);


        String output = Localization.translate(
                "ic2.EUStorage.gui.info.output",
                ModUtils.getString(this.container.base.getOutput())
        );
        this.fontRenderer.drawString(output, 85, 70, 4210752);
        this.fontRenderer.drawString(
                TextFormatting.BOLD + "" + TextFormatting.AQUA + Localization.translate("button.rg"),
                118,
                38,
                4210752
        );

        String tooltip3 = Localization.translate("inforamtionelectricstorage");
        new Area(this, 110, 34, 132 - 110, 51 - 34).withTooltip(tooltip3).drawForeground(xOffset, yOffset);


        handleUpgradeTooltip(par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        drawTexturedModalRect(j + 3, k + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(background);
        if (this.container.base.energy.getEnergy() > 0.0D) {
            int i1 = (int) (24.0F * this.container.base.getChargeLevel());
            drawTexturedModalRect(j + 79 + 6 - 2 - 1, k + 34, 176, 14, i1 + 1, 16);
        }
        if (this.container.base.energy2 > 0.0D) {

            int i1 = (int) (24.0F * this.container.base.getChargeLevel1());
            drawTexturedModalRect(j + 79 + 54 + 2, k + 34, 176, 31, i1 + 1, 16);
        }
        if (this.container.base.rf) {
            if (this.container.base.rfeu) {
                drawTexturedModalRect(j + 110, k + 22, 176, 62, 21, 9);

            } else {
                drawTexturedModalRect(j + 110, k + 22, 176, 51, 21, 9);

            }
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.electricstorageinformation"));
            List<String> compatibleUpgrades = ListInformationUtils.storageinform;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

}
