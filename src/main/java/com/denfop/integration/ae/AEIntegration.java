package com.denfop.integration.ae;

import appeng.core.Api;
import appeng.tile.powersink.AEBasePoweredTile;
import com.denfop.IUCore;
import com.denfop.recipes.MaceratorRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class AEIntegration {

    public static void init() {
        MaceratorRecipe.addmacerator(
                new ItemStack(Items.QUARTZ),
                Api.INSTANCE.definitions().materials().netherQuartzDust().maybeStack(1).get()
        );
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

    public static boolean check(TileEntity tile) {
        return tile instanceof AEBasePoweredTile;
    }

}
