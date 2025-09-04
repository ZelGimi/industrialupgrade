package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuAutoCrafter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenAutoCrafter<T extends ContainerMenuAutoCrafter> extends ScreenMain<ContainerMenuAutoCrafter> {

    public ScreenAutoCrafter(ContainerMenuAutoCrafter guiContainer) {
        super(guiContainer);
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
        this.addComponent(new ScreenWidget(this, 70, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        this.addComponent(new ScreenWidget(this, 102, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        if (this.container.base.getRecipe() != null) {
            new TooltipWidget(this, 74, 52, 18, 18)
                    .withTooltip(() -> this.container.base.getRecipe().output.items.get(0).getDisplayName().getString())
                    .drawForeground(poseStack, par1, par2);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        if (this.container.base.getRecipe() != null) {
            this.drawItemStack(poseStack, 74, 52, this.container.base.getRecipe().output.items.get(0));
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
