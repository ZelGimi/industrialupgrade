package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerSmelteryCasting;
import com.denfop.tiles.smeltery.TileEntitySmelteryController;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSmelteryCasting extends GuiIU<ContainerSmelteryCasting> {

    public GuiSmelteryCasting(ContainerSmelteryCasting guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.ySize = 181;
        this.addComponent(new GuiComponent(this, 55, 46, EnumTypeComponent.PROCESS,
                new Component<>(this.getContainer().base.progress)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        if (container.base.getMain() != null) {
            TileEntitySmelteryController controller = (TileEntitySmelteryController) container.base.getMain();
            if (controller.getFirstTank() != null) {
                new FluidItem(this, 35, 44, controller.getFirstTank().getFluid()).drawForeground(par1, par2);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        GlStateManager.color(1, 1, 1, 1);
        drawTexturedModalRect(this.guiLeft + 80, guiTop + 44, 178, 62, 18, 18);
        drawTexturedModalRect(this.guiLeft + 10, guiTop + 70, 178, 62, 18, 18);
        drawTexturedModalRect(this.guiLeft + 35, guiTop + 44, 178, 83, 18, 18);
        if (container.base.getMain() != null) {
            TileEntitySmelteryController controller = (TileEntitySmelteryController) container.base.getMain();
            if (controller.getFirstTank() != null) {
                new FluidItem(this, 35, 44, controller.getFirstTank().getFluid()).drawBackground(
                        this.guiLeft,
                        guiTop
                );
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guismeltery.png");
    }

}
