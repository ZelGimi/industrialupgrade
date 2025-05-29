package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerRodManufacturer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiRodManufacturer<T extends ContainerRodManufacturer> extends GuiIU<ContainerRodManufacturer> {

    private final GuiComponent component;
    ContainerRodManufacturer container;

    public GuiRodManufacturer(ContainerRodManufacturer guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        componentList.clear();
        this.invSlotList.add(container.base.outputSlot);
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.component = new GuiComponent(this, 117, 59, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy));
        this.addComponent(new GuiComponent(this, 80, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(container.base, 1,
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
        super.drawForegroundLayer(poseStack,par1, par2);
        this.component.drawForeground(poseStack,par1, par2);

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
