package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuGeothermalWaste;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenGeothermalWaste<T extends ContainerMenuGeothermalWaste> extends ScreenMain<ContainerMenuGeothermalWaste> {

    public ScreenGeothermalWaste(ContainerMenuGeothermalWaste guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        for (int i = 0; i < 4; i++) {
            drawTexturedModalRect(poseStack, this.guiLeft + 176 / 2 - 37 + i * 18, guiTop + 29, 237,
                    76, 18, 18
            );
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigeothermalpump.png");
    }

}
