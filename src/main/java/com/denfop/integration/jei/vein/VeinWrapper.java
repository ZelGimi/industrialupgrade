package com.denfop.integration.jei.vein;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VeinWrapper implements IRecipeWrapper {


    public VeinWrapper(VeinHandler container) {


    }


    public List<ItemStack> getInputs() {
        List<ItemStack> stack = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            stack.add(new ItemStack(IUItem.heavyore, 1, i));
        }
        stack.add(new ItemStack(IUItem.oilblock));
        return stack;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.FLUID,
                Arrays.asList(new FluidStack(FluidName.fluidneft.getInstance(),
            1000), new FluidStack(FluidName.fluidgas.getInstance(), 1000)
                        , new FluidStack(FluidName.fluidsour_heavy_oil.getInstance(), 1000)
                        , new FluidStack(FluidName.fluidsour_medium_oil.getInstance(), 1000)
                        , new FluidStack(FluidName.fluidsour_light_oil.getInstance(), 1000)
                        , new FluidStack(FluidName.fluidsweet_medium_oil.getInstance(), 1000)
                        , new FluidStack(FluidName.fluidsweet_heavy_oil.getInstance(), 1000)));
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                Localization.translate("iu.vein.info") + ". " + Localization.translate("iu.vein.info1") + ". " +    Localization.translate("iu.vein.info2") + ". " +   Localization.translate("iu.vein.info3") + ". " +   Localization.translate("iu.vein.info4") + ". " +   Localization.translate("iu.vein.info5"),
                6,
                14, recipeWidth - 5,
                4210752
        );


    }

}
