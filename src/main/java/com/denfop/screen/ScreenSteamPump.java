package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuSteamPump;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenSteamPump<T extends ContainerMenuSteamPump> extends ScreenMain<ContainerMenuSteamPump> {

    public final ContainerMenuSteamPump container;

    public ScreenSteamPump(ContainerMenuSteamPump container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;
        this.addWidget(TankWidget.createNormal(this, 70, 16, container.base.fluidTank));
        this.addComponent(new ScreenWidget(this, 36, 35, EnumTypeComponent.STEAM_PROCESS2,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));

        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.STEAM_FLUID,
                new WidgetDefault<>(this.container.base.steam)
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
        super.drawForegroundLayer(poseStack, par1, par2);

        handleUpgradeTooltip(par1, par2);
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteam_machine.png");
    }

}
