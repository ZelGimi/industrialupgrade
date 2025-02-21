package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerGraphiteController;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiGraphiteGraphiteController extends GuiIU<ContainerGraphiteController> {

    public GuiGraphiteGraphiteController(ContainerGraphiteController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 187;
        this.ySize = 212;
        this.addComponent(new GuiComponent(this, 83, 70 - 26, 22, 22,
                new Component<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 83, 70 + 20, 22, 22,
                new Component<>(new ComponentButton(this.container.base, 1) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(Localization.translate("reactor.level_graphite") + this.container.base.getLevelGraphite(),
                23 - 6
                , 10,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                )
        );
        this.fontRenderer.drawString(Localization.translate("reactor.level_fuel_graphite") + ModUtils.getString(this.container.base.getFuelGraphite()),
                23 - 6
                , 20,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                )
        );
        this.fontRenderer.drawString(Localization.translate("reactor.index_graphite") + this.container.base.getIndex(),
                23 - 6, 30,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                )
        );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 67, 188, 3, 23, 23);

        this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 70 - 26, 188, 43, 22, 22);
        this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 70 + 20, 211, 43, 22, 22);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigraphite4.png");
    }

}
