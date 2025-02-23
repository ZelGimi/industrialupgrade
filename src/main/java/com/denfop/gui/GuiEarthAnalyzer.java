package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerEarthAnalyzer;
import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryAnalyzer;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiEarthAnalyzer extends GuiIU<ContainerEarthAnalyzer> {

    public GuiEarthAnalyzer(ContainerEarthAnalyzer guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 30, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityEarthQuarryAnalyzer) this.getEntityBlock()).isAnalyzed() ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityEarthQuarryAnalyzer) this.getEntityBlock()).isAnalyzed();
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
        if (this.container.base.isAnalyzed() && !this.container.base.fullAnalyzed()) {
            this.fontRenderer.drawString(Localization.translate("earth_quarry.analyze"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        } else if (this.container.base.fullAnalyzed() && this.container.base.isAnalyzed()) {
            this.fontRenderer.drawString(Localization.translate("earth_quarry.analyze1"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        } else if (this.container.base.fullAnalyzed() && !this.container.base.isAnalyzed()) {
            this.fontRenderer.drawString(Localization.translate("earth_quarry.full_analyze"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        }
        this.fontRenderer.drawString(Localization.translate("earth_quarry.block_col") + container.base.blockCol, 10, 60,
                ModUtils.convertRGBcolorToInt(56, 56, 56)
        );
        this.fontRenderer.drawString(Localization.translate("earth_quarry.block_ores") + container.base.blockOres, 10, 70,
                ModUtils.convertRGBcolorToInt(56, 56, 56)
        );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
