package com.denfop.api.gui;


import com.denfop.Localization;
import com.denfop.gui.GuiCore;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FluidItem extends GuiElement<FluidItem> {

    private final FluidStack fluid;

    public FluidItem(GuiCore<?> gui, int x, int y, FluidStack fluid) {
        super(gui, x, y, 18, 18);

        this.fluid = fluid;
    }

    public static FluidItem createFluidSlot(GuiCore<?> gui, int x, int y, FluidStack fluid) {
        return new FluidItem(gui, x, y, fluid);
    }

    @Override
    public void drawForeground(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        if (mouseX >= this.x && mouseX < this.x + 18 && mouseY >= this.y && mouseY < this.y + 18) {
            List<String> lines = this.getToolTip();
            if (this.getTooltipProvider() != null) {
                String tooltip = this.getTooltipProvider().get();
                if (tooltip != null && !tooltip.isEmpty()) {
                    addLines(lines, tooltip);
                }
            }

            if (!lines.isEmpty()) {
                this.gui.drawTooltip(mouseX, mouseY, lines);
            }
        }

    }

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = fluid;
        if (!fs.isEmpty() && fs.getAmount() > 0) {
            int fluidX = this.x + 1;
            int fluidY = this.y + 1;
            int fluidWidth = 16;
            int fluidHeight = 16;
            Fluid fluid = fs.getFluid();
            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
            TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
            int color = extensions.getTintColor();
            bindBlockTexture();
            this.gui.drawSprite(poseStack,
                   mouseX+ fluidX,
                    mouseY+     fluidY,
                    fluidWidth,
                    fluidHeight,
                    sprite,
                    color,
                    1.0,
                    false,
                    false
            );
        }
        GuiCore.bindTexture(commonTexture1);
    }

    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack fs = this.fluid;
        if (fs != null && fs.getAmount() > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(Localization.translate(fs.getTranslationKey()));
                ret.add("Amount: " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
                ret.add("Type: " + "Liquid");
            } else {
                ret.add("Invalid FluidStack instance.");
            }
        } else {
            ret.add("No Fluid");
            ret.add("Amount: 0 " + Localization.translate("iu.generic.text.mb"));
            ret.add("Type: Not Available");
        }

        return ret;
    }

}
