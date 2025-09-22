package com.denfop.api.gui;


import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import com.denfop.gui.GuiCore;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GuiElementFluidToFluids extends GuiElement {

    private final FluidStack fluid;
    private final TypeFluids recipe;
    private final int coef;

    public GuiElementFluidToFluids(GuiCore<?> gui, int x, int y, int width, int height, TypeFluids typeGenerator) {
        super(gui, x, y, width, height);
        this.recipe = typeGenerator;
        this.coef = 1000 / this.recipe.stack.amount;
        this.fluid = new FluidStack(this.recipe.getStack().getFluid(), 1000);

    }

    public static GuiElementFluidToFluids GuiElementFluidToFluids(GuiCore<?> gui, int x, int y, TypeFluids typeGenerator) {
        return new GuiElementFluidToFluids(gui, x, y, 83, 22, typeGenerator);
    }

    @Override
    public void drawForeground(final int mouseX, final int mouseY) {

        if (mouseX >= this.x && mouseX <= this.x + 18 && mouseY >= this.y && mouseY <= this.y + 18) {
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
        for (int i = 0; i < this.recipe.getPut().length; i++) {
            FluidStack stack = this.recipe.getPut()[i];
            int fluidX = this.x + 1 + 44 + 20 * i;
            int fluidY = this.y + 1;
            List<String> ret = new ArrayList<>();
            if (mouseX >= fluidX && mouseX <= fluidX + 18 && mouseY >= fluidY && mouseY <= fluidY + 18) {
                if (stack != null && stack.amount > 0) {
                    Fluid fluid = stack.getFluid();
                    if (fluid != null) {
                        ret.add(fluid.getLocalizedName(stack));
                        ret.add("Amount: " + stack.amount * this.coef + " " + Localization.translate("iu.generic.text.mb"));
                        String state = stack.getFluid().isGaseous() ? "Gas" : "Liquid";
                        ret.add("Type: " + state);
                    } else {
                        ret.add("Invalid FluidStack instance.");
                    }
                } else {
                    ret.add("No Fluid");
                    ret.add("Amount: 0 " + Localization.translate("iu.generic.text.mb"));
                    ret.add("Type: Not Available");
                }
                if (!ret.isEmpty()) {
                    this.gui.drawTooltip(mouseX, mouseY, ret);
                }
            }
        }


    }

    public void drawBackground(int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = fluid;

        this.gui.drawTexturedRect(this.x, this.y, this.width, this.height, 169, 51);

        if (fs != null && fs.amount > 0) {
            int fluidX = this.x + 1;
            int fluidY = this.y + 1;
            int fluidWidth = 16;
            int fluidHeight = 16;
            Fluid fluid = fs.getFluid();
            TextureAtlasSprite sprite = fluid != null ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString()) : null;
            int color = fluid != null ? fluid.getColor(fs) : -1;
            bindBlockTexture();
            this.gui.drawSprite(
                    fluidX,
                    fluidY,
                    fluidWidth,
                    fluidHeight,
                    sprite,
                    color,
                    1.0,
                    false,
                    false
            );
        }
        for (int i = 0; i < this.recipe.getPut().length; i++) {
            FluidStack stack = this.recipe.getPut()[i];
            int fluidX = this.x + 1 + 44 + 20 * i;
            int fluidY = this.y + 1;
            int fluidWidth = 16;
            int fluidHeight = 16;
            Fluid fluid = stack.getFluid();
            TextureAtlasSprite sprite = fluid != null ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString()) : null;
            int color = fluid != null ? fluid.getColor(fs) : -1;
            bindBlockTexture();
            this.gui.drawSprite(
                    fluidX,
                    fluidY,
                    fluidWidth,
                    fluidHeight,
                    sprite,
                    color,
                    1.0,
                    false,
                    false
            );
        }
    }

    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack fs = this.fluid;
        if (fs != null && fs.amount > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(fluid.getLocalizedName(fs));
                ret.add("Amount: " + fs.amount + " " + Localization.translate("iu.generic.text.mb"));
                String state = fs.getFluid().isGaseous() ? "Gas" : "Liquid";
                ret.add("Type: " + state);
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

    public enum TypeFluids {
        OIL_REFINERY(new FluidStack(FluidName.fluidneft.getInstance(), 5), new FluidStack(FluidName.fluidbenz.getInstance(), 3),
                new FluidStack(FluidName.fluiddizel.getInstance(), 2)
        ),
        ADV_OIL_REFINERY(new FluidStack(FluidName.fluidneft.getInstance(), 10), new FluidStack(
                FluidName.fluidpolyeth.getInstance(),
                5
        ),
                new FluidStack(FluidName.fluidpolyprop.getInstance(), 5)
        ),
        ;
        private final FluidStack stack;
        private final FluidStack[] put;

        TypeFluids(FluidStack stack, FluidStack... out) {
            this.stack = stack;
            this.put = out;
        }

        public FluidStack getStack() {
            return stack;
        }

        public FluidStack[] getPut() {
            return put;
        }
    }

}
