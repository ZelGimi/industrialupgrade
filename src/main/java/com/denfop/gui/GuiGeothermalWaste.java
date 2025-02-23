package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerGeothermalWaste;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiGeothermalWaste extends GuiIU<ContainerGeothermalWaste> {

    public GuiGeothermalWaste(ContainerGeothermalWaste guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        bindTexture();
        GlStateManager.color(1, 1, 1, 1);
        for (int i = 0; i < 4; i++) {
            drawTexturedModalRect(this.guiLeft + 176 / 2 - 37 + i * 18, guiTop + 29, 237,
                    76, 18, 18
            );
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigeothermalpump.png");
    }

}
