package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuMultiCrop;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenMultiCrop<T extends ContainerMenuMultiCrop> extends ScreenMain<ContainerMenuMultiCrop> {

    public ScreenMultiCrop(ContainerMenuMultiCrop guiContainer) {
        super(guiContainer);
        this.imageWidth += 40;
        this.imageHeight += 40;
        this.inventory.addY(40);
        this.inventory.setX(this.inventory.getX() + 20);
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, imageWidth, imageHeight));
        this.addWidget(TankWidget.createNormal(this, 5, 122, guiContainer.base.fluidWaterTank));
        this.addWidget(TankWidget.createNormal(this, imageWidth - 25, 122, guiContainer.base.fluidPestTank));
        int totalSlots = guiContainer.base.place.length;
        int centerX = (178 + 40 - 18) / 2 - 1;
        int slotSpacing = 40;
        for (int i = 0; i < guiContainer.base.upBlockSlot.size(); i++) {
            int xDisplayPosition1 = centerX - ((totalSlots - 1) * slotSpacing) / 2 + i * slotSpacing;
            this.addComponent(new ScreenWidget(this, xDisplayPosition1 + 2, 38 + 18, EnumTypeComponent.FLUID_PART6,
                    new WidgetDefault<>(new EmptyWidget())
            ));
            final int finalI = i;
            this.addWidget(new TooltipWidget(this, xDisplayPosition1 + 2, 38 + 18, 13, 17).withTooltip(() -> {
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
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }

        PoseStack pose = poseStack.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.imageWidth / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6) / scale);


        draw(poseStack, name, textX, textY, 4210752);


        pose.popPose();
    }


}
