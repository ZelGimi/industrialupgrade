package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSteamPeatGenerator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiSteamPeatGenerator<T extends ContainerSteamPeatGenerator> extends GuiIU<ContainerSteamPeatGenerator> {


    public ContainerSteamPeatGenerator container;
    public String name;

    public GuiSteamPeatGenerator(ContainerSteamPeatGenerator container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;
        this.name = Localization.translate(this.container.base.getName());
        this.addComponent(new GuiComponent(this, 25, 36, EnumTypeComponent.FIRE,
                new Component<>(new ComponentProgress(this.container.base, 1, (short) 1) {
                    @Override
                    public double getBar() {
                        return container.base.gaugeFuelScaled(12) / 12D;
                    }

                })
        ));
        this.addComponent(new GuiComponent(this, 86, 36, EnumTypeComponent.STEAM_ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.componentList.add(new GuiComponent(this, 72, 41, EnumTypeComponent.STEAM_PROCESS,
                new Component<>(new ComponentEmpty())
        ));
        this.addElement(TankGauge.createNormal(this, 46, 22, (container.base).fluidTank));
        this.addElement(TankGauge.createNormal(this, 96, 22, (container.base).fluidTank1));

    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        handleUpgradeTooltip(par1, par2);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("steam_machine.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                compatibleUpgrades.add(Localization.translate("steam_machine.info" + i));
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

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteam_machine.png");
    }

protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
       bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }

}
