package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.FluidItem;
import com.denfop.container.ContainerSmelteryCasting;
import com.denfop.tiles.smeltery.TileEntitySmelteryController;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSmelteryCasting extends GuiIU<ContainerSmelteryCasting> {

    public GuiSmelteryCasting(ContainerSmelteryCasting guiContainer) {
        super(guiContainer);
        this.componentList.clear();

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        new AdvArea(this, 69, 34, 100, 52)
                .withTooltip(ModUtils.getString(this.getContainer().base.progress.getBar() * 100) + "%")
                .drawForeground(par1, par2);
        if (container.base.getMain() != null) {
            TileEntitySmelteryController controller = (TileEntitySmelteryController) container.base.getMain();
            if (controller.getFirstTank() != null) {
                new FluidItem(this, 49, 34, controller.getFirstTank().getFluid()).drawForeground(par1, par2);
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        GlStateManager.color(1, 1, 1, 1);
        drawTexturedModalRect(
                this.guiLeft + 69,
                guiTop + 34,
                177,
                1,
                (int) (this.getContainer().base.progress.getBar() * 32),
                20
        );
        if (container.base.getMain() != null) {
            TileEntitySmelteryController controller = (TileEntitySmelteryController) container.base.getMain();
            if (controller.getFirstTank() != null) {
                new FluidItem(this, 49, 34, controller.getFirstTank().getFluid()).drawBackground(
                        this.guiLeft,
                        guiTop
                );
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guismeltery_casting.png");
    }

}
