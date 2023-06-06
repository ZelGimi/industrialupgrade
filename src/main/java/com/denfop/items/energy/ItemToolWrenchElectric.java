package com.denfop.items.energy;

import com.denfop.items.BaseElectricItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.core.IC2;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.util.LogCategory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.LinkedList;
import java.util.List;

public class ItemToolWrenchElectric extends ItemToolWrench implements IPseudoDamageItem, IElectricItem, IItemHudInfo {

    public ItemToolWrenchElectric() {
        super("electric_wrench");
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
        this.setNoRepair();
    }

    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new LinkedList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        return info;
    }

    public boolean canTakeDamage(ItemStack stack, int amount) {
        amount *= 100;
        return ElectricItem.manager.getCharge(stack) >= (double) amount;
    }

    public void damage(ItemStack stack, int amount, EntityPlayer player) {
        ElectricItem.manager.use(stack, 100 * amount, player);
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxCharge(ItemStack stack) {
        return 12000.0;
    }

    public int getTier(ItemStack stack) {
        return 1;
    }

    public double getTransferLimit(ItemStack stack) {
        return 250.0;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants(this, subItems);
        }
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);
        if (damage != prev && BaseElectricItem.logIncorrectItemDamaging) {
            IC2.log.warn(
                    LogCategory.Armor,
                    new Throwable(),
                    "Detected invalid armor damage application (%d):",
                    damage - prev
            );
        }

    }

    public void setStackDamage(ItemStack stack, int damage) {
        super.setDamage(stack, damage);
    }

}
