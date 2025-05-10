package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSteamPressureConverter;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiSteamPressureConverter<T extends ContainerSteamPressureConverter> extends GuiIU<ContainerSteamPressureConverter> {

    public ContainerSteamPressureConverter container;
    public String name;

    public GuiSteamPressureConverter(ContainerSteamPressureConverter guiContainer) {
        super(guiContainer, EnumTypeStyle.STEAM);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());

        this.componentList.add(new GuiComponent(this, 72, 41, EnumTypeComponent.STEAM_PROCESS,
                new Component<>(new ComponentEmpty())
        ));
        this.addElement(TankGauge.createNormal(this, 46, 22, (container.base).fluidTank));
        this.componentList.add(new GuiComponent(this, 96, 24, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return "+1";
                    }

                })
        ));
        this.componentList.add(new GuiComponent(this, 96, 56, EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 1, "") {
                    @Override
                    public String getText() {
                        return "-1";
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
        super.drawForegroundLayer(poseStack, par1, par2);
        this.font.draw(poseStack, String.valueOf(this.container.base.maxpressure), 100, 43,
                4210752
        );
        handleUpgradeTooltip(par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer( poseStack,f, x, y);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());



        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect( poseStack,3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteam_machine.png");

    }


}
