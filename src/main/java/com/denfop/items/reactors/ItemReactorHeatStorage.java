package com.denfop.items.reactors;

import ic2.api.reactor.IReactor;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemReactorHeatStorage extends AbstractDamageableReactorComponent {

    public ItemReactorHeatStorage(String name, int heatStorage) {
        super(name, heatStorage);
    }

    public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return true;
    }

    public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return this.getMaxCustomDamage(stack);
    }

    public int getCurrentHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return this.getCustomDamage(stack);
    }

    public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        int myHeat = this.getCurrentHeat(stack, reactor, x, y);
        myHeat += heat;
        int max = this.getMaxHeat(stack, reactor, x, y);
        if (myHeat > max) {
            reactor.setItemAt(x, y, null);
            heat = max - myHeat + 1;
        } else {
            if (myHeat < 0) {
                heat = myHeat;
                myHeat = 0;
            } else {
                heat = 0;
            }

            this.setCustomDamage(stack, myHeat);
        }

        return heat;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(
            @Nonnull ItemStack stack,
            World world,
            @Nonnull List<String> tooltip,
            @Nonnull ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);
        if (this.getCustomDamage(stack) > 0) {
            tooltip.add(Localization.translate("ic2.reactoritem.heatwarning.line1"));
            tooltip.add(Localization.translate("ic2.reactoritem.heatwarning.line2"));
        }

    }

}
