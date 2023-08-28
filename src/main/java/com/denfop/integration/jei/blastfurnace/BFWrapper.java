package com.denfop.integration.jei.blastfurnace;

import com.denfop.IUItem;
import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BFWrapper implements IRecipeWrapper {


    public BFWrapper(BFHandler container) {


    }


    public List<ItemStack> getOutputs() {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(new ItemStack(IUItem.blastfurnace, 1, i));
        }
        return list;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(Localization.translate("iu.blastfurnace.info1"), 75, 21, 4210752);
        minecraft.fontRenderer.drawString(Localization.translate("iu.blastfurnace.info2"), 75, 28, 4210752);
        minecraft.fontRenderer.drawString(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace,
                1,
                0
        ).getUnlocalizedName()), 75, 35, 4210752);
        minecraft.fontRenderer.drawString(Localization.translate("iu.blastfurnace.info4"), 75, 42, 4210752);
        minecraft.fontRenderer.drawString(
                Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer).getDisplayName(),
                75,
                49,
                4210752
        );

    }

}
