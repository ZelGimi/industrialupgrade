package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuSolarPanels1;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenSolarPanels1<Y extends ContainerMenuSolarPanels1> extends ScreenMain<ContainerMenuSolarPanels1> implements ButtonListSliderWidget.GuiResponder, SliderWidget.FormatHelper {
    public final BlockEntitySolarPanel tileentity;
    private SliderWidget slider;

    public ScreenSolarPanels1(ContainerMenuSolarPanels1 containerSolarPanels1) {
        super(containerSolarPanels1);
        this.componentList.clear();
        this.tileentity = containerSolarPanels1.tileentity;
        this.imageWidth = 198;
        this.imageHeight = 232;
        this.componentList.add(new ScreenWidget(this, 50, 95, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(new Energy(tileentity, tileentity.debtMax) {
                    @Override
                    public double getFillRatio() {
                        return Math.min(1, tileentity.debt / tileentity.debtMax);
                    }

                    @Override
                    public double getEnergy() {
                        return tileentity.debt;
                    }

                    @Override
                    public double getCapacity() {
                        return tileentity.debtMax;
                    }
                })
        ));
    }


    @Override
    public void init() {
        super.init();
        slider = new SliderWidget(this, 0, (this.width - this.imageWidth) / 2 + 33, (this.height - this.imageHeight) / 2 + 84,
                "",
                (float) -50, (float) 100, (float) this.tileentity.deptPercent, this, 78
        );

        this.addWidget(slider);
        this.addRenderableWidget(slider);
    }

    @Override
    public String getText(final int var1, final String var2, final float var3) {
        return String.valueOf((int) tileentity.deptPercent);
    }

    @Override
    public void setEntryValue(final int i, final boolean b) {

    }

    @Override
    public void setEntryValue(final int i, final float v) {
        new PacketUpdateServerTile(this.tileentity, v);

    }

    @Override
    public void setEntryValue(final int i, final String s) {

    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int mouseX, int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        if (mouseX + 17 >= 0 && mouseX <= 12 + 17 && mouseY >= 41 && mouseY <= 12 + 41) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.panel_upgrade.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 7; i++) {
                compatibleUpgrades.add(Localization.translate("iu.panel_upgrade.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 60, mouseY, text);
        }
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft + 17, this.guiTop + 41, 0, 0, 10, 10);

    }

    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisolarpanel1.png");
    }

}
