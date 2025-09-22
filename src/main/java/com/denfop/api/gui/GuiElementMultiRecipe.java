package com.denfop.api.gui;


import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.gui.GuiCore;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class GuiElementMultiRecipe extends GuiElement {


    private BaseMachineRecipe recipe;

    public GuiElementMultiRecipe(GuiCore<?> gui, int x, int y, int width, int height, EnumTypeMachines recipe, ItemStack stack) {
        super(gui, x, y, width, height);
        final List<BaseMachineRecipe> recipes = Recipes.recipes.getRecipeList(recipe.recipe);
        for (BaseMachineRecipe recipe1 : recipes) {
            for (ItemStack stack1 : recipe1.output.items) {
                if (stack1.isItemEqual(stack)) {
                    this.recipe = recipe1;
                    break;
                }
            }

        }


    }

    public static GuiElementMultiRecipe GuiElementMultiRecipe(
            GuiCore<?> gui,
            int x,
            int y,
            EnumTypeMachines recipe,
            ItemStack stack
    ) {
        return new GuiElementMultiRecipe(gui, x, y, 36, 75, recipe, stack);
    }

    @Override
    public void drawForeground(final int mouseX, final int mouseY) {


        final int fluidX = this.x + 1 + 19;
        int fluidY = this.y + 1;
        if (mouseX >= fluidX && mouseX <= fluidX + 18 && mouseY >= fluidY && mouseY <= fluidY + 18) {
            ItemStack stack1 = recipe.input.getInputs().get(0).getInputs().get(0);
            if (!ModUtils.isEmpty(stack1)) {
                this.gui.drawTooltip(mouseX, mouseY, stack1);
            }
        }


    }

    public void drawBackground(int mouseX, int mouseY) {
        bindCommonTexture1();


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
