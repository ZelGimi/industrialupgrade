package com.denfop.items.armour;

import com.denfop.ElectricItem;
import com.denfop.api.item.IEnergyItem;
import com.denfop.items.armour.material.ArmorMaterials;
import com.denfop.utils.ModUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class ItemArmorEnergy extends ItemArmorBase implements ISpecialArmor, IEnergyItem {
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;

    public ItemArmorEnergy(
            String armorName,
            EquipmentSlot armorType,
            double maxCharge,
            double transferLimit,
            int tier
    ) {
        super(ArmorMaterials.ENERGY_ITEM, armorName, armorType);
        this.maxCharge = maxCharge;
        this.tier = tier;
        this.transferLimit = transferLimit;
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }


    public ISpecialArmor.ArmorProperties getProperties(
            LivingEntity player,
            @NotNull ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        if (source.isBypassArmor()) {
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

    public int getArmorDisplay(Player player, ItemStack armor, int slot) {
        return ElectricItem.manager.getCharge(armor) >= (double) this.getEnergyPerDamage()
                ? (int) Math.round(20.0 * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio())
                : 0;
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
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }


    public void damageArmor(LivingEntity entity, ItemStack stack, DamageSource source, float damage, int slot) {
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


    public abstract double getDamageAbsorptionRatio();

    public abstract int getEnergyPerDamage();

    protected final double getBaseAbsorptionRatio() {
        switch (this.slot) {
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
