package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.tiles.mechanism.dual.TileEntitySolidCanner;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

public class CannerRecipe {

    public static void recipe() {
        final IRecipeInputFactory input = Recipes.inputFactory;
        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                IUItem.proton,
                IUItem.reactorprotonSimple
        );
        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 4),
                IUItem.reactortoriySimple
        );

        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 0),
                IUItem.reactoramericiumSimple
        );
        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 1),
                IUItem.reactorneptuniumSimple
        );
        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 2),
                IUItem.reactorcuriumSimple
        );
        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 3),
                IUItem.reactorcaliforniaSimple
        );

        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 5),
                IUItem.reactormendeleviumSimple
        );
        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 6),
                IUItem.reactorberkeliumSimple
        );
        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 7),
                IUItem.reactoreinsteiniumSimple
        );
        TileEntitySolidCanner.addBottleRecipe(
                Ic2Items.fuelRod,
                new ItemStack(IUItem.radiationresources, 1, 8),
                IUItem.reactoruran233Simple
        );
    }

}
