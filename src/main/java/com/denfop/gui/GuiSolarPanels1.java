package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiSlider;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerSolarPanels;
import com.denfop.container.ContainerSolarPanels1;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiSolarPanels1  extends GuiIU<ContainerSolarPanels1> implements GuiPageButtonList.GuiResponder, GuiSlider.FormatHelper{
    public final TileSolarPanel tileentity;
    public GuiSolarPanels1(ContainerSolarPanels1 containerSolarPanels1) {
        super(containerSolarPanels1);
        this.componentList.clear();
        this.tileentity = container.tileentity;
        this.xSize = 198;
        this.ySize = 232;
        this.componentList.add(new GuiComponent(this, 50, 95, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>( new Energy(tileentity,tileentity.debtMax){
                    @Override
                    public double getFillRatio() {
                        return Math.min(1,tileentity.debt / tileentity.debtMax);
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
    protected void actionPerformed(@Nonnull GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton instanceof GuiSlider) {
            GuiSlider slider = (GuiSlider) guibutton;
            this.setEntryValue(guibutton.id,slider.getSliderValue());
        }

    }
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiSlider(this, 0, (this.width - this.xSize) / 2 +33, (this.height - this.ySize) / 2 +84,
                "",
                (float) -50, (float) 100, (float) this.container.base.deptPercent, this,78
        ));
    }
    @Override
    public String getText(final int var1, final String var2, final float var3) {
        return String.valueOf((int)tileentity.deptPercent);
    }

    @Override
    public void setEntryValue(final int i, final boolean b) {

    }

    @Override
    public void setEntryValue(final int i, final float v) {
        new PacketUpdateServerTile(this.container.base, v);

    }

    @Override
    public void setEntryValue(final int i, final String s) {

    }
    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        if (mouseX + 17>= 0 && mouseX <= 12+ 17 && mouseY >= 41 && mouseY <= 12+41) {
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

            this.drawTooltip(mouseX-60, mouseY, text);
        }
    }
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft + 17, this.guiTop+41, 0, 0, 10, 10);

    }
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisolarpanel1.png");
    }

}
