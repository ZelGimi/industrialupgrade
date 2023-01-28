package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.utils.ListInformationUtils;
import ic2.core.IC2;
import ic2.core.gui.TextBox;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiBlockLimiter extends GuiIU<ContainerBlockLimiter> {

    private final TextBox textBox;

    public GuiBlockLimiter(ContainerBlockLimiter guiContainer) {
        super(guiContainer);
        this.textBox = new TextBox(this, 5, 15, 80, 20, String.valueOf(guiContainer.base.getEnergy().limit_amount));

        this.addElement(textBox);

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.limiter.info"));
            List<String> compatibleUpgrades = ListInformationUtils.limiter_info;
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
        if (x >= 12 && x <= 46 && y >= 36 && y <= 48) {
            try {
                IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, Integer.parseInt(textBox.getText()));
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(Localization.translate("button.write"), 14, 38, 4210752);
        handleUpgradeTooltip(par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xoffset + 3, yoffset + 3, 0, 0, 10, 10);

        this.mc.getTextureManager().bindTexture(getTexture());
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guilimiter.png");
    }

}
