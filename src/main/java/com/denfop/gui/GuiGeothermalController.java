package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerGeothermalController;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantController;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalController;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiGeothermalController extends GuiIU<ContainerGeothermalController> {
    private boolean hover = false;
    public GuiGeothermalController(ContainerGeothermalController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.componentList.add(new GuiComponent(this, 70, 35, 24,
                24,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityGeothermalController) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityGeothermalController) this.getEntityBlock()).work;
                    }
                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("guide.geothermalpump"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 4; i++) {
                compatibleUpgrades.add(Localization.translate("guide.geothermalpump" + i));
            }
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

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);

        handleUpgradeTooltip(par1, par2);
        if (!this.container.base.work) {
            this.hover = par1 >= 70 && par1 <= 70+24 && par2 >= 35 && par2 <= 35+24;
        } else {
            this.hover = false;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();
        drawTexturedModalRect(this.guiLeft + 70,
                this.guiTop + 35
                , 231, 1, 24, 24
        );
        if (this.container.base.work) {
            drawTexturedModalRect(this.guiLeft + 70,
                    this.guiTop + 35
                    , 231, 51, 24, 24
            );
        }
        if (this.hover) {
            drawTexturedModalRect(this.guiLeft + 70,
                    this.guiTop + 35
                    , 231, 26, 24, 24
            );
        }
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 10, 10);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigeothermalpump.png");
    }

}
