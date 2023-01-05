package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.container.ContainerRotorAssembler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiRotorAssembler extends GuiIU<ContainerRotorAssembler> {

    public final ResourceLocation rotors_gui = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors.png");
    public final ResourceLocation rotors_gui1 = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors1.png");

    public GuiRotorAssembler(ContainerRotorAssembler guiContainer) {
        super(guiContainer);
        this.xSize = 206;
        this.ySize = 256;
        this.inventory.setY(172);
        this.addComponent(new GuiComponent(this, 142, 150, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotorcreate.png");

    }

    void setTexture(ResourceLocation resourceLocation) {
        this.mc.getTextureManager().bindTexture(resourceLocation);
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        // progress 18 pixels
        int progress = (int) (16 * (this.container.base.progress / 100D));
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;


        if (this.container.base.recipe != null) {
            ItemStack stack = this.container.base.recipe.getRecipe().output.items.get(0);
            int index = ((IWindRotor) stack.getItem()).getIndex();
            setTexture(rotors_gui1);
            drawTexturedModalRect(j + 80, k + 63, 32 * (index % 8),
                    32 * (index / 8), 32, 32
            );
            setTexture(rotors_gui);
            drawTexturedModalRect(j + 80, k + 9 + progress, 32 * (index % 8),
                    55 * (index / 8), 32, 54
            );
            drawTexturedModalRect(j + 80, k + 95 - progress, 32 * (index % 8),
                    55 * (index / 8), 32, 54
            );
            drawTexturedModalRect(j + 26 + progress, k + 63, 55 * (index % 4),
                    112 + 33 * (index / 4), 54, 32
            );
            drawTexturedModalRect(j + 112 - progress, k + 63, 55 * (index % 4),
                    112 + 33 * (index / 4), 54, 32
            );

            this.bindTexture();

        }
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

}
