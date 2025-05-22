package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerVending;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

public class GuiVending extends GuiIU<ContainerVending> {

    private final boolean contains;

    public GuiVending(ContainerVending guiContainer, final boolean contains) {
        super(guiContainer, guiContainer.base.style);
        this.contains = contains;

        if (contains) {
            this.ySize = 255;
            this.inventory.addY(89);
            this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        }

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
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
                    new Area(this, xDisplayPosition2, 16, 18, 18).withTooltip(() -> this.container.base.invSlotBuyPrivate
                            .get(finalI)
                            .getDisplayName()).drawForeground(par1, par2);
                }
                if (!this.container.base.invSlotSellPrivate
                        .get(i).isEmpty()) {
                    final int finalI = i;
                    new Area(this, xDisplayPosition2, 60, 18, 18).withTooltip(() -> this.container.base.invSlotSellPrivate
                            .get(finalI)
                            .getDisplayName()).drawForeground(par1, par2);

                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);


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
                this.mc.getTextureManager().bindTexture(GuiComponent.commonTexture1);
                this.drawTexturedModalRect(
                        this.guiLeft + xDisplayPosition2 + 13,
                        this.guiTop + 16 + 12,
                        89,
                        3,
                        6,
                        6
                );
                this.drawTexturedModalRect(
                        this.guiLeft + xDisplayPosition2 + 13,
                        this.guiTop + 60 + 12,
                        89,
                        3,
                        6,
                        6
                );

            } else {
                RenderHelper.enableGUIStandardItemLighting();
                if (!this.container.base.invSlotBuyPrivate.get(i).isEmpty()) {
                    this.drawItemStack(xDisplayPosition2, 17,
                            this.container.base.invSlotBuyPrivate.get(i)
                    );
                }
                if (!this.container.base.invSlotSellPrivate.get(i).isEmpty()) {
                    this.drawItemStack(xDisplayPosition2, 61, this.container.base.invSlotSellPrivate.get(i));
                }
                RenderHelper.disableStandardItemLighting();
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
