package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemToolWrenchEnergy extends ItemToolWrench implements IEnergyItem {


    public boolean canTakeDamage(ItemStack stack, int amount) {
        amount *= 100;
        return ElectricItem.manager.getCharge(stack) >= (double) amount;
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "item."+pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }
    public int getBarWidth(ItemStack stack) {

        return 13 - (int) (13.0F * Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        ));
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            p_41392_.add(var4);
            p_41392_.add(new ItemStack(this, 1));
        }
    }

    public void damage(ItemStack stack, int amount, Player player) {
        ElectricItem.manager.use(stack, 100 * amount, player);
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxEnergy(ItemStack stack) {
        return 12000.0;
    }

    public short getTierItem(ItemStack stack) {
        return 1;
    }

    public double getTransferEnergy(ItemStack stack) {
        return 250.0;
    }

}
