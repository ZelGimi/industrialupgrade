package com.denfop.items.armour;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

public class ItemArmorUtility extends ItemArmorBase implements ISpecialArmor {

    public ItemArmorUtility(String armorName, EntityEquipmentSlot type) {
        super(ArmorMaterial.DIAMOND, armorName, type);
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    public ISpecialArmor.ArmorProperties getProperties(
            EntityLivingBase player,
            ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        return new ISpecialArmor.ArmorProperties(0, 0.0, 0);
    }

    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
    }

}
