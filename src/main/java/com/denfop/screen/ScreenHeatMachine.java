package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.blockentity.base.BlockEntityBaseHeatMachine;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuHeatMachine;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenHeatMachine<T extends ContainerMenuHeatMachine> extends ScreenMain<ContainerMenuHeatMachine> {

    public ContainerMenuHeatMachine container;
    public String name;

    public ScreenHeatMachine(ContainerMenuHeatMachine guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());

        if (this.container.base.hasFluid) {
            this.componentList.add(new ScreenWidget(this, 117, 41, EnumTypeComponent.FLUID_PART,
                    new WidgetDefault<>(new EmptyWidget())
            ));
            this.addWidget(TankWidget.createNormal(this, 96, 22, (container.base).fluidTank));
        } else {
            this.componentList.add(new ScreenWidget(this, 113, 21, EnumTypeComponent.ENERGY_HEIGHT,
                    new WidgetDefault<>(this.container.base.energy)
            ));
        }
        this.componentList.add(new ScreenWidget(this, 51, 41, EnumTypeComponent.HEAT,
                new WidgetDefault<>(this.container.base.heat)
        ));
        this.componentList.add(new ScreenWidget(this, 20, 37, EnumTypeComponent.WORK_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 2, "") {
                    @Override
                    public String getText() {
                        return ((BlockEntityBaseHeatMachine) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((BlockEntityBaseHeatMachine) this.getEntityBlock()).work;
                    }
                })
        ));
        this.componentList.add(new ScreenWidget(this, 53, 60, EnumTypeComponent.PLUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return "+1000";
                    }

                })
        ));
        this.componentList.add(new ScreenWidget(this, 78, 60, EnumTypeComponent.MINUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 1, "") {
                    @Override
                    public String getText() {
                        return "-1000";
                    }

                })
        ));
    }


    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.heat_storage.info_main"));
            List<String> compatibleUpgrades = ListInformationUtils.heating;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
        bindTexture(getTexture());


    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");

    }


}
