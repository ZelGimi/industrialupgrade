package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerWaterRotorUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiWaterRotorUpgrade<T extends ContainerWaterRotorUpgrade> extends GuiIU<ContainerWaterRotorUpgrade> {

    public final ContainerWaterRotorUpgrade container;
    public final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guiwaterrotorupgrade.png");
    public final ResourceLocation rotors_gui = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors.png");

    public GuiWaterRotorUpgrade(ContainerWaterRotorUpgrade guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.imageHeight = 206;
        this.elements.add(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        this.componentList.clear();
        inventory = new GuiComponent(this, 7, 123, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );

        componentList.add(inventory);
        componentList.add(slots);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
       bindTexture(background);

        if (!this.container.base.rotor_slot.isEmpty()) {
            int j = guiLeft;
            int k = guiTop;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            setTexture(rotors_gui);
            drawTexturedModalRect(poseStack,j + 71, k + 5, 32 * (this.container.base.getRotor().getIndex() % 8),
                    55 * (this.container.base.getRotor().getIndex() / 8), 32, 54
            );
            drawTexturedModalRect(poseStack,j + 71, k + 57, 32 * (this.container.base.getRotor().getIndex() % 8),
                    55 * (this.container.base.getRotor().getIndex() / 8), 32, 54
            );
            drawTexturedModalRect(poseStack,j + 33, k + 43, 55 * (this.container.base.getRotor().getIndex() % 4),
                    112 + 33 * (this.container.base.getRotor().getIndex() / 4), 54, 32
            );
            drawTexturedModalRect(poseStack,j + 85, k + 43, 55 * (this.container.base.getRotor().getIndex() % 4),
                    112 + 33 * (this.container.base.getRotor().getIndex() / 4), 54, 32
            );
            setTexture(background);
            drawTexturedModalRect(poseStack,j + 77, k + 6, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(poseStack,j + 34, k + 49, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(poseStack,j + 77, k + 49, 212,
                    33, 18, 18
            );
            drawTexturedModalRect(poseStack,j + 120, k + 49, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(poseStack,j + 77, k + 92, 230,
                    33, 18, 18
            );

        }

    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    void setTexture(ResourceLocation resourceLocation) {
        bindTexture(resourceLocation);
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

}
