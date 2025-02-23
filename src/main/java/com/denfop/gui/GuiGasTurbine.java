package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerChemicalController;
import com.denfop.container.ContainerGasTurbineController;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantController;
import com.denfop.tiles.gasturbine.TileEntityGasTurbineController;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiGasTurbine extends GuiIU<ContainerGasTurbineController> {

    public GuiGasTurbine(ContainerGasTurbineController guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 45, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityGasTurbineController) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityGasTurbineController) this.getEntityBlock()).work;
                    }
                })
        ));
        this.addElement(TankGauge.createNormal(this, this.xSize / 2 - 10, 20, guiContainer.base.tank.getTank()));
        this.addComponent(new GuiComponent(this, 120, 30, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy.getEnergy())
        ));
        this.addComponent(new GuiComponent(this, 99, 40, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("quarry.guide.gasturbine"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 4; i++) {
                compatibleUpgrades.add(Localization.translate("quarry.guide.gasturbine" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX-120, mouseY, text);
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
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 10, 10);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
