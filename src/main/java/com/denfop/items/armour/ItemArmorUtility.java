package com.denfop.items.armour;

import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;

public class ItemArmorUtility extends ItemArmorBase {
    public ItemArmorUtility(String armorName, Type p_40387_) {
        super(ArmorMaterials.DIAMOND, armorName, p_40387_);
    }

    public ItemArmorUtility(ArmorMaterial material, String armorName, Type p_40387_) {
        super(material, armorName, p_40387_);
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }
}
