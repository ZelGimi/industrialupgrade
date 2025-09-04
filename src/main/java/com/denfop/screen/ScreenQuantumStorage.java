package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.containermenu.ContainerMenuQuantumStorage;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenQuantumStorage<T extends ContainerMenuQuantumStorage> extends ScreenMain<ContainerMenuQuantumStorage> {

    private final ContainerMenuQuantumStorage container;

    public ScreenQuantumStorage(ContainerMenuQuantumStorage container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;

        this.componentList.clear();


    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        this.drawForeground(poseStack, mouseX, mouseY);
        new AdvancedTooltipWidget(this, 5, 30, 170, 54).withTooltip(ModUtils
                .getString(this.container.base.qe.buffer.storage) + "/" + ModUtils
                .getString(this.container.base.qe.buffer.capacity) + " " +
                "QE").drawForeground(poseStack, mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        final int h = guiLeft;
        final int k = guiTop;
        this.bindTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int gauge = (int) (this.container.base.qe.getFillRatio() * 162);
        drawTexturedModalRect(poseStack, h + 7, k + 32, 7, 171, gauge, 19);
        this.drawBackground(poseStack);
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumstorage.png");
    }


}
