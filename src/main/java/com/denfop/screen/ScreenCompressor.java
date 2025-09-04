package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuCompressor;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenCompressor<T extends ContainerMenuCompressor> extends ScreenMain<ContainerMenuCompressor> {

    public ScreenCompressor(ContainerMenuCompressor guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 186;
        this.imageHeight = 211;
        this.addComponent(new ScreenWidget(this, 83, 57 - 26, 22, 22,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new ScreenWidget(this, 83, 57 + 20, 22, 22,
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
        draw(poseStack, String.valueOf(this.container.base.getPressure()), 93, 60, ModUtils.convertRGBcolorToInt(15,
                125, 205
        ));
    }


    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 83, this.guiTop + 57 - 26, 188, 43, 22, 22);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 80, this.guiTop + 57, 197, 26, 30, 15);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 83, this.guiTop + 57 + 20, 211, 43, 22, 22);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigasreactor5.png");
    }

}
