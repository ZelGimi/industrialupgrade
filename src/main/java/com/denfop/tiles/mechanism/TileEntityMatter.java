package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityMultiMatter;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityMatter extends TileEntityMultiMatter implements IHasRecipe {

    public TileEntityMatter() {
        super(1000000F, 10, 5000000);
        com.denfop.api.Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public void init() {
        addAmplifier(new ItemStack(IUItem.doublescrapBox),  405000);
        addAmplifier(Ic2Items.scrap,  5000);
        addAmplifier(Ic2Items.scrapBox,  45000);
    }
    public static void addAmplifier(ItemStack stack, int amplification) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("amount",amplification);
        com.denfop.api.Recipes.recipes.addRecipe("matterAmplifier", new BaseMachineRecipe(
                new Input(input.forStack(stack)),
                new RecipeOutput(nbt, stack)
        ));

    }
}
