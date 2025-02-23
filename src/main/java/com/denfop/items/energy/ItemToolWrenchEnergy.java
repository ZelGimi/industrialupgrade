package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.ModUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemToolWrenchEnergy extends ItemToolWrench implements IEnergyItem {

    public ItemToolWrenchEnergy() {
        super("electric_wrench");
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
        this.setNoRepair();
    }

    public boolean showDurabilityBar(final ItemStack stack) {
        return true;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        );
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

    public double getMaxEnergy(ItemStack stack) {
        return 12000.0;
    }

    public short getTierItem(ItemStack stack) {
        return 1;
    }

    public double getTransferEnergy(ItemStack stack) {
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
        this.getDamage(stack);
    }


}
