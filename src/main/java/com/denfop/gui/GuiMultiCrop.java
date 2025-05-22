package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerMultiCrop;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiMultiCrop extends GuiIU<ContainerMultiCrop> {

    public GuiMultiCrop(ContainerMultiCrop guiContainer) {
        super(guiContainer);
        this.xSize += 40;
        this.ySize += 40;
        this.inventory.addY(40);
        this.inventory.setX(this.inventory.getX() + 20);
        this.addElement(new ImageInterface(this, 0, 0, xSize, ySize));
        this.addElement(TankGauge.createNormal(this, 5, 122, guiContainer.base.fluidWaterTank));
        this.addElement(TankGauge.createNormal(this, xSize - 25, 122, guiContainer.base.fluidPestTank));
        int totalSlots = guiContainer.base.place.length;
        int centerX = (178 + 40 - 18) / 2 - 1;
        int slotSpacing = 40;
        for (int i = 0; i < guiContainer.base.upBlockSlot.size(); i++) {
            int xDisplayPosition1 = centerX - ((totalSlots - 1) * slotSpacing) / 2 + i * slotSpacing;
            this.addComponent(new GuiComponent(this, xDisplayPosition1 + 2, 38 + 18, EnumTypeComponent.FLUID_PART6,
                    new Component<>(new ComponentEmpty())
            ));
            final int finalI = i;
            this.addElement(new Area(this, xDisplayPosition1 + 2, 38 + 18, 13, 17).withTooltip(() -> {
                if (guiContainer.base.maxTickSoil[finalI] != 0) {
                    return ModUtils.getString(guiContainer.base.tickSoil[finalI] * 100D / guiContainer.base.maxTickSoil[finalI]) + "%";
                }
                return "0%";
            }));
        }

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.fontRenderer.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }


        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.xSize / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6) / scale);


        this.fontRenderer.drawString(name, textX, textY, 4210752);


        GlStateManager.popMatrix();
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

    }

}
