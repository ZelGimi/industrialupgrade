package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.containermenu.ContainerMenuVending;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenVending<T extends ContainerMenuVending> extends ScreenMain<ContainerMenuVending> {

    private final boolean contains;

    public ScreenVending(ContainerMenuVending guiContainer, final boolean contains) {
        super(guiContainer, guiContainer.base.style);
        this.contains = contains;

        if (contains) {
            this.imageHeight = 255;
            this.inventory.addY(89);
            this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
        }

    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        int sizeWorkingSlot = container.base.getStyle().ordinal() + 1;
        int startPosX = -sizeWorkingSlot * 10;
        int dop = 5 - startPosX;
        int endPosX = (46 - sizeWorkingSlot) * (sizeWorkingSlot - 1) - sizeWorkingSlot * 10;

        if (endPosX - startPosX != 140) {
            dop += (46 * (4 - sizeWorkingSlot) / 2);
        }

        for (int i = 0; i < sizeWorkingSlot; i++) {
            if (!contains) {
                int xDisplayPosition2 = dop + (46 - sizeWorkingSlot) * i - sizeWorkingSlot * 10 + 20;
                if (!this.container.base.invSlotBuyPrivate
                        .get(i).isEmpty()) {
                    final int finalI = i;
                    new TooltipWidget(this, xDisplayPosition2, 16, 18, 18).withTooltip(() -> this.container.base.invSlotBuyPrivate
                            .get(finalI)
                            .getDisplayName().getString()).drawForeground(poseStack, par1, par2);
                }
                if (!this.container.base.invSlotSellPrivate
                        .get(i).isEmpty()) {
                    final int finalI = i;
                    new TooltipWidget(this, xDisplayPosition2, 60, 18, 18).withTooltip(() -> this.container.base.invSlotSellPrivate
                            .get(finalI)
                            .getDisplayName().getString()).drawForeground(poseStack, par1, par2);

                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);


        int sizeWorkingSlot = container.base.getStyle().ordinal() + 1;
        int startPosX = -sizeWorkingSlot * 10;
        int dop = 5 - startPosX;
        int endPosX = (46 - sizeWorkingSlot) * (sizeWorkingSlot - 1) - sizeWorkingSlot * 10;

        if (endPosX - startPosX != 140) {
            dop += (46 * (4 - sizeWorkingSlot) / 2);
        }

        for (int i = 0; i < sizeWorkingSlot; i++) {


            int xDisplayPosition2 = dop + (46 - sizeWorkingSlot) * i - sizeWorkingSlot * 10 + 20;
            if (contains) {
                bindTexture(ScreenWidget.commonTexture1);
                this.drawTexturedModalRect(poseStack,
                        this.guiLeft + xDisplayPosition2 + 13,
                        this.guiTop + 16 + 12,
                        89,
                        3,
                        6,
                        6
                );
                this.drawTexturedModalRect(poseStack,
                        this.guiLeft + xDisplayPosition2 + 13,
                        this.guiTop + 60 + 12,
                        89,
                        3,
                        6,
                        6
                );

            } else {

                if (!this.container.base.invSlotBuyPrivate.get(i).isEmpty()) {
                    this.drawItemStack(poseStack, xDisplayPosition2, 17,
                            this.container.base.invSlotBuyPrivate.get(i)
                    );
                }
                if (!this.container.base.invSlotSellPrivate.get(i).isEmpty()) {
                    this.drawItemStack(poseStack, xDisplayPosition2, 61, this.container.base.invSlotSellPrivate.get(i));
                }

            }
        }
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
