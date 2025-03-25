package com.denfop.integration.jei.deposits_molot;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DepositsMolotWrapper implements IRecipeWrapper {


    private final VeinType vein;
    private final MachineRecipe machineRecipe;

    public DepositsMolotWrapper(DepositsMolotHandler container) {

        this.vein = container.getVeinType();
        this.machineRecipe = container.getMachineRecipe();
    }

    public VeinType getVein() {
        return vein;
    }

    public MachineRecipe getMachineRecipe() {
        return machineRecipe;
    }

    public List<ItemStack> getInputs() {

        return new ArrayList<>( machineRecipe.getRecipe().output.items);
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(IUItem.molot));
        ingredients.setOutputs(VanillaTypes.ITEM, this.getInputs());
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int y = 20;
        int x = 25;
        minecraft.fontRenderer.drawSplitString(Localization.translate("deposists.jei2")+" " + (this.vein.getHeavyOre() != null ?
                        new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta()).getDisplayName() :
                        new ItemStack(vein.getOres().get(0).getBlock().getBlock(), 1,
                                vein.getOres().get(0).getMeta()
                        ).getDisplayName()), 5, 3,
                recipeWidth - 5, 4210752
        );


    }

}
