package com.denfop.items.armour;

import com.denfop.ElectricItem;
import com.denfop.Localization;
import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.ModUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ISpecialArmor;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public abstract class ItemArmorEnergy extends ItemArmorBase implements ISpecialArmor, IEnergyItem {

    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;

    public ItemArmorEnergy(
            String armorName,
            EntityEquipmentSlot armorType,
            double maxCharge,
            double transferLimit,
            int tier
    ) {
        super(ArmorMaterial.DIAMOND, armorName, armorType);
        this.maxCharge = maxCharge;
        this.tier = tier;
        this.transferLimit = transferLimit;
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

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new LinkedList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        info.add(Localization.translate("iu.item.tooltip.PowerTier", this.tier));
        return info;
    }

    public void getSubItems(CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants(this, subItems);
        }
    }

    public ISpecialArmor.ArmorProperties getProperties(
            EntityLivingBase player,
            @NotNull ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        if (source.isUnblockable()) {
            return new ISpecialArmor.ArmorProperties(0, 0.0, 0);
        } else {
            double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
            int energyPerDamage = this.getEnergyPerDamage();
            int damageLimit = Integer.MAX_VALUE;
            if (energyPerDamage > 0) {
                damageLimit = (int) Math.min(
                        damageLimit,
                        25.0 * ElectricItem.manager.getCharge(armor) / (double) energyPerDamage
                );
            }

            return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
        }
    }

    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return ElectricItem.manager.getCharge(armor) >= (double) this.getEnergyPerDamage()
                ? (int) Math.round(20.0 * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio())
                : 0;
    }

    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        ElectricItem.manager.discharge(
                stack,
                damage * this.getEnergyPerDamage(),
                Integer.MAX_VALUE,
                true,
                false,
                false
        );
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

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);


    }

    public void setStackDamage(ItemStack stack, int damage) {
        super.setDamage(stack, damage);
    }

    public abstract double getDamageAbsorptionRatio();

    public abstract int getEnergyPerDamage();

    protected final double getBaseAbsorptionRatio() {
        switch (this.armorType) {
            case HEAD:
            case FEET:
                return 0.15;
            case CHEST:
                return 0.4;
            case LEGS:
                return 0.3;
            default:
                return 0.0;
        }
    }

}
