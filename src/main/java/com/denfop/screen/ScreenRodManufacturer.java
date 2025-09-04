package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuRodManufacturer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenRodManufacturer<T extends ContainerMenuRodManufacturer> extends ScreenMain<ContainerMenuRodManufacturer> {

    private final ScreenWidget component;
    ContainerMenuRodManufacturer container;

    public ScreenRodManufacturer(ContainerMenuRodManufacturer guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        componentList.clear();
        this.invSlotList.add(container.base.outputSlot);
        inventory = new ScreenWidget(this, 7, 83, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.component = new ScreenWidget(this, 117, 59, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy));
        this.addComponent(new ScreenWidget(this, 80, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(container.base, 1,
                        (short) this.container.base.defaultOperationLength
                ) {
                    @Override
                    public short getMaxValue() {
                        return (short) container.base.operationLength;
                    }

                    @Override
                    public double getBar() {
                        return container.base.guiProgress;
                    }
                })
        ));
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        this.component.drawForeground(poseStack, par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(getTexture());
        int progress = (int) (24.0F * this.container.base.getProgress());
        int xoffset = guiLeft;
        int yoffset = guiTop;
        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        this.component.drawBackground(poseStack, xoffset, yoffset);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
