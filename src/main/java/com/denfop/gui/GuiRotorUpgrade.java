package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerRotorUpgrade;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiRotorUpgrade extends GuiIU<ContainerRotorUpgrade> {

    public final ContainerRotorUpgrade container;
    public final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotorupgrade.png");
    public final ResourceLocation rotors_gui = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors.png");

    public GuiRotorUpgrade(ContainerRotorUpgrade guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.ySize = 206;
        this.elements.add(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
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
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.mc.getTextureManager().bindTexture(background);

        if (!this.container.base.rotor_slot.isEmpty()) {
            int j = (this.width - this.xSize) / 2;
            int k = (this.height - this.ySize) / 2;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            setTexture(rotors_gui);
            drawTexturedModalRect(j + 71, k + 5, 32 * (this.container.base.getRotor().getIndex() % 8),
                    55 * (this.container.base.getRotor().getIndex() / 8), 32, 54
            );
            drawTexturedModalRect(j + 71, k + 57, 32 * (this.container.base.getRotor().getIndex() % 8),
                    55 * (this.container.base.getRotor().getIndex() / 8), 32, 54
            );
            drawTexturedModalRect(j + 33, k + 43, 55 * (this.container.base.getRotor().getIndex() % 4),
                    112 + 33 * (this.container.base.getRotor().getIndex() / 4), 54, 32
            );
            drawTexturedModalRect(j + 85, k + 43, 55 * (this.container.base.getRotor().getIndex() % 4),
                    112 + 33 * (this.container.base.getRotor().getIndex() / 4), 54, 32
            );
            setTexture(background);
            drawTexturedModalRect(j + 77, k + 6, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(j + 34, k + 49, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(j + 77, k + 49, 212,
                    33, 18, 18
            );
            drawTexturedModalRect(j + 120, k + 49, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(j + 77, k + 92, 230,
                    33, 18, 18
            );

        }

    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    void setTexture(ResourceLocation resourceLocation) {
        this.mc.getTextureManager().bindTexture(resourceLocation);
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

}
