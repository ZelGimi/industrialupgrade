package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.container.ContainerRotorAssembler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GuiRotorAssembler<T extends ContainerRotorAssembler> extends GuiIU<ContainerRotorAssembler> {

    public final ResourceLocation rotors_gui = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors.png");
    public final ResourceLocation rotors_gui1 = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors1.png");

    public GuiRotorAssembler(ContainerRotorAssembler guiContainer) {
        super(guiContainer);
        this.imageWidth = 206;
        this.imageHeight = 256;
        this.inventory.setY(172);
        this.addComponent(new GuiComponent(this, 132, 150, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.elements.add(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");

    }

    void setTexture(ResourceLocation resourceLocation) {
        this.bindTexture(resourceLocation);
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack,mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer( poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // progress 18 pixels
        int progress = (int) (16 * (this.container.base.progress / 100D));
        int j = guiLeft;
        int k = guiTop;


        if (this.container.base.recipe != null) {
            ItemStack stack = this.container.base.recipe.getRecipe().output.items.get(0);
            int index = ((IWindRotor) stack.getItem()).getIndex();
            setTexture(rotors_gui1);
            drawTexturedModalRect( poseStack, j + 80, k + 63, 32 * (index % 8),
                    32 * (index / 8), 32, 32
            );
            setTexture(rotors_gui);
            drawTexturedModalRect( poseStack, j + 80, k + 9 + progress, 32 * (index % 8),
                    55 * (index / 8), 32, 54
            );
            drawTexturedModalRect( poseStack, j + 80, k + 95 - progress, 32 * (index % 8),
                    55 * (index / 8), 32, 54
            );
            drawTexturedModalRect( poseStack, j + 26 + progress, k + 63, 55 * (index % 4),
                    112 + 33 * (index / 4), 54, 32
            );
            drawTexturedModalRect( poseStack, j + 112 - progress, k + 63, 55 * (index % 4),
                    112 + 33 * (index / 4), 54, 32
            );

            this.bindTexture();

        }
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

}
