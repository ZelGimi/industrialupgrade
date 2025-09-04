package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ImageScreenWidget;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuBlockLimiter;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenBlockLimiter<T extends ContainerMenuBlockLimiter> extends ScreenMain<ContainerMenuBlockLimiter> {


    public ScreenBlockLimiter(ContainerMenuBlockLimiter guiContainer) {
        super(guiContainer);
        for (int m = 0; m < 6; m++) {
            final int finalM = m;
            this.componentList.add(new ScreenWidget(this, 10 + m * 15, 50, EnumTypeComponent.PLUS_BUTTON,
                    new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
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
            this.componentList.add(new ScreenWidget(this, 10 + m * 15, 65, EnumTypeComponent.MINUS_BUTTON,
                    new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
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
        this.addWidget(new ImageScreenWidget(this, 10, 25, 86, 18));

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
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);

    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);
        poseStack.drawString(Minecraft.getInstance().font, ChatFormatting.GREEN + ModUtils.getString(this.container.base.getEnergy().limit_amount),
                (64 - getStringWidth(ModUtils.getString(this.container.base.getEnergy().limit_amount))), 31,
                ModUtils.convertRGBcolorToInt(217, 217, 217), false
        );


    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        int xoffset = guiLeft;
        int yoffset = guiTop;
        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, xoffset + 3, yoffset + 3, 0, 0, 10, 10);

        bindTexture(getTexture());
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
