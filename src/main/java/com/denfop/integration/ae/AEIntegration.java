package com.denfop.integration.ae;

import appeng.core.Api;
import com.denfop.IUCore;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AEIntegration {

    public static void init() {
        addmacerator(new ItemStack(Items.QUARTZ), Api.INSTANCE.definitions().materials().netherQuartzDust().maybeStack(1).get());
        addmacerator(
                Api.INSTANCE.definitions().materials().certusQuartzCrystal().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().certusQuartzDust().maybeStack(1).get()
        );
        addmacerator(
                Api.INSTANCE.definitions().materials().certusQuartzCrystalCharged().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().certusQuartzDust().maybeStack(1).get()
        );
        addmacerator(
                Api.INSTANCE.definitions().materials().fluixCrystal().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().fluixDust().maybeStack(1).get()
        );
        IUCore.list.add(Api.INSTANCE.definitions().materials().certusQuartzCrystal().maybeStack(1).get());
        IUCore.list.add(Api.INSTANCE.definitions().materials().certusQuartzCrystalCharged().maybeStack(1).get());

    }

    public static void addmacerator(ItemStack input, ItemStack output) {


        final IRecipeInputFactory input1 = Recipes.inputFactory;
        Recipes.macerator.addRecipe(input1.forStack(
                        input), null, false,
                output
        );


    }

}
