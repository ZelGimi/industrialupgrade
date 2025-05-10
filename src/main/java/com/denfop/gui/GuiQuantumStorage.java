package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerQuantumStorage;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiQuantumStorage<T extends ContainerQuantumStorage> extends GuiIU<ContainerQuantumStorage> {

    private final ContainerQuantumStorage container;

    public GuiQuantumStorage(ContainerQuantumStorage container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;

        this.componentList.clear();


    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        this.drawForeground(poseStack, mouseX, mouseY);
        new AdvArea(this, 5, 30, 170, 54).withTooltip(ModUtils
                .getString(this.container.base.qe.storage) + "/" + ModUtils
                .getString(this.container.base.qe.capacity) + " " +
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
