package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.container.ContainerMultiCrop;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiMultiCrop<T extends ContainerMultiCrop> extends GuiIU<ContainerMultiCrop> {

    public GuiMultiCrop(ContainerMultiCrop guiContainer) {
        super(guiContainer);
        this.imageWidth += 40;
        this.imageHeight += 40;
        this.inventory.addY(40);
        this.inventory.setX(this.inventory.getX() + 20);
        this.addElement(new ImageInterface(this, 0, 0, imageWidth, imageHeight));
        this.addElement(TankGauge.createNormal(this, 5, 122, guiContainer.base.fluidWaterTank));
        this.addElement(TankGauge.createNormal(this, imageWidth - 25, 122, guiContainer.base.fluidPestTank));
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
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }


        poseStack.pushPose();
        poseStack.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.imageWidth / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6) / scale);


        this.font.draw(poseStack, name, textX, textY, 4210752);


        poseStack.popPose();
    }



}
