package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerHeatLimiter;
import com.denfop.utils.ListInformationUtils;
import ic2.core.IC2;
import ic2.core.gui.GuiElement;
import ic2.core.gui.TextBox;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiHeatLimiter extends GuiIU<ContainerHeatLimiter> {

    private final TextBox textBox;

    public GuiHeatLimiter(ContainerHeatLimiter guiContainer) {
        super(guiContainer);
        this.textBox = new TextBox(this, 60, 15, 60, 15, String.valueOf(guiContainer.base.limit));

        this.addElement(textBox);

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            List<String> compatibleUpgrades = ListInformationUtils.heat_limiter;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 60 && x <= 120 && y >= 32 && y <= 44) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, -1);
        }
        if (x >= 60 && x <= 120 && y >= 46 && y <= 58) {
            try {
                IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, Math.min(
                        10000,
                        Math.max(0, Integer.parseInt(textBox.getText()))
                ));
            } catch (Exception ignored) {
            }
        }
        if (x >= 60 && x <= 120 && y >= 60 && y <= 72) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 10001);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(Localization.translate("button.update_info"), 66, 34, 4210752);
        this.fontRenderer.drawString(Localization.translate("button.delete_info"), 66, 62, 4210752);
        this.fontRenderer.drawString(Localization.translate("button.write"), 66, 48, 4210752);
        handleUpgradeTooltip(par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xoffset + 3, yoffset + 3, 0, 0, 10, 10);

        this.mc.getTextureManager().bindTexture(getTexture());
        this.drawBackground();

        for (final GuiElement<?> element : this.elements) {
            if (element.isEnabled()) {
                element.drawBackground(xoffset, yoffset);
            }
        }

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheatlimiter.png");
    }

}
