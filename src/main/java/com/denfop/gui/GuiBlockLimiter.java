package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageScreen;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiBlockLimiter extends GuiIU<ContainerBlockLimiter> {


    public GuiBlockLimiter(ContainerBlockLimiter guiContainer) {
        super(guiContainer);
        for (int m = 0; m < 6; m++) {
            final int finalM = m;
            this.componentList.add(new GuiComponent(this, 10 + m * 15, 50, EnumTypeComponent.PLUS_BUTTON,
                    new Component<>(new ComponentButton(this.container.base, 0, "") {
                        @Override
                        public String getText() {
                            int mod = 1;
                            mod = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? mod * 10 : mod;
                            mod = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? mod * 10 : mod;
                            return "+" + ModUtils.getString(Math.pow(10, finalM) * mod);
                        }

                        @Override
                        public void ClickEvent() {
                            int mod = 1;
                            mod = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? mod * 10 : mod;
                            mod = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? mod * 10 : mod;
                            new PacketUpdateServerTile(getEntityBlock(), Math.pow(10, finalM) * mod);
                        }
                    })
            ));
        }
        for (int m = 0; m < 6; m++) {
            final int finalM = m;
            this.componentList.add(new GuiComponent(this, 10 + m * 15, 65, EnumTypeComponent.MINUS_BUTTON,
                    new Component<>(new ComponentButton(this.container.base, 0, "") {
                        @Override
                        public String getText() {
                            int mod = 1;
                            mod = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? mod * 10 : mod;
                            mod = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? mod * 10 : mod;

                            return "-" + ModUtils.getString(Math.pow(10, finalM) * mod);
                        }

                        @Override
                        public void ClickEvent() {
                            int mod = 1;
                            mod = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? mod * 10 : mod;
                            mod = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? mod * 10 : mod;
                            new PacketUpdateServerTile(getEntityBlock(), -1 * Math.pow(10, finalM) * mod);
                        }

                    })
            ));
        }
        this.addElement(new ImageScreen(this, 10, 25, 86, 18));

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

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        this.fontRenderer.drawString(TextFormatting.GREEN + ModUtils.getString(this.container.base.getEnergy().limit_amount),
                (64 - getStringWidth(ModUtils.getString(this.container.base.getEnergy().limit_amount))), 31,
                ModUtils.convertRGBcolorToInt(217, 217, 217)
        );


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
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
