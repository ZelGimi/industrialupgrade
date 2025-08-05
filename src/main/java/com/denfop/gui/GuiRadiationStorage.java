package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerRadiationStorage;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiRadiationStorage<T extends ContainerRadiationStorage> extends GuiIU<ContainerRadiationStorage> {

    private final ContainerRadiationStorage container;

    public GuiRadiationStorage(ContainerRadiationStorage container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;

        this.componentList.clear();


    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        this.drawForeground(poseStack, mouseX, mouseY);
        new AdvArea(this, 5, 31, 170, 55).withTooltip(ModUtils
                .getString(this.container.base.radiation.storage) + "/" + ModUtils
                .getString(this.container.base.radiation.capacity) + " " +
                "☢").drawForeground(poseStack, mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        final int h = (this.width - this.imageWidth) / 2;
        final int k = (this.height - this.imageHeight) / 2;
        this.bindTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int gauge = (int) (this.container.base.radiation.getFillRatio() * 162);
        drawTexturedModalRect(poseStack, h + 7, k + 33, 7, 171, gauge, 19);
        this.drawBackground(poseStack);
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiradiationstorage.png");
    }


}
