package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuCombinerSE;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ScreenCombinerSE<T extends ContainerMenuCombinerSE> extends ScreenMain<ContainerMenuCombinerSE> {

    public final ContainerMenuCombinerSE container;

    public ScreenCombinerSE(ContainerMenuCombinerSE container1) {
        super(container1);
        this.container = container1;
        this.imageHeight = 200;
        componentList.clear();
        inventory = new ScreenWidget(this, 7, 119, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new ScreenWidget(this, 95, 100, EnumTypeComponent.SOLARIUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.sunenergy)
        ));
    }


    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);

        if (this.container.base != null) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }

    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        draw(poseStack, Localization.translate("gui.SuperSolarPanel.generating") +
                        ": " + (int) this.container.base.generation + Localization.translate("iu.machines_work_energy_type_se"),
                9, 105, ModUtils.convertRGBcolorToInt(0, 0, 0)
        );
    }


    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 162 && mouseX <= 172 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.solar_generator_info"));
            List<String> compatibleUpgrades = ListInformationUtils.solar;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
