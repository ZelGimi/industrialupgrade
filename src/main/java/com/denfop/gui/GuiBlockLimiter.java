package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiBlockLimiter extends GuiIU<ContainerBlockLimiter> {


    public GuiBlockLimiter(ContainerBlockLimiter guiContainer) {
        super(guiContainer);


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
        int mod = 1;
        mod = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? mod * 10 : mod;
        mod = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? mod * 10 : mod;
        for (int m = 0; m < 6; m++) {
            if (x >= 10 + m * 15 && x <= 20 + m * 15 && y >= 57 && y <= 67) {
                new PacketUpdateServerTile(this.container.base, Math.pow(10, m) * mod);
            }
        }
        for (int m = 0; m < 6; m++) {
            if (x >= 10 + m * 15 && x <= 20 + m * 15 && y >= 70 && y <= 80) {
                new PacketUpdateServerTile(this.container.base, -1 * Math.pow(10, m) * mod);
            }
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getEnergy().limit_amount),
                (42 - String.valueOf(this.container.base.getEnergy().limit_amount).length()) / 2, 20,
                ModUtils.convertRGBcolorToInt(221, 221, 221)
        );
        int mod = 1;
        mod = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? mod * 10 : mod;
        mod = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? mod * 10 : mod;
        for (int m = 0; m < 6; m++) {
            new AdvArea(this, 10 + m * 15, 57, 20 + m * 15, 67)
                    .withTooltip("+" + ModUtils.getString(Math.pow(10, m) * mod))
                    .drawForeground(
                            par1,
                            par2
                    );
            new AdvArea(this, 10 + m * 15, 70, 20 + m * 15, 80)
                    .withTooltip("-" + ModUtils.getString(Math.pow(10, m) * mod))
                    .drawForeground(par1, par2);

        }


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xoffset + 3, yoffset + 3, 0, 0, 10, 10);

        this.mc.getTextureManager().bindTexture(getTexture());
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guilimiter.png");
    }

}
