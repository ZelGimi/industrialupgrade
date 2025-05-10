package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.FluidItem;
import com.denfop.container.ContainerSmelteryCasting;
import com.denfop.tiles.smeltery.TileEntitySmelteryController;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiSmelteryCasting<T extends ContainerSmelteryCasting> extends GuiIU<ContainerSmelteryCasting> {

    public GuiSmelteryCasting(ContainerSmelteryCasting guiContainer) {
        super(guiContainer);
        this.componentList.clear();

    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        new AdvArea(this, 69, 34, 100, 52)
                .withTooltip(ModUtils.getString(this.getContainer().base.progress.getBar() * 100) + "%")
                .drawForeground(poseStack,par1, par2);
        if (container.base.getMain() != null) {
            TileEntitySmelteryController controller = (TileEntitySmelteryController) container.base.getMain();
            if (controller.getFirstTank() != null) {
                new FluidItem(this, 49, 34, controller.getFirstTank().getFluid()).drawForeground(poseStack,par1, par2);
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
        this.bindTexture();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        drawTexturedModalRect(poseStack,
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
                new FluidItem(this, 49, 34, controller.getFirstTank().getFluid()).drawBackground(poseStack,
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
