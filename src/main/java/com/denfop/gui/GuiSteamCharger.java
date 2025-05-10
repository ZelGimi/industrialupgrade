package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.SteamImageInterface;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSteamCharger;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiSteamCharger<T extends ContainerSteamCharger> extends GuiIU<ContainerSteamCharger> {

    public final ContainerSteamCharger container;

    public GuiSteamCharger(ContainerSteamCharger container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;

        this.addElement(new SteamImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addComponent(new GuiComponent(this, 56, 62, EnumTypeComponent.STEAM_ENERGY_HEIGHT,
                new Component<>(this.container.base.ampere)
        ));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.STEAM_FLUID,
                new Component<>(this.container.base.steam)
        ));
        this.addComponent(new GuiComponent(this, 85, 45, EnumTypeComponent.STEAM_PROCESS,
                new Component<>(new ComponentProgress(this.container.base, 1,
                        (short) 0
                ) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
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

protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);

        handleUpgradeTooltip(par1, par2);
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack,float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack,float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack,f, x, y);
      bindTexture(getTexture());


        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack,this.imageWidth / 2 + 15, 5, name, 4210752, false);
       bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack,3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
