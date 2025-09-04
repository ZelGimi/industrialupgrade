package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuCapacitor;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenCapacitor<T extends ContainerMenuCapacitor> extends ScreenMain<ContainerMenuCapacitor> {

    public ScreenCapacitor(ContainerMenuCapacitor guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 187;
        this.imageHeight = 212;
        this.addComponent(new ScreenWidget(this, 81, 57 - 26, 22, 22,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new ScreenWidget(this, 81, 57 + 22, 22, 22,
                new WidgetDefault<>(new ComponentButton(this.container.base, 1) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));
    }


    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        poseStack.drawString(Minecraft.getInstance().font, String.valueOf(this.container.base.getX()), 52, 57 + 4,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                ), false
        );
    }


    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 81, this.guiTop + 55, 188, 3, 23, 23);

        this.drawTexturedModalRect(poseStack, this.guiLeft + 81, this.guiTop + 57 - 26, 188, 43, 22, 22);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 40, this.guiTop + 57, 197, 26, 30, 15);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 81, this.guiTop + 57 + 22, 211, 43, 22, 22);

    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guigraphite4.png");
    }

}
