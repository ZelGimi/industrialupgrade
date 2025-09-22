package com.denfop.api.gui;


import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.ArrayList;
import java.util.List;

public class GuiElementItemFluid extends GuiElement {


    private BaseMachineRecipe recipe;

    public GuiElementItemFluid(GuiCore<?> gui, int x, int y, int width, int height, String recipe, ItemStack stack, Fluid fluid) {
        super(gui, x, y, width, height);
        final List<BaseMachineRecipe> recipes = Recipes.recipes.getRecipeList(recipe);
        for (BaseMachineRecipe recipe1 : recipes) {
            if (recipe1.input.getFluid() == null) {
                continue;
            } else {
                FluidStack liquid = FluidUtil.getFluidContained(recipe1.output.items.get(0));
                if (liquid != null && liquid.getFluid() == fluid) {
                    if (stack != null) {
                        if (recipe1.input.getInputs().get(0).matches(stack)) {
                            this.recipe = recipe1;
                            break;
                        }
                    } else {
                        this.recipe = recipe1;
                        break;
                    }
                }
            }
        }

    }

    public static GuiElementItemFluid GuiElementItemFluid(
            GuiCore<?> gui, int x, int y, String recipe, ItemStack stack,
            Fluid fluid
    ) {
        return new GuiElementItemFluid(gui, x, y, 83, 22, recipe, stack, fluid);
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
        int i = 1;
        final FluidStack stack = FluidUtil.getFluidContained(recipe.output.items.get(0));
        int fluidX = this.x + 1 + 44 + 20 * i;
        int fluidY = this.y + 1;
        List<String> ret = new ArrayList<>();
        if (mouseX >= fluidX && mouseX <= fluidX + 18 && mouseY >= fluidY && mouseY <= fluidY + 18) {
            if (stack != null && stack.amount > 0) {
                Fluid fluid = stack.getFluid();
                if (fluid != null) {
                    ret.add(fluid.getLocalizedName(stack));
                    ret.add("Amount: " + stack.amount + " " + Localization.translate("iu.generic.text.mb"));
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
        fluidX = this.x + 1 + 19;
        fluidY = this.y + 1;
        if (mouseX >= fluidX && mouseX <= fluidX + 18 && mouseY >= fluidY && mouseY <= fluidY + 18) {
            ItemStack stack1 = recipe.input.getInputs().get(1).getInputs().get(0);
            if (!ModUtils.isEmpty(stack1)) {
                this.gui.drawTooltip(mouseX, mouseY, stack1);
            }
        }


    }

    public void drawBackground(int mouseX, int mouseY) {
        bindCommonTexture();
        if (recipe == null) {
            return;
        }
        if (recipe.input == null) {
            return;
        }
        FluidStack fs = recipe.input.getFluid();
        this.gui.drawTexturedRect(this.x, this.y, this.width, this.height, 169, 75);

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
        ItemStack stack = recipe.input.getInputs().get(1).getInputs().get(0);
        if (!ModUtils.isEmpty(stack)) {
            RenderHelper.enableGUIStandardItemLighting();
            this.gui.drawItemStack(this.x + 21, this.y + 1, stack);
            RenderHelper.disableStandardItemLighting();
        }
        fs = FluidUtil.getFluidContained(recipe.output.items.get(0));
        if (fs != null && fs.amount > 0) {
            int fluidX = this.x + 1 + 64;
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

    }

    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack fs = this.recipe.input.getFluid();
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

}
