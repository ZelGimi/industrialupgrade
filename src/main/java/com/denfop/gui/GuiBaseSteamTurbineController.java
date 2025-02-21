package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerBaseSteamTurbineController;
import com.denfop.container.ContainerGasTank;
import com.denfop.container.ContainerSteamTurbineTank;
import com.denfop.tiles.mechanism.steamturbine.controller.TileEntityBaseSteamTurbineController;
import com.denfop.tiles.mechanism.triple.heat.TileAdvAlloySmelter;
import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiBaseSteamTurbineController extends GuiIU<ContainerBaseSteamTurbineController> {

    public GuiBaseSteamTurbineController(ContainerBaseSteamTurbineController guiContainer) {
        super(guiContainer);
        this.xSize+=40;
        this.ySize+=20;
        this.inventory.addY(20);
        this.inventory.setX(this.inventory.getX()+20);
        this.addElement(new ImageInterface(this,0,0,this.xSize,this.ySize));
        elements.add(TankGauge.createNormal(this, 10, 5, guiContainer.base.getWaterFluid()));
        elements.add(TankGauge.createNormal(this, 40, 5, guiContainer.base.getSteamFluid()));

        for (int i = 0; i < guiContainer.base.listCoolant.size();i++){
            elements.add(TankGauge.createNormal(this, 155+30*i, 5, guiContainer.base.listCoolant.get(i).getCoolant()));
        }
        this.addComponent(new GuiComponent(this, 88, 15, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy.getEnergy())));
        this.componentList.add(new GuiComponent(this, 98, 80, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityBaseSteamTurbineController) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityBaseSteamTurbineController) this.getEntityBlock()).work;
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 155, 65, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 1) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 193, 65 ,  EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 2) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));
        this.addElement(new Area(this,70,6,13,74).withTooltip(() -> "Phase: " + String.valueOf(this.container.base.phase) + "\n" + "Stage: " + String.valueOf(this.container.base.enumSteamPhase.name())+
                "\n" + "Generate: " + ModUtils.getString(this.container.base.generation) + " EF/t"+ "\n" +
                "Heat: " + ModUtils.getString(this.container.base.heat)));
    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.steam_turbine.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 19; i++) {
                compatibleUpgrades.add(Localization.translate("iu.steam_turbine.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX-147, mouseY-20, text);
        }
    }
    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.stableenumSteamPhase.name()), 170, 80,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                ));

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor.png"));

        double bar = this.container.base.phase / 230D;
        bar = Math.min(1, bar);
        drawTexturedModalRect(this.guiLeft + 70, (int) (this.guiTop + 6)
                , 202, 11, 13, 74);
        drawTexturedModalRect(this.guiLeft + 72, (int) (this.guiTop + 73+6 - (bar * 71))
                , 228, (int) (72 - (bar * 71)), 9, (int) (bar * 71));
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
