package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerSolariumStorage;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiSolariumStorage<T extends ContainerSolariumStorage> extends GuiIU<ContainerSolariumStorage> {

    private final ContainerSolariumStorage container;

    public GuiSolariumStorage(ContainerSolariumStorage container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.componentList.clear();


    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        this.drawForeground(poseStack, mouseX, mouseY);
        new AdvArea(this, 5, 30, 170, 54).withTooltip(ModUtils
                .getString(this.container.base.se.storage) + "/" + ModUtils
                .getString(this.container.base.se.capacity) + " " +
                "SE").drawForeground(poseStack, mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        final int h = guiLeft;
        final int k = guiTop;
        this.bindTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int gauge = (int) (this.container.base.se.getFillRatio() * 162);
        drawTexturedModalRect(poseStack, h + 7, k + 32, 7, 171, gauge, 19);

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisolariumstorage.png");
    }

}
