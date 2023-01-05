package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

public class CannerRecipe {

    public static void recipe() {
        final IRecipeInputFactory input = Recipes.inputFactory;
        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.proton, 1), 1),
                IUItem.reactorprotonSimple, false
        );
        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 4), 1),
                IUItem.reactortoriySimple, false
        );

        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 0), 1),
                IUItem.reactoramericiumSimple, false
        );
        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 1), 1),
                IUItem.reactorneptuniumSimple, false
        );
        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 2), 1),
                IUItem.reactorcuriumSimple, false
        );
        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 3), 1),
                IUItem.reactorcaliforniaSimple, false
        );

        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 5), 1),
                IUItem.reactormendeleviumSimple, false
        );
        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 6), 1),
                IUItem.reactorberkeliumSimple, false
        );
        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 7), 1),
                IUItem.reactoreinsteiniumSimple, false
        );
        Recipes.cannerBottle.addRecipe(
                input.forStack(Ic2Items.fuelRod, 1),
                input.forStack(new ItemStack(IUItem.radiationresources, 1, 8), 1),
                IUItem.reactoruran233Simple, false
        );
    }

}
