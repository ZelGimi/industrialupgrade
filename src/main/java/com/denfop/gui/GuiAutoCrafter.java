package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerAutoCrafter;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

public class GuiAutoCrafter extends GuiIU<ContainerAutoCrafter> {

    public GuiAutoCrafter(ContainerAutoCrafter guiContainer) {
        super(guiContainer);
        this.ySize = 200;
        componentList.clear();
        inventory = new GuiComponent(this, 7, 119, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
        this.addComponent(new GuiComponent(this, 102, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        if (this.container.base.getRecipe() != null) {
            new Area(this, 74, 52, 18, 18)
                    .withTooltip(() -> this.container.base.getRecipe().output.items.get(0).getDisplayName())
                    .drawForeground(par1, par2);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if (this.container.base.getRecipe() != null) {
            RenderHelper.enableGUIStandardItemLighting();
            this.drawItemStack(74, 52, this.container.base.getRecipe().output.items.get(0));
            RenderHelper.disableStandardItemLighting();
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
