package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.containermenu.ContainerMenuSmelteryFurnace;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenSmelteryFurnace<T extends ContainerMenuSmelteryFurnace> extends ScreenMain<ContainerMenuSmelteryFurnace> {

    public ScreenSmelteryFurnace(ContainerMenuSmelteryFurnace guiContainer) {
        super(guiContainer);
        this.componentList.clear();

    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        new AdvancedTooltipWidget(this, 40, 58, 129, 65)
                .withTooltip(ModUtils.getString(this.getContainer().base.progress.getBar() * 100) + "%")
                .drawForeground(poseStack, par1, par2);
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        drawTexturedModalRect(poseStack, this.guiLeft + 45, guiTop + 63, 176, 0, (int) (this.getContainer().base.progress.getBar() * 80),
                20
        );

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guismeltery_furnace.png");
    }

}
