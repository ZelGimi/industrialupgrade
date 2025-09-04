package com.denfop.items.armour;

import com.denfop.api.item.energy.EnergyItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.register.Register;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class ItemArmorEnergy extends ItemArmorBase implements ISpecialArmor, EnergyItem {
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;

    public ItemArmorEnergy(
            String armorName,
            Type armorType,
            double maxCharge,
            double transferLimit,
            int tier
    ) {
        super(Register.ENERGY_ITEM, armorName, armorType);
        this.maxCharge = maxCharge;
        this.tier = tier;
        this.transferLimit = transferLimit;
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
        if (!p_41404_.has(DataComponentsInit.ENERGY))
            p_41404_.set(DataComponentsInit.ENERGY, 0D);
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
        if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
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
        switch (this.getEquipmentSlot()) {
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
