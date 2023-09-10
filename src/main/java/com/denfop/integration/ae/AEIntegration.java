package com.denfop.integration.ae;

import appeng.core.Api;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.recipes.MaceratorRecipe;
import com.denfop.tiles.base.TileSunnariumMaker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AEIntegration {

    public static void init() {

        MaceratorRecipe.addmacerator(
                new ItemStack(Items.QUARTZ),
                Api.INSTANCE.definitions().materials().netherQuartzDust().maybeStack(1).get()
        );
        if (Api.INSTANCE.definitions().materials().purifiedNetherQuartzCrystal().maybeStack(1).isPresent()) {
            TileSunnariumMaker.addSunnariumMaker(
                    new ItemStack(IUItem.sunnarium, 4, 4),
                    new ItemStack(Items.GLOWSTONE_DUST),
                    Api.INSTANCE.definitions().materials().purifiedNetherQuartzCrystal().maybeStack(1).get(),
                    new ItemStack(IUItem.iuingot, 1, 3),
                    new ItemStack(IUItem.sunnarium, 1, 3)
            );
        }
        MaceratorRecipe.addmacerator(
                Api.INSTANCE.definitions().materials().certusQuartzCrystal().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().certusQuartzDust().maybeStack(1).get()
        );
        MaceratorRecipe.addmacerator(
                Api.INSTANCE.definitions().materials().certusQuartzCrystalCharged().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().certusQuartzDust().maybeStack(1).get()
        );
        MaceratorRecipe.addmacerator(
                Api.INSTANCE.definitions().materials().fluixCrystal().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().fluixDust().maybeStack(1).get()
        );
        IUCore.list.add(Api.INSTANCE.definitions().materials().certusQuartzCrystal().maybeStack(1).get());
        IUCore.list.add(Api.INSTANCE.definitions().materials().certusQuartzCrystalCharged().maybeStack(1).get());

    }


}
