package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerRadiationStorage;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiRadiationStorage extends GuiIU<ContainerRadiationStorage> {

    private final ContainerRadiationStorage container;

    public GuiRadiationStorage(ContainerRadiationStorage container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;

        this.componentList.clear();


    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.drawForeground(mouseX, mouseY);
        new AdvArea(this, 5, 31, 170, 55).withTooltip(ModUtils
                .getString(this.container.base.radiation.storage) + "/" + ModUtils
                .getString(this.container.base.radiation.capacity) + " " +
                "☢").drawForeground(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(f, x, y);
        final int h = (this.width - this.xSize) / 2;
        final int k = (this.height - this.ySize) / 2;
        this.bindTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int gauge = (int) (this.container.base.radiation.getFillRatio() * 162);
        drawTexturedModalRect(h + 7, k + 33, 7, 171, gauge, 19);
        this.drawBackground();
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiradiationstorage.png");
    }


}
