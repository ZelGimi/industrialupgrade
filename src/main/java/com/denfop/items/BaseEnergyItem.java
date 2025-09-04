package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BaseEnergyItem extends Item implements EnergyItem, IItemTab {
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;
    protected String nameItem;

    public BaseEnergyItem(double maxCharge, double transferLimit, int tier) {
        super(new Properties().setNoRepair().stacksTo(1));
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
            p_41392_.add(var4);
            p_41392_.add(new ItemStack(this, 1));
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }


    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
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

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxEnergy(ItemStack stack) {
        return this.maxCharge;
    }

    public short getTierItem(ItemStack stack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack stack) {
        return this.transferLimit;
    }


}
