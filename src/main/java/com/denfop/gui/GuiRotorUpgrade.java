package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerRotorUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiRotorUpgrade<T extends ContainerRotorUpgrade> extends GuiIU<ContainerRotorUpgrade> {

    public final ContainerRotorUpgrade container;
    public final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guiwind_rotor_upgrade.png");
    public final ResourceLocation rotors_gui = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors.png");

    public GuiRotorUpgrade(ContainerRotorUpgrade guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.componentList.clear();

    }



    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture(background);

        if (!this.container.base.rotor_slot.isEmpty()) {
            RenderSystem.setShaderColor(this.container.base.getRotor().getColor().getRed()/255f,this.container.base.getRotor().getColor().getGreen()/255f,this.container.base.getRotor().getColor().getBlue()/255f,1);
            drawTexturedModalRect(poseStack, guiLeft + 49, guiTop + 5, 177,
                    81, 78, 78
            );

            RenderSystem.setShaderColor(1,1,1,1);
            drawTexturedModalRect(poseStack, guiLeft + 49, guiTop + 5, 177,
                    0, 78, 78
            );
        }

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
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

}
